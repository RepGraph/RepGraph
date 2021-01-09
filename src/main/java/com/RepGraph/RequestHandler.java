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

    AbstractModel RepModel;


    /**
     * Simple Home Mapping that returns "Welcome"
     *
     * @return String This is the welcome message
     */
    @GetMapping("/")
    @ResponseBody
    public String Home() {
        return "Welcome";
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
    public HashMap<String, Object> UploadData(@RequestParam("FileName") String name, @RequestParam("data") MultipartFile file) throws IOException {
        //This is where we would change framework model
        this.RepModel = new DMRSModel();

        RepModel.clearGraphs();
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
            AbstractGraph currgraph = objectMapper.readValue(currentLine, DMRSGraph.class);
            //checks if model doesnt contain the ID already and if it does dont add it and tell the user duplicates were found
            if (!RepModel.containsKey(currgraph.getId())) {

                RepModel.addGraph(currgraph);

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
    public HashMap<String, String> UploadDataSingle(@RequestBody AbstractGraph data) {
        HashMap<String, String> returninfo = new HashMap<>();

        returninfo.put("input", data.getInput());
        returninfo.put("id", data.getId());
        //add Graph to model
        RepModel.addGraph(data);

        return returninfo;
    }

    /**
     * This method will be called when the class receives a GET HTTP request with "/Visualise".
     * The Request URL also requires the "graphID" and "format" Request Params to be present.
     * This method finds the Graph in the model dataset and constructs it into the required
     * format according to the format type specified.
     *
     * @param graphID This refers to which Graph the user wants to be visualised on the front end.
     * @param format  This refers to which format the user wants the Graph to be visualised in.
     *                format = 1 - hierarchical
     *                format = 2 - tree-like
     *                format = 3 - flat
     *                format = 4 - planar
     * @return HashMap<String, Object> This is the Graph visualisation information.
     */
    @GetMapping("/Visualise")
    @ResponseBody
    public HashMap<String, Object> Visualise(@RequestParam String graphID, @RequestParam int format) {
        return RepModel.Visualise(graphID, format);
    }

    /**
     * This method creates and visualises a subset of a Graph in a desired format. This method is mapped to "/DisplaySubset" URL
     * and requires the Request Parameters as follows:
     *
     * @param graphID    This is the Graph ID of the Graph where the subset is constructed from
     * @param NodeID     This is the ID of the Node which the subset is constructed from
     * @param SubsetType This is the type of subset i.e descendent or adjacent
     * @param format     this is the format of the visualisation
     *                   format = 1 - hierarchical
     *                   format = 2 - tree-like
     *                   format = 3 - flat
     *                   format = 4 - planar
     * @return HashMap<String, Object> This is the Graph visualisation information.
     */
    @GetMapping("/DisplaySubset")
    @ResponseBody
    public HashMap<String, Object> DisplaySubset(@RequestParam String graphID, @RequestParam String NodeID, @RequestParam String SubsetType, @RequestParam int format) {
        return RepModel.DisplaySubset(graphID, NodeID, SubsetType, format);
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
    public HashMap<String, Object> SearchSubgraphNodeSet(@RequestParam ArrayList<String> labels) {

        return RepModel.searchSubgraphNodeSet(labels);

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
    public HashMap<String, Object> SearchSubgraphPattern(@RequestParam String graphID, @RequestParam String[] NodeId, @RequestParam int[] EdgeIndices) {
        return RepModel.searchSubgraphPattern(graphID, NodeId, EdgeIndices);
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
    public HashMap<String, Object> CompareGraphs(@RequestParam String graphID1, @RequestParam String graphID2) {
        return RepModel.compareTwoGraphs(graphID1, graphID2);
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
    public HashMap<String, Object> TestGraph(@RequestParam String graphID, @RequestParam boolean planar, @RequestParam boolean longestPathDirected, @RequestParam boolean longestPathUndirected, @RequestParam boolean connected) {
        return RepModel.runFormalTests(graphID, planar, longestPathDirected, longestPathUndirected, connected);

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
    public AbstractGraph GetGraph(@RequestParam String graphID) {
        return RepModel.getGraph(graphID);
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
    public AbstractGraph GetSubset(@RequestParam String graphID, @RequestParam String NodeID, @RequestParam String SubsetType) {
        if (SubsetType.equals("adjacent")) {
            return RepModel.CreateSubsetAdjacent(graphID, NodeID);
        } else if (SubsetType.equals("descendent")) {
            return RepModel.CreateSubsetDescendent(graphID, NodeID);
        }
        return null;
    }

    /**
     * Method to parse entered sentence into ACE parser
     * and ultimately format it into dmrs format.
     * This Graph is then added to the model and visualisation information is returned.
     *
     * @param sentence Sentence to be parsed
     * @param format   Visualisation format that the sentence is returned in.
     * @return HashMap<String, Object> Visualisation data of entered sentence
     * @throws Exception
     */
    @GetMapping("/ParseSentence")
    @ResponseBody
    public HashMap<String, Object> parseSentence(@RequestParam String sentence, @RequestParam int format) throws Exception {

        //Replace all spaces with unique character to parse it to java python argument
        String result = sentence.replaceAll(" ", "_&_&_*_*");
        String jsonString = null;
        String s = "";
        AbstractGraph currgraph;
        try {
            //Run parser
            Process p = Runtime.getRuntime().exec("python3 Scripts/src/parse-convert-mrs.py --convert_semantics --extract_semantics -g Scripts/src -s " + '"' + result + '"');

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the dmrs output from command
            while ((s = stdInput.readLine()) != null) {
                if (s.startsWith("{\"id\":")) {
                    jsonString = s;
                }
            }
            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            //Creates Graph object from JSON string

            currgraph = objectMapper.readValue(jsonString, AbstractGraph.class);

            currgraph.setId(currgraph.getInput().hashCode() + "");
            //checks if model doesnt contain the ID already
            if (!RepModel.containsKey(currgraph.getId())) {

                RepModel.addGraph(currgraph);

            }

        } catch (Exception e) {

            HashMap<String, Object> error = new HashMap<>();
            error.put("Error", "Parser Error");
            return error;

        }


        return Visualise(currgraph.getId(), format);
    }

    @GetMapping("/ReturnModelList")
    @ResponseBody
    public ArrayList<HashMap<String, String>> GetModelList() {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        for (AbstractGraph g : RepModel.getAllGraphs().values()) {
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
