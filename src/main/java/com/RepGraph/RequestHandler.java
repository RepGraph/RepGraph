package com.RepGraph;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;

/**
 * This is the RestAPI class and controller in the MVC format. This class will handle all communication between
 * the front-end and back-end.
 *
 * @author TLDEDA001
 * @version 1
 * @since 24-Aug-2020
 *
 */

@SpringBootApplication
@RestController
public class RequestHandler {

    RepGraphModel RepModel = new RepGraphModel();

    /**
     * This method is the post request to upload data and create the model.
     *
     * @param name
     * @param file
     */
    @PostMapping("/UploadData")
    @ResponseBody
    public void UploadData(@RequestParam("FileName") String name, @RequestParam("data") MultipartFile file) {

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

            BufferedReader reader = new BufferedReader(new FileReader(serverFile));
            String currentLine;
            ObjectMapper objectMapper = new ObjectMapper();
            while ((currentLine = reader.readLine()) != null) {
                graph currgraph = objectMapper.readValue(currentLine, graph.class);
                RepModel.addGraph(currgraph);
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
        //RepModel.getGraph("20001001");
    }


    /**
     * Uploads a single graph to the model object
     *
     * @param data This is the graph object to be uploaded
     */
    @PostMapping("/UploadSingle")
    @ResponseBody
    public void UploadDataSingle(@RequestBody graph data) {
        RepModel.addGraph(data);

    }

    /**
     * This method will be called when the class receives a GET HTTP request with the "Visualise" keyword in the Request URL.
     * The Request URL also requires the "index" and "format" Request Params to be present.
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
     * This method will be called when the class receives a GET HTTP request with the "SearchSubgraphNodeSet" keyword in the Request URL.
     * The Request URL also requires the "graphID" and "NodeID" list Request Params to be present.
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
     * This method will be called when the class receives a GET HTTP request with the "SearchSubgraphPattern" keyword in the Request URL.
     * The Request URL also requires the "graphID" Request Param and "subgraph" graph object requestbody to be present.
     *
     * @param graphID  This is the graph id of the graph with the pattern to be searched
     * @param subgraph This is the graph object containing the subgraph information.
     * @return ArrayList<String> This is a list a graph IDs with matching subgraph patterns
     */
    @GetMapping("/SearchSubgraphPattern")
    @ResponseBody
    public ArrayList<String> SearchSubgraphPattern(@RequestParam String graphID, @RequestBody graph subgraph) {
        return RepModel.searchSubgraphPattern(graphID, subgraph);
    }

    /**
     * This method will be called when the class receives a GET HTTP request with the "CompareGraphs" keyword in the Request URL.
     * The Request URL also requires the "index1" and "index2" Request Params to be present.
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
     * This method will be called when the class receives a GET HTTP request with the "TestGraph" keyword in the Request URL.
     * The Request URL also requires the "index" and "test" Request Params to be present.
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
