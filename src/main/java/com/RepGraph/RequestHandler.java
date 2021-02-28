package com.RepGraph;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;


/**
 * This is the RestAPI class and controller in the MVC format. This class will handle all communication between
 * the front-end and back-end. The class makes use of the Spring Framework to handle all the GET and POST request mappings.
 * It also handles automatic serialisation and deserialization of JSON to Java objects and vice versa.
 */

@CrossOrigin
@SpringBootApplication
@RestController
public class RequestHandler {

    public static final String USER_HEADER = "X-USER";


    HashMap<String,AbstractModel> RepModel = new HashMap<>();

    /**
     * Testing function
     */
    @GetMapping(value = "/")
    @ResponseBody
    public void Home(@RequestHeader(USER_HEADER) String userID) throws IOException, InterruptedException {

    }

    /**
     * This method is the post request to upload data to the model. It is mapped to "/UploadData"
     *
     * @param name This is the name that the file will be saved under. The RequestParam is "FileName"
     * @param file This is the file data. The RequestParam is "data"
     * @return HashMap<String, Object> This is a hashmap to return the necessary uploaded file information
     * and a response about whether or not there are duplicate graphs.
     * Keys for the return object:
     * - Response : a response message
     * - data : list of Graph IDs (key - id) and Graph inputs (key - input)
     */
    @PostMapping("/UploadData")
    @ResponseBody
    public HashMap<String, Object> UploadData(@RequestHeader(USER_HEADER)String userID,@RequestParam("FileName") String name, @RequestParam("Framework") String framework, @RequestParam("data") MultipartFile file) throws IOException {
        System.out.println(userID);
        //This is where we would change framework model
        switch (framework){
            case "1":
                this.RepModel.put(userID,new DMRSModel());
                break;
            case "2":
                this.RepModel.put(userID,new EDSModel());
                break;
            case "3":
                this.RepModel.put(userID,new PTGModel());
                break;
            case "4":
                this.RepModel.put(userID,new UCCAModel());
                break;
            case "5":
                this.RepModel.put(userID,new AMRModel());
                break;
        }

        RepModel.get(userID).clearGraphs();
        HashMap<String, Object> returnobj = new HashMap<>();

        //List of Graph ids and Graph inputs in a hashmap.
        ArrayList<HashMap<String, String>> returninfo = new ArrayList<>();


        byte[] bytes = file.getBytes();
        //Creates Directory if it does not exist otherwise it finds it in the project folder.
        File directory = new File("Dataset");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        //Creates File in directory
        File serverFile = new File(directory.getAbsolutePath() + File.separator + name);

        //Writes contents to file
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        //Read the contents of the file uploaded and construct a Graph for each line;
        BufferedReader reader = new BufferedReader(new FileReader(serverFile));
        String currentLine;
        ObjectMapper objectMapper = new ObjectMapper();
        boolean duplicates = false;
        while ((currentLine = reader.readLine()) != null) {
            //Creates Graph object from JSON string
            AbstractGraph currgraph;
            if (framework.equals("1")) {
                currgraph = objectMapper.readValue(currentLine, DMRSGraph.class);

            } else if (framework.equals("2")) {
                currgraph = objectMapper.readValue(currentLine, EDSGraph.class);

            } else if (framework.equals("3")) {
                currgraph = objectMapper.readValue(currentLine, PTGGraph.class);

            }else if (framework.equals("4")) {
                currgraph = objectMapper.readValue(currentLine, UCCAGraph.class);

            }else {
                currgraph = objectMapper.readValue(currentLine, AMRGraph.class);

            }
            //checks if model doesnt contain the ID already and if it does dont add it and tell the user duplicates were found
            if (!RepModel.containsKey(currgraph.getId())) {

                RepModel.get(userID).addGraph(currgraph);

                HashMap<String, String> returnGraph = new HashMap<>();
                returnGraph.put("id", currgraph.getId());
                returnGraph.put("input", currgraph.getInput());
                returninfo.add(returnGraph);
            } else {
                duplicates = true;
            }
        }
        reader.close();

        if (duplicates) {
            returnobj.put("response", "Duplicates Found");

        } else {
            returnobj.put("response", "Data-set Uploaded Successfully");

        }

        returnobj.put("data", returninfo);


        return returnobj;
    }


