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
 */

@SpringBootApplication
@RestController
public class RequestHandler {

    /**
     * This method will be called when the class receives a GET HTTP request with the "Visualise" keyword in the Request URL.
     * The Request URL also requires the "index" and "format" Request Params to be present.
     *
     * @param index  This refers to which graph the user wants to be visualised on the front end.
     * @param format This refers to which format the user wants the graph to be visualised in.
     */
    @GetMapping("/Visualise")
    @ResponseBody
    public void Visualise(@RequestParam int index, @RequestParam int format) {
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
     * @param index1 This refers to one of the indexes of the graphs to be compared.
     * @param index2 This refers to the other index of the graph to be compared.
     */
    @GetMapping("/CompareGraphs")
    @ResponseBody
    public void CompareGraphs(@RequestParam int index1, @RequestParam int index2) {
    }

    /**
     * This method will be called when the class receives a GET HTTP request with the "TestGraph" keyword in the Request URL.
     * The Request URL also requires the "index" and "test" Request Params to be present.
     *
     * @param index This refers to the index of the graph that will be tested.
     * @param test  The refers to the TestId of the test to be performed.
     */
    @GetMapping("/TestGraph")
    @ResponseBody
    public void TestGraph(@RequestParam int index, @RequestParam int test) {
    }

    public static void main(String[] args) {
        SpringApplication.run(RequestHandler.class, args);
    }
}
