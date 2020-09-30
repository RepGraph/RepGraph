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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RequestHandlerTest {

    @Autowired
    private MockMvc mockMvc;


    private final String BASE_URL = "localhost:8080";

    private graph testgraph, testgraph2;


    @Before
    public void construct_test_graph() {

        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();

        ArrayList<anchors> anch1 = new ArrayList<anchors>();
        ArrayList<anchors> anch2 = new ArrayList<anchors>();
        ArrayList<anchors> anch3 = new ArrayList<anchors>();
        ArrayList<anchors> anch4 = new ArrayList<anchors>();
        anch1.add(new anchors(0, 0));
        anch2.add(new anchors(1, 1));
        anch3.add(new anchors(2, 2));
        anch4.add(new anchors(3, 3));

        nodes.add(new node(0, "node1", anch1));
        nodes.add(new node(1, "node2", anch2));
        nodes.add(new node(2, "node3", anch3));
        nodes.add(new node(3, "node4", anch4));

        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 3, "testlabel", "testpostlabel"));
        edges.add(new edge(2, 3, "testlabel", "testpostlabel"));

        tokens.add(new token(0, "node 1", "node1", "node1"));
        tokens.add(new token(1, "node 2", "node2", "node2"));
        tokens.add(new token(2, "node 3", "node3", "node3"));
        tokens.add(new token(3, "node 4", "node4", "node4"));

        testgraph = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        ArrayList<node> nodes2 = new ArrayList<>();
        ArrayList<edge> edges2 = new ArrayList<>();
        ArrayList<token> tokens2 = new ArrayList<>();

        nodes2.add(new node(0, "node3", anch1));
        nodes2.add(new node(1, "node4", anch2));
        nodes2.add(new node(2, "node5", anch3));
        nodes2.add(new node(3, "node6", anch4));

        edges2.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges2.add(new edge(1, 3, "testlabel", "testpostlabel"));
        edges2.add(new edge(0, 2, "testlabel", "testpostlabel"));

        tokens2.add(new token(0, "node3", "node3", "node3"));
        tokens2.add(new token(1, "node4", "node4", "node4"));
        tokens2.add(new token(2, "node5", "node5", "node5"));
        tokens2.add(new token(3, "node6", "node6", "node6"));

        testgraph2 = new graph("22222", "testsource", "node3 node4 node5 node6", nodes2, tokens2, edges2, new ArrayList<Integer>());

    }


    @Test
    public void test_UploadData_FileIsUploaded() throws Exception {
        String URL = "/UploadData";

        FileInputStream testfilestream = new FileInputStream(new File("src/test/TestResources/wsj00a.dmrs"));
        MockMultipartFile file = new MockMultipartFile("data", "testUploadDataFile", "text/plain", testfilestream);

        mockMvc.perform(MockMvcRequestBuilders.multipart(URL)
                .file(file)
                .param("FileName", "UploadUnitTestData"))
                .andExpect(status().isOk());
    }

    @Test
    public void test_UploadDataSingle_GraphShouldBeAddedToModel() throws Exception {

        String URL = "/UploadSingle";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

        String requestJSON = writer.writeValueAsString(testgraph);

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJSON))
                .andExpect(status().isOk());

    }

    @Test
    public void test_Visualise_returnsGraph() throws Exception {
        String URL = "/Visualise";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

        String requestJSON = writer.writeValueAsString(testgraph);

        mockMvc.perform(post("/UploadSingle").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJSON));

        String graphID = "11111";
        String format = "1";

        mockMvc.perform(get(URL)
                .param("graphID", graphID)
                .param("format", format))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(requestJSON));
    }

    @Test
    public void test_SearchSubgraphNodeSet_CorrectlyReturnsList() throws Exception {
        String URL = "/SearchSubgraphNodeSet";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = writer.writeValueAsString(testgraph);

        mockMvc.perform(post("/UploadSingle").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJSON));

        String requestJSON2 = writer.writeValueAsString(testgraph2);

        mockMvc.perform(post("/UploadSingle").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJSON2));

        String[] labels = new String[]{"node3", "node4"};


        mockMvc.perform(get(URL)
                .param("labels", labels))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("[\"11111\",\"22222\"]"));
    }

    @Test
    public void test_SearchSubgraphPattern_CorrectlyReturnsList() throws Exception {
        String URL = "/SearchSubgraphPattern";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = writer.writeValueAsString(testgraph);

        mockMvc.perform(post("/UploadSingle").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJSON));

        String requestJSON2 = writer.writeValueAsString(testgraph2);

        mockMvc.perform(post("/UploadSingle").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJSON2));


        mockMvc.perform(get(URL)
                .param("graphID", "11111")
                .param("NodeId", new String[]{"2", "3"})
                .param("EdgeIndices", new String[]{"2"}))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("[\"11111\",\"22222\"]"));
    }

    @Test
    public void test_CompareGraphs_CorrectlyComparesGraphs() throws Exception {
        String URL = "/CompareGraphs";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = writer.writeValueAsString(testgraph);

        mockMvc.perform(post("/UploadSingle").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJSON));

        String requestJSON2 = writer.writeValueAsString(testgraph2);

        mockMvc.perform(post("/UploadSingle").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJSON2));


        mockMvc.perform(get(URL)
                .param("graphID1", "11111")
                .param("graphID2", "22222"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_TestGraph_CorrectlyRunsFormalTests() throws Exception {
        String URL = "/TestGraph";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = writer.writeValueAsString(testgraph);

        mockMvc.perform(post("/UploadSingle").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJSON));

        String requestJSON2 = writer.writeValueAsString(testgraph2);

        mockMvc.perform(post("/UploadSingle").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJSON2));

        String planar = "true";
        String longestPathDirected = "true";
        String longestPathUndirected = "true";
        String connected = "true";

        mockMvc.perform(get(URL)
                .param("graphID", "11111")
                .param("planar", planar)
                .param("longestPathDirected", longestPathDirected)
                .param("longestPathUndirected", longestPathUndirected)
                .param("connected", connected))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }



}
