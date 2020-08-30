package com.RepGraph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

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

    RepGraphModel RepModel;


    /**
     * This method is the post request to upload data and create the model
     *
     * @param data This parameter is the json string of data that is automatically converted into a repgraph model.
     * @return RepGraphModel - this method returns the method it creates which is a RepGraphModel of the data uploaded
     */
    @PostMapping("/Upload")
    @ResponseBody
    public RepGraphModel UploadData(@RequestBody RepGraphModel data) {
        RepModel = data;
        return RepModel;
    }


    /**
     * This method will be called when the class receives a GET HTTP request with the "Visualise" keyword in the Request URL.
     * The Request URL also requires the "index" and "format" Request Params to be present.
     *
     * @param id  This refers to which graph the user wants to be visualised on the front end.
     * @param format This refers to which format the user wants the graph to be visualised in.
     * @return graph This method returns a graph object of the requested graph.
     */
    @GetMapping("/Visualise")
    @ResponseBody
    public graph Visualise(@RequestParam String id, @RequestParam int format) {
        return RepModel.getGraph(id);
    }


    /**
     * This method will be called when the class receives a GET HTTP request with the "SearchSubgraph" keyword in the Request URL.
     * The Request URL also requires the "Subgraph" Request Param to be present.
     *
     * @param Subgraph This refers the subgraph pattern that will be searched over all the models
     */
    @GetMapping("/SearchSubgraph")
    @ResponseBody
    public void SearchSubgraph(@RequestParam String Subgraph) {
    }

    /**
     * This method will be called when the class receives a GET HTTP request with the "CompareGraphs" keyword in the Request URL.
     * The Request URL also requires the "index1" and "index2" Request Params to be present.
     *
     * @param id1 This refers to one of the indexes of the graphs to be compared.
     * @param id2 This refers to the other index of the graph to be compared.
     */
    @GetMapping("/CompareGraphs")
    @ResponseBody
    public void CompareGraphs(@RequestParam String id1, @RequestParam String id2) {
    }

    /**
     * This method will be called when the class receives a GET HTTP request with the "TestGraph" keyword in the Request URL.
     * The Request URL also requires the "index" and "test" Request Params to be present.
     *
     * @param id This refers to the id of the graph that will be tested.
     * @param planar This refers to whether its getting tested for the graph being planar
     * @param connected This refers to whether its getting tested for being connected
     * @param longestpath This refers to finding the longest path
     */
    @GetMapping("/TestGraph")
    @ResponseBody
    public void TestGraph(@RequestParam String id, @RequestParam boolean planar, @RequestParam boolean longestpath, @RequestParam boolean connected) {
    }

    public static void main(String[] args) {
        SpringApplication.run(RequestHandler.class, args);
    }
}
