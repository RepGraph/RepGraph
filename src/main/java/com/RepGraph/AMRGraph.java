package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.PTBTokenizer;

import javax.swing.text.StyledEditorKit;
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
    public AMRGraph(@JsonProperty("id") String id, @JsonProperty("source") String source, @JsonProperty("input") String input, @JsonProperty("nodes") ArrayList<Node> nodes, @JsonProperty("edges") ArrayList<Edge> edges, @JsonProperty("tops") ArrayList<Integer> top) throws IOException, InterruptedException {
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

        fillInSpans(this.nodes);

    }

    @Override
    public ArrayList<Token> extractTokensFromNodes() {
        ArrayList<Token> tokenlist = new ArrayList<>();

        int index = 0;

        PTBTokenizer<CoreLabel> ptbt = PTBTokenizer.newPTBTokenizer(new StringReader(this.input), false, true);
        while (ptbt.hasNext()) {
            CoreLabel label = ptbt.next();
            tokenlist.add(new Token(index, label.originalText(), label.word(), label.word()));
            index++;
        }

        return tokenlist;

    }

    public void alignUtil(String NodeID, HashMap<String, Node> nodes, HashMap<String, Boolean> visited, int layer, FileWriter writer) throws IOException {
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
                alignUtil(n, nodes, visited, layer, writer);
            } else {
                writer.write(")");
            }
        }
        writer.write(")");

    }

    public void alignNodes() throws IOException, InterruptedException {
        setNodeNeighbours();
        HashMap<String, Boolean> visited = new HashMap<>();
        for (String i : nodes.keySet()) {
            visited.put(i, false);
        }
        FileWriter myWriter = new FileWriter("supportScripts/temp/AMR_TEMP.txt");
        myWriter.write("#::snt " + this.input + "\n");
        myWriter.write("(v" + this.top + " / " + nodes.get(this.top).getLabel() + " ");

        alignUtil(this.top, this.nodes, visited, 0, myWriter);

        myWriter.close();


        Process proc = Runtime.getRuntime().exec(new String[]{"python3", "supportScripts/align_AMR.py"});
        proc.waitFor();
        BufferedReader input =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = null;
        while ((line = input.readLine()) != null) {

            if (line.startsWith("v")) {
                String nodeid = line.substring(1, line.indexOf(" "));
                int from = Integer.parseInt(line.substring(line.indexOf("(") + 1, line.indexOf(",")));
                int end = from;

                if (line.substring(line.indexOf(",") + 1, line.indexOf(")")) != "") {
                    end = Integer.parseInt(line.substring(line.indexOf(",") + 1, line.indexOf(")")));
                }

                Anchors a = new Anchors(from, end);
                nodes.get(nodeid).setAnchors(new ArrayList<Anchors>());
                nodes.get(nodeid).getAnchors().add(a);

            }
        }


    }

    public void fillInSpans(HashMap<String, Node> nodes) {
        ArrayList<ArrayList<String>> order = getPaths(nodes);

        for (ArrayList<String> path : order) {
            if (nodes.get(path.get(0)).getAnchors() != null) {
                continue;
            }
            int min = Integer.MAX_VALUE;
            for (int i = 1; i < path.size(); i++) {
                if (nodes.get(path.get(i)).getAnchors() != null) {
                    min = Math.min(min, nodes.get(path.get(i)).getAnchors().get(0).getFrom());
                }
            }
            if (min == Integer.MAX_VALUE) {
                for (Node n : nodes.get(path.get(0)).getUndirectedNeighbours()) {
                    if (n.getAnchors() != null) {
                        min = Math.min(min, n.getAnchors().get(0).getFrom());
                    }
                }
            }
            if (min != Integer.MAX_VALUE) {
                ArrayList<Anchors> arr = new ArrayList<>();
                Anchors a = new Anchors(min, min);
                arr.add(a);
                nodes.get(path.get(0)).setAnchors(arr);
            }
        }

    }

    private void topologicalSort(String nodeID, HashMap<String, Node> nodes, HashMap<String, Boolean> visited,
                             Stack<String> stack) {

        visited.put(nodeID, true);
        Iterator<Node> it = nodes.get(nodeID).getDirectedNeighbours().iterator();

        String i;
        while (it.hasNext()) {
            i = it.next().getId();
            if (!visited.get(i))
                topologicalSort(i, nodes, visited, stack);
        }

        stack.push((nodeID));
    }

    private ArrayList<ArrayList<String>> getPaths(HashMap<String, Node> nodes) {

        Stack<String> stack = new Stack<String>();

        HashMap<String, Boolean> visited = new HashMap<>();

        for (String i : nodes.keySet()) {
            visited.put(i, false);
        }

        for (Node n : nodes.values()) {
            if (!visited.get(n.getId()))
                topologicalSort(n.getId(), nodes, visited, stack);

        }

        ArrayList<String> order = new ArrayList<>();
        while (stack.empty() == false) {
            order.add(stack.pop());
        }

        ArrayList<ArrayList<String>> searches = new ArrayList<>();
        for (String id : order) {
            searches.add(getDFSOrder(id, nodes));
        }

        return searches;
    }


    private  ArrayList<String> getDFSOrder(String node, HashMap<String, Node> nodes) {
        ArrayList<String> order = new ArrayList<>();

        HashMap<String, Boolean> visited = new HashMap<>();
        for (String i : nodes.keySet()) {
            visited.put(i, false);
        }

        Stack<String> stack = new Stack<>();


        stack.push(node);

        while (stack.empty() == false) {

            node = stack.peek();
            stack.pop();


            if (visited.get(node) == false) {
                order.add(node);
                visited.put(node, true);
            }


            Iterator<Node> itr = nodes.get(node).getDirectedNeighbours().iterator();

            while (itr.hasNext()) {
                String v = itr.next().getId();
                if (!visited.get(v)) {
                    stack.push(v);
                }
            }

        }
        return order;
    }


}