    /**
     * This method is the POST request that takes in a single Graph JSON and uploads a single Graph to the model object. It is mapped to "/UploadSingle".
     *
     * @param data This is the Graph object to be uploaded
     * @return HashMap<String, String> This is a hashmap object with the Graph id and Graph input.
     * Key:
     * - id : Graph ID
     * - input : Graph input
     */
    @PostMapping("/UploadSingle")
    @ResponseBody
    public HashMap<String, String> UploadDataSingle(@RequestHeader(USER_HEADER)String userID,@RequestParam("Framework") String framework,@RequestBody AbstractGraph data) {
        switch (framework){
            case "1":
                this.RepModel.put(userID,new DMRSModel());
                break;
            case "2":
                this.RepModel.put(userID,new EDSModel());
                break;
            case "3":
                this.RepModel.put(userID,new PTGModel());
                break;
            case "4":
                this.RepModel.put(userID,new UCCAModel());
                break;
            case "5":
                this.RepModel.put(userID,new AMRModel());
                break;
        }

        HashMap<String, String> returninfo = new HashMap<>();

        returninfo.put("input", data.getInput());
        returninfo.put("id", data.getId());
        //add Graph to model
        RepModel.get(userID).addGraph(data);

        return returninfo;
    }





    /**
     * This method will be called when the class receives a GET HTTP request with "/SearchSubgraphNodeSet".
     * The Request URL also requires the Request Parameter "labels" - List of Node labels to be present.
     * This method searches the model's dataset for graphs containing the specified list of Node labels and
     * returns information on graphs where the set of Node labels have been found.
     *
     * @param labels list of labels to be searched for.
     * @return HashMap<String, Object> This is the list of Graph ids and Graph inputs under the "data" key of the returned hashmap.
     * The "data" key returns a list of hashmaps that have "id" and "input" keys.
     * The "Response" key returns an error message if an error has taken place.
     */
    @GetMapping("/SearchSubgraphNodeSet")
    @ResponseBody
    public HashMap<String, Object> SearchSubgraphNodeSet(@RequestHeader(USER_HEADER)String userID,@RequestParam ArrayList<String> labels) {

        return RepModel.get(userID).searchSubgraphNodeSet(labels);

    }

    /**
     * This method will be called when the class receives a GET HTTP request with "/SearchSubgraphPattern".
     * The Request URL also requires the "graphID", "NodeId" - list of Node ids, "EdgeIndices" list of Edge indices Request Params to be present.
     * This method searches the model's dataset for graphs containing the specified subgraph pattern and
     * returns information on graphs where the subgraph pattern has been found.
     *
     * @return HashMap<String, Object> This is the list of Graph ids and Graph inputs under the "data" key of the returned hashmap.
     * The "data" key returns a list of hashmaps that have "id" and "input" keys.
     * The "Response" key returns an error message if an error has taken place.
     */
    @GetMapping("/SearchSubgraphPattern")
    @ResponseBody
    public HashMap<String, Object> SearchSubgraphPattern(@RequestHeader(USER_HEADER)String userID,@RequestParam String graphID, @RequestParam String[] NodeID, @RequestParam int[] EdgeIndices) {
        return RepModel.get(userID).searchSubgraphPattern(graphID, NodeID, EdgeIndices);
    }

    /**
     * This method will be called when the class receives a GET HTTP request with "/CompareGraphs".
     * The Request URL also requires the "index1" and "index2" Request Params to be present.
     * This method finds both graphs requested to be compared in the model's dataset and performs the
     * comparison analysis.
     *
     * @param graphID1 This refers to one of the indexes of the graphs to be compared.
     * @param graphID2 This refers to the other index of the Graph to be compared.
     * @return HashMap<String, Object> The differences and similarities of the two graphs i.e
     * the "SimilarNodes1" key gives the Node ids of the similar nodes in graph1.
     * the "SimilarNodes2" key gives the Node ids of the similar nodes in graph2.
     * the "SimilarEdges1" key gives the Node ids of the similar edges in graph1.
     * the "SimilarEdge2" key gives the Node ids of the similar edges in graph2.
     */
    @GetMapping("/CompareGraphs")
    @ResponseBody
    public HashMap<String, Object> CompareGraphs(@RequestHeader(USER_HEADER)String userID,@RequestParam String graphID1, @RequestParam String graphID2,@RequestParam boolean strict,@RequestParam boolean noAbstract,@RequestParam boolean noSurface ) {
        return RepModel.get(userID).compareTwoGraphs(graphID1, graphID2,strict,noAbstract,noSurface);
    }

