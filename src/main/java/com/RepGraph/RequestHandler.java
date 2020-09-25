package com.RepGraph;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the RestAPI class and controller in the MVC format. This class will handle all communication between
 * the front-end and back-end. The class makes use of the Spring Framework to handle all the GET and POST request mappings.
 * It also handles automatic serialisation and deserialization of JSON to Java objects and vice versa.
 *
 * @author TLDEDA001
 *
 */
@CrossOrigin
@SpringBootApplication
@RestController
public class RequestHandler {

    RepGraphModel RepModel = new RepGraphModel();

    /**
     * This method is the post request to upload data and create the model.
     *
     * @param name This is the name that the file will be saved under
     * @param file This is the file data.
     * @return HashMap<String, Object> This is a hashmap to return the necessary uploaded file information and a response.
     */
    @PostMapping("/UploadData")
    @ResponseBody
    public HashMap<String, Object> UploadData(@RequestParam("FileName") String name, @RequestParam("data") MultipartFile file) {
        RepModel.clearGraphs();
        HashMap<String, Object> returnobj = new HashMap<>();

        //List of graph ids and graph inputs in a hashmap.
        ArrayList<HashMap<String, String>> returninfo = new ArrayList<>();

        try {
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

            //Read the contents of the file uploaded and construct a graph for each line;
            BufferedReader reader = new BufferedReader(new FileReader(serverFile));
            String currentLine;
            ObjectMapper objectMapper = new ObjectMapper();
            boolean duplicates = false;
            while ((currentLine = reader.readLine()) != null) {
                graph currgraph = objectMapper.readValue(currentLine, graph.class);

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
            HashMap<String, String> response = new HashMap<>();
            if (duplicates) {
                returnobj.put("response", "Duplicates Found");

            } else {
                returnobj.put("response", "No Duplicates Found");

            }

            returnobj.put("data", returninfo);
        } catch (Exception e) {
            e.printStackTrace();
            return returnobj;
        }
        return returnobj;
    }


    /**
     * This method is the POST request that takes in a single graph JSON and uploads a single graph to the model object. It is mapped to "/UploadSingle".
     *
     * @param data This is the graph object to be uploaded
     * @return HashMap<String, String> This is a hashmap object with the graph id and graph input.
     */
    @PostMapping("/UploadSingle")
    @ResponseBody
    public HashMap<String, String> UploadDataSingle(@RequestBody graph data) {
        HashMap<String, String> returninfo = new HashMap<>();

        returninfo.put("input", data.getInput());
        returninfo.put("id", data.getId());

        RepModel.addGraph(data);

        return returninfo;
    }

    /**
     * This method will be called when the class receives a GET HTTP request with "/Visualise".
     * The Request URL also requires the "index" and "format" Request Params to be present.
     * This method finds the graph in the model dataset and constructs it into the required
     * format according to the format type specified.
     *
     * @param graphID  This refers to which graph the user wants to be visualised on the front end.
     * @param format This refers to which format the user wants the graph to be visualised in.
     * @return graph This method returns a graph object of the requested graph.
     */
    @GetMapping("/Visualise")
    @ResponseBody
    public graph Visualise(@RequestParam String graphID, @RequestParam int format) {
        return RepModel.getGraph(graphID);
    }

    /**
     * This method will be called when the class receives a GET HTTP request with "/SearchSubgraphNodeSet".
     * The Request URL also requires the "graphID" and "NodeID" list Request Params to be present.
     * This method searches the model's dataset for graphs containing the specified list of node labels and
     * returns a list of graph IDs where the set of node labels have been found.
     *
     * @param graphID This is the graph containing the nodes and their corresponding labels to be searched for
     * @param NodeID  This is the list of node indexes
     * @return ArrayList<String> This is the list of graph ids of graphs that have matching node labels
     */
    @GetMapping("/SearchSubgraphNodeSet")
    @ResponseBody
    public ArrayList<String> SearchSubgraphNodeSet(@RequestParam String graphID, @RequestParam int[] NodeID) {

        return RepModel.searchSubgraphNodeSet(graphID, NodeID);
    }

    /**
     * This method will be called when the class receives a GET HTTP request with "/SearchSubgraphPattern".
     * The Request URL also requires the "graphID" Request Param and "subgraph" graph object requestbody to be present.
     * This method searches the model's dataset for graphs containing the specified subgraph pattern and
     * returns a list of graph IDs where the subgraph pattern has been found.
     *
     * @param subgraph This is the graph object containing the subgraph information.
     * @return ArrayList<String> This is a list a graph IDs with matching subgraph patterns
     */
    @GetMapping("/SearchSubgraphPattern")
    @ResponseBody
    public ArrayList<String> SearchSubgraphPattern(@RequestBody graph subgraph) {
        return RepModel.searchSubgraphPattern(subgraph);
    }

    /**
     * This method will be called when the class receives a GET HTTP request with "/CompareGraphs".
     * The Request URL also requires the "index1" and "index2" Request Params to be present.
     * This method finds both graphs requested to be compared in the model's dataset and performs the
     * comparison analysis.
     *
     * @param graphID1 This refers to one of the indexes of the graphs to be compared.
     * @param graphID2 This refers to the other index of the graph to be compared.
     */
    @GetMapping("/CompareGraphs")
    @ResponseBody
    public void CompareGraphs(@RequestParam String graphID1, @RequestParam String graphID2) {
        RepModel.compareTwoGraphs(graphID1, graphID2);

    }

    /**
     * This method will be called when the class receives a GET HTTP request with "/TestGraph".
     * The Request URL also requires the "index" and "test" Request Params to be present.
     * This method runs the formal tests requested on the model specified and returns the results.
     *
     * @param graphID          This refers to the id of the graph that will be tested.
     * @param planar      This refers to whether its getting tested for the graph being planar
     * @param connected   This refers to whether its getting tested for being connected
     * @param longestpath This refers to finding the longest path
     */
    @GetMapping("/TestGraph")
    @ResponseBody
    public void TestGraph(@RequestParam String graphID, @RequestParam boolean planar, @RequestParam boolean longestpath, @RequestParam boolean connected) {
        RepModel.runFormalTests(graphID, planar, longestpath, connected);

    }

    public static void main(String[] args) {
        SpringApplication.run(RequestHandler.class, args);
    }
}
