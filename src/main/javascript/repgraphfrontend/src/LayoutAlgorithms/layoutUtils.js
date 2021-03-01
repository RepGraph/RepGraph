import lodash from "lodash";
import {v4 as uuidv4} from 'uuid';

export function createDummyNodes(graphData, parents, children,createEdges) {
    let dummyNodes = []
    let dummyEdges = []
    for (const node of graphData.nodes) {

        if (node.anchors !== null && node.anchors.length > 1) {

            for (let i = 1; i < node.anchors.length; i++) {
                let anchs = [];
                anchs.push(node.anchors[i]);
                let uuid = uuidv4().toString()

                let nodeClone = lodash.cloneDeep(node)
                nodeClone = {
                    ...nodeClone,
                    label: nodeClone.label + " " + (i + 1),
                    id: uuid,
                    anchors: [nodeClone.anchors[i]]
                }
                dummyNodes.push(nodeClone)

                if (createEdges===true){
                children.set(uuid, []);
                parents.set(uuid, []);
                for (let parent of parents.get(node.id)) {
                    dummyEdges.push({source: parent, target: uuid, label: "\"\""})

                    let temp = parents.get(uuid);
                    temp.push(parent);
                    parents.set(uuid, temp);
                }
                for (let child of children.get(node.id)) {
                    dummyEdges.push({source: uuid, target: child, label: "\"\""})
                    let temp = children.get(uuid);
                    temp.push(child);
                    children.set(uuid, temp);
                }
                }
            }
        }
    }
    graphData.nodes = graphData.nodes.concat(dummyNodes)
    graphData.edges = graphData.edges.concat(dummyEdges)

    return graphData;
}

export const childrenAnchors = (node, children, visited, graphClone) => {
    console.log("node", node.id)
    if (visited[node.id] && node.anchors === null) {
        return {from: Number.MAX_VALUE, end: Number.MAX_VALUE};
    } else if (visited[node.id] && node.anchors !== null) {
        return {from: node.anchors[0].from, end: node.anchors[0].end};
    }
    visited[node.id] = true;
    if (children.get(node.id).length === 0 && node.anchors === null) {
        console.log("WE HERE")
        return {from: Number.MAX_VALUE, end: Number.MAX_VALUE};
        ;
    } else if (node.anchors !== null) {
        return {from: node.anchors[0].from, end: node.anchors[0].end};
    } else {

        let anchors = [];
        for (let childID of children.get(node.id)) {
            if (graphClone.nodes.get(childID).anchors === null) {

                let temp = childrenAnchors(graphClone.nodes.get(childID), children, visited, graphClone);

                if (temp.from != Number.MAX_VALUE) {
                    graphClone.nodes.set(node.id, {...node, anchors: [{from: temp.from, end: temp.end}], span: false});
                }
                anchors.push({from: temp.from, end: temp.end});


            } else {

                anchors.push({
                    from: graphClone.nodes.get(childID).anchors[0].from,
                    end: graphClone.nodes.get(childID).anchors[0].end
                });
            }
        }
        let leftMost = anchors[0];
        for (let i = 1; i < anchors.length; i++) {
            if (leftMost.from > anchors[i].from) {
                leftMost = anchors[i];
            }
        }
        return leftMost;
    }
}

export const topologicalSort = (node, children, visited, stack, graphClone) => {

    visited[node.id] = true;

    for (let child of children.get(node.id)) {

        if (!visited[child]) {
            topologicalSort(graphClone.nodes.get(child), children, visited, stack, graphClone);
        }
    }

    stack.push((node.id));
    return stack;
}

export const getPath = (graphClone, children) => {

    let stack = []

    let visited = {};

    for (let node of graphClone.nodes.values()) {
        visited[node.id] = false;
    }

    for (let n of graphClone.nodes.values()) {

        if (!visited[n.id]) {
            topologicalSort(n, children, visited, stack, graphClone);
        }

    }
    let order = [];
    while (stack.length !== 0) {
        order.push(stack.pop());
    }
    return order;
}