    /**
     * This method will be called when the class receives a GET HTTP request with "/TestGraph".
     * The Request URL also requires the "graphID", "planar","longestPathDirected","longestPathUndirected", and "connected" Request Params to be present.
     * This method runs the formal tests requested on the model specified and returns the results.
     *
     * @param graphID               This refers to the id of the Graph that will be tested.
     * @param planar                This refers to whether its getting tested for the Graph being planar
     * @param connected             This refers to whether its getting tested for being connected
     * @param longestPathDirected   This refers to finding the longest directed path
     * @param longestPathUndirected This refers to finding the longest undirected path
     * @return HashMap<String, Object> Results of the tests i.e
     * the "Planar" key returns a boolean of whether or not the Graph is planar
     * the "PlanarVis" returns the planar visualisation data.
     * the "LongestPathDirected" key returns an ArrayList of an Arraylist of integers defining the multiple longest directed paths in the graphs
     * the "LongestPathUndirected" key returns an ArrayList of an Arraylist of integers defining the multiple longest undirected paths in the graphs
     * the "Connected" returns a boolean of whether or not the Graph is connected.
     */
    @GetMapping("/TestGraph")
    @ResponseBody
    public HashMap<String, Object> TestGraph(@RequestHeader(USER_HEADER)String userID,@RequestParam String graphID, @RequestParam boolean planar, @RequestParam boolean longestPathDirected, @RequestParam boolean longestPathUndirected, @RequestParam boolean connected) {
        return RepModel.get(userID).runFormalTests(graphID, planar, longestPathDirected, longestPathUndirected, connected);

    }

    /**
     * This method gets only the Graph data of a specific Graph ID in the model and it is mapped to "/GetGraph"
     * The Request URL also requires the "graphID" request param.
     *
     * @param graphID This is the ID of the Graph that is requested.
     * @return Graph Returns the Graph data
     */
    @GetMapping("/GetGraph")
    @ResponseBody
    public AbstractGraph GetGraph(@RequestHeader(USER_HEADER)String userID,@RequestParam String graphID) {
        return RepModel.get(userID).getGraph(graphID);
    }

    /**
     * This method gets only the Graph data of a created subset. This method is mapped to "/GetSubset"
     * This method requires the "graphID","headNodeID", and "SubsetType" request parameters.
     *
     * @param graphID    This is the Graph ID of the Graph where the subset is created from
     * @param NodeID     This is the Node ID of the starting point of subset creation.
     * @param SubsetType This is the type of subset being created. "adjacent" or "descendent"
     * @return Graph Returns a Graph object of the subset
     */
    @GetMapping("/GetSubset")
    @ResponseBody
    public AbstractGraph GetSubset(@RequestHeader(USER_HEADER)String userID,@RequestParam String graphID, @RequestParam String NodeID, @RequestParam String SubsetType) {
        AbstractGraph graph = RepModel.get(userID).graphs.get(graphID);
        if (SubsetType.equals("adjacent")) {
            return RepModel.get(userID).CreateSubsetAdjacent(graph, NodeID);
        } else if (SubsetType.equals("descendent")) {
            return RepModel.get(userID).CreateSubsetDescendent(graph, NodeID);
        }
        return null;
    }


    @GetMapping("/ReturnModelList")
    @ResponseBody
    public ArrayList<HashMap<String, String>> GetModelList(@RequestHeader(USER_HEADER)String userID) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        for (AbstractGraph g : RepModel.get(userID).getAllGraphs().values()) {
            HashMap<String, String> graphinfo = new HashMap<String, String>();
            graphinfo.put("id", g.getId());
            graphinfo.put("input", g.getInput());
            list.add(graphinfo);
        }
        Collections.sort(list, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                return o1.get("id").compareTo(o2.get("id"));

            }
        });
        return list;
    }



    /**
     * Main Method to run the spring boot application and host the API.
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(RequestHandler.class, args);
    }
}
