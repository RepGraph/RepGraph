package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

/**
 * The Graph class represents a single sentence which comprises of nodes, edges and tokens.
 */
public class AMRGraph extends AbstractGraph {


    /**
     * Default constructor for the Graph class.
     */
    public AMRGraph() {
        super();
    }

    @JsonCreator
    public AMRGraph(@JsonProperty("id") String id, @JsonProperty("source") String source, @JsonProperty("input") String input, @JsonProperty("nodes") ArrayList<Node> nodes, @JsonProperty("edges") ArrayList<Edge> edges, @JsonProperty("tops") ArrayList<Integer> top) throws IOException {
        this.id = id;
        this.source = source;
        this.input = input;
        this.edges = edges;

        for (Node n : nodes) {
            this.nodes.put(n.getId(), n);

        }
        this.setNodeNeighbours();
        this.top = top.get(0) + "";
        alignNodes();

        populateTokens();

    }


    public void alignUtil(String NodeID, HashMap<String, Boolean> visited, int layer, FileWriter writer) throws IOException {
        // Mark the current node as visited and print it
        visited.put(NodeID, true);

        Iterator<Node> i = nodes.get(NodeID).getDirectedNeighbours().iterator();
        Iterator<Edge> ie = nodes.get(NodeID).getDirectedEdgeNeighbours().iterator();

        if (i.hasNext()) {
            layer += 1;
        }
        while (i.hasNext()) {
            String n = i.next().getId();
            String ne = ie.next().getLabel();
            writer.write(" :" + ne + " ( v" + n + " / " + nodes.get(n).getLabel() + " ");
            if (!visited.get(n)) {
                alignUtil(n, visited, layer, writer);
            } else {
                writer.write(")");
            }
        }
        writer.write(")");

    }

    public void alignNodes() throws IOException {
        setNodeNeighbours();
        HashMap<String, Boolean> visited = new HashMap<>();
        for (String i : nodes.keySet()) {
            visited.put(i, false);
        }
        FileWriter myWriter = new FileWriter("supportScripts/temp/AMR_TEMP.txt");
        myWriter.write("#::snt " + this.input + "\n");
        myWriter.write("(v" + this.top + " / " + nodes.get(this.top).getLabel() + " ");

        alignUtil(this.top, visited, 0, myWriter);

        myWriter.close();


        Process proc = Runtime.getRuntime().exec(new String[]{"python3", "supportScripts/align_AMR.py"});

        BufferedReader input =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = null;
        while ((line = input.readLine()) != null) {
            if (line.startsWith("v")) {
                String nodeid = line.substring(1, line.indexOf(" "));
                int from = Integer.parseInt(line.substring(line.indexOf("(") + 1, line.indexOf(",")));
                int end;
                if (line.substring(line.indexOf(",") + 1, line.indexOf(")")) != "") {
                    end = Integer.parseInt(line.substring(line.indexOf(",") + 1, line.indexOf(")")));
                } else {
                    end = from;
                }
                Anchors a = new Anchors(from, end);
                nodes.get(nodeid).setAnchors(new ArrayList<Anchors>());
                nodes.get(nodeid).getAnchors().add(a);

            }
        }


    }


}
