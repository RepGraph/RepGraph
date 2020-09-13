package com.RepGraph;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RequestHandlerTest {

    @Autowired
    private MockMvc mockMvc;


    private final String BASE_URL = "localhost:8080";

    private graph testgraph;


    @Before
    public void construct_test_graph() {

        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();

        nodes.add(new node(0, "node1", new ArrayList<anchors>()));
        nodes.add(new node(1, "node2", new ArrayList<anchors>()));
        nodes.add(new node(2, "node3", new ArrayList<anchors>()));
        nodes.add(new node(3, "node4", new ArrayList<anchors>()));

        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 3, "testlabel", "testpostlabel"));
        edges.add(new edge(0, 2, "testlabel", "testpostlabel"));

        tokens.add(new token(0, "node 1", "node1", "node1"));
        tokens.add(new token(1, "node 2", "node2", "node2"));
        tokens.add(new token(2, "node 3", "node3", "node3"));
        tokens.add(new token(3, "node 4", "node4", "node4"));

        testgraph = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges);

    }

    //Later
    @Test
    public void test_UploadData_RepModelShouldBePopulated() {
    }

    @Test
    public void test_UploadDataSingle_GraphShouldBeAddedToModel() throws Exception {

        String URL = "/UploadSingle";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

        String requestJSON = writer.writeValueAsString(testgraph);


        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("id").value("11111"));

    }

}
