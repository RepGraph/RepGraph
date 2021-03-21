import lodash from "lodash";
import {v4 as uuidv4} from 'uuid';

export function createDummyNodes(graphData, parents, children, createEdges) {
    let dummyNodes = []
    let dummyEdges = []

    for (let [key, node] of graphData.nodes) {
        if (node.anchors !== null && node.anchors.length > 1) {
            let skip =[]
            for (let i = 0; i < node.anchors.length; i++) {
                for (let j = 0; j < node.anchors.length; j++){
                    if ((i!==j)&&(node.anchors[j].from<=node.anchors[i].from && node.anchors[j].end>=node.anchors[i].from && node.anchors[j].end<=node.anchors[i].end) || (node.anchors[j].end===node.anchors[i].from-1)){
                        node.anchors[j].end = node.anchors[i].end;
                        skip.push(i);
                    }
                }
            }

            for (let i = 1; i < node.anchors.length; i++) {

                if (skip.includes(i)){
                    continue;
                }

                let anchs = [];
                anchs.push(node.anchors[i]);
                let uuid = uuidv4().toString()

                let nodeClone = lodash.cloneDeep(node)
                nodeClone = {
                    dummy: true,
                    ...nodeClone,
                    label: nodeClone.label + " ID:"+ nodeClone.id+ " Span "+ (i + 1),
                    id: uuid,
                    anchors: [nodeClone.anchors[i]]
                }
                dummyNodes.push(nodeClone)
                children.set(uuid, []);
                parents.set(uuid, []);
                for (let parent of parents.get(node.id)) {
                    if (createEdges === true) {
                        dummyEdges.push({source: parent, target: uuid, label: "\"\""})
                    }
                    let temp = parents.get(uuid);
                    temp.push(parent);
                    parents.set(uuid, temp);
                }
                for (let child of children.get(node.id)) {
                    if (createEdges === true) {
                        dummyEdges.push({source: uuid, target: child, label: "\"\""})
                    }
                    let temp = children.get(uuid);
                    temp.push(child);
                    children.set(uuid, temp);
                }

            }
        }
    }

    for (let node of dummyNodes) {
        graphData.nodes.set(node.id, node);
    }
    graphData.edges = graphData.edges.concat(dummyEdges);

    return graphData;
}

export function addTokenSpanText(graphData) {

    for (let j = 0; j < graphData.nodes.length; j++) {
        let node = graphData.nodes[j];
        let output = ""
        if (node.anchors !== null) {
            for (let i = 0; i < node.anchors.length; i++) {
                let anch = node.anchors[i];
                for (let i = anch.from; i < anch.end + 1; i++) {
                    output += graphData.tokens.find((token)=>(token.index === i)).form + " "
                }
                output = output.trim();
                anch = {...anch, text: output}
                output = "";
                graphData.nodes[j].anchors[i] = anch;
            }
        }
    }

}

export const childrenAnchors = (node, children, visited, graphClone, nodesWithoutAnchors) => {

    if (visited[node.id] && node.anchors === null) {
        return {from: Number.MAX_VALUE, end: Number.MAX_VALUE};
    } else if (visited[node.id] && node.anchors !== null) {
        return {from: node.anchors[0].from, end: node.anchors[0].end};
    }
    visited[node.id] = true;
    if (children.get(node.id).length === 0 && node.anchors === null) {

        return {from: Number.MAX_VALUE, end: Number.MAX_VALUE};
        ;
    } else if (node.anchors !== null) {
        return {from: node.anchors[0].from, end: node.anchors[0].end};
    } else {

        let anchors = [];
        for (let childID of children.get(node.id)) {
            if (graphClone.nodes.get(childID).anchors === null) {

                let temp = childrenAnchors(graphClone.nodes.get(childID), children, visited, graphClone, nodesWithoutAnchors);

                if (temp.from !== Number.MAX_VALUE) {
                    graphClone.nodes.set(node.id, {...node, anchors: [{from: temp.from, end: temp.end}], span: false});
                    nodesWithoutAnchors.push(node.id);
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

export const setAnchors = (graphClone, children, parents, nodesWithoutAnchors) => {
    let topological = getPath(graphClone, children);

    for (let nodeID of topological) {
        let node = graphClone.nodes.get(nodeID)

        if (node.anchors === null) {
            let vis = {};
            for (let node of graphClone.nodes.values()) {
                vis[node.id] = false;
            }
            nodesWithoutAnchors.push(node.id);
            let anch = [];
            anch.push(childrenAnchors(node, children, vis, graphClone, nodesWithoutAnchors));
            if (anch[0].from === Number.MAX_VALUE) {
                for (let parentID of parents.get(node.id)) {
                    if (graphClone.nodes.get(parentID).anchors !== null) {
                        if (graphClone.nodes.get(parentID).anchors[0].from < anch[0].from) {
                            anch[0] = graphClone.nodes.get(parentID).anchors[0];
                        }
                    }
                }
            }

            graphClone.nodes.set(node.id, {...node, anchors: anch, span: false});

        } else {
            graphClone.nodes.set(node.id, {...node, span: true});
        }
    }
}

export const controlPoints = (source, target, direction, degree, graphLayoutSpacing) => {
    let x1 = 0;
    let y1 = 0;
    let offsetX = 0;
    let offsetY = 0;

    if (direction === "vertical-left") {
        if (source.y < target.y) {
            x1 = Math.min(
                target.x - (source.y - target.y) * degree,
                target.x + graphLayoutSpacing.intraLevelSpacing + graphLayoutSpacing.nodeWidth - 25
            );
            offsetX = 20;
        } else {
            x1 = Math.max(
                target.x - (source.y - target.y) * degree,
                target.x - graphLayoutSpacing.intraLevelSpacing - graphLayoutSpacing.nodeWidth + 25
            );
            offsetX = -20;
        }
        y1 = (source.y + target.y) / 2;
        offsetY = -20;
    } else if (direction === "vertical-right") {
        if (source.y < target.y) {
            x1 = Math.max(
                target.x + (source.y - target.y) * degree,
                target.x - graphLayoutSpacing.intraLevelSpacing - graphLayoutSpacing.nodeWidth + 25
            );
            offsetX = -20;
        } else {
            x1 = Math.min(
                target.x + (source.y - target.y) * degree,
                target.x + graphLayoutSpacing.intraLevelSpacing + graphLayoutSpacing.nodeWidth - 25
            );
            offsetX = 20;
        }
        y1 = (source.y + target.y) / 2;
        offsetY = -20;
    } else if (direction === "horizontal-left") {
        x1 = (source.x + target.x) / 2;
        y1 = Math.min(target.y + (source.x - target.x) * degree, target.y + graphLayoutSpacing.tokenLevelSpacing);
        offsetX = 0;
        offsetY = source.x < target.x ? -20 : 20;
    } else if (direction === "horizontal-right") {
        x1 = (source.x + target.x) / 2;
        y1 = Math.min(target.y - (source.x - target.x) * degree, target.y + graphLayoutSpacing.tokenLevelSpacing);
        offsetX = 0;
        offsetY = source.x < target.x ? 20 : -20;
    } else if (direction === "custom") {
        x1 = target.x;
        y1 = source.y;
        offsetX = source.x < target.x ? 30 : -30;
        offsetY = 20;
    } else if (direction === "duplicate") {
        if (source.x < target.x) {
            x1 = (source.x + target.x) / 2 + graphLayoutSpacing.nodeWidth;
        } else {
            x1 = (source.x + target.x) / 2 - graphLayoutSpacing.nodeWidth;
        }
        y1 = (source.y + target.y) / 2 - 2 * graphLayoutSpacing.nodeHeight;
        offsetY = source.x === target.x ? -10 : -20;
        offsetX = source.y === target.y ? 0 : source.x < target.x ? 22 : -22;
    } else {
        x1 = (source.x + target.x) / 2;
        y1 = (source.y + target.y) / 2;
        if (source.y < target.y) {
            offsetY = source.x === target.x ? -10 : -20;
            offsetX = source.y === target.y ? 0 : source.x < target.x ? 20 : -20;
        } else {
            offsetY = source.x === target.x ? -20 : -10;
            offsetX = source.y === target.y ? 0 : source.x < target.x ? -20 : 20;
        }
    }
    return {x1, y1, offsetX, offsetY};
}

export const edgeRulesSameColumn = (
    edge,
    source,
    target,
    finalGraphNodes,
    finalGraphEdges,
    graphLayoutSpacing
) => {
    let direction = "";
    let degree = 0.4;
    for (let e of finalGraphEdges) {
        if (source.id === e.source && target.id === e.target && edge !== e) {
            //There exists a duplicate edge
            if (edge.label.localeCompare(e.label) <= 0) {
                direction = "vertical-right";
            } else {
                direction = "vertical-left";
            }
            degree = 0.3;
            return controlPoints(source, target, direction, degree, graphLayoutSpacing);
        }
    }
    if (Math.abs(source.relativeY - target.relativeY) !== 1) {
        //In the same column and more than level apart
        let found = false;
        for (let node of finalGraphNodes) {
            if (node.x === source.x) {
                if (
                    (node.y > source.y && node.y < target.y) ||
                    (node.y < source.y && node.y > target.y)
                ) {
                    //There exists a node inbetween the target and source node
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            //Make the edge curve to avoid the clash, otherwise straight.
            let left = false;
            //Check if there is an outgoing or incoming edge from the left side
            for (let edge of finalGraphEdges) {
                if (edge.source === source.id && edge.target !== target.id) {
                    let neighbourNode =
                        finalGraphNodes[
                            finalGraphNodes.findIndex((node) => node.id === edge.target)
                            ];
                    if (
                        neighbourNode.x < source.x &&
                        Math.abs(neighbourNode.x - source.x) ===
                        graphLayoutSpacing.nodeWidth + graphLayoutSpacing.intraLevelSpacing
                    ) {
                        left = true;
                    }
                }
            }

            //Make the edge go to absolute left, need to check direction of edge.
            if (target.y < source.y) {
                if (left) {
                    direction = "vertical-right";
                } else {
                    direction = "vertical-left";
                }
            } else {
                if (left) {
                    direction = "vertical-left";
                } else {
                    direction = "vertical-right";
                }
            }
        }
    }
    return controlPoints(source, target, direction, degree, graphLayoutSpacing);
}

export const edgeRulesSameRow = (
    edge,
    source,
    target,
    finalGraphNodes,
    finalGraphEdges,
    graphLayoutSpacing
) => {
    let direction = "";
    let degree = 0.25;

    if (Math.abs(target.x - source.x) !== graphLayoutSpacing.intraLevelSpacing + graphLayoutSpacing.nodeWidth) {
        //On the same level and more than 1 space apart

        let found = false;
        for (let node of finalGraphNodes) {
            if (node.y === source.y) {
                if (
                    (node.x > source.x && node.x < target.x) ||
                    (node.x < source.x && node.x > target.x)
                ) {
                    //There exists a node in between the target and source node
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            if (source.x < target.x) {
                direction = "horizontal-right";
            } else {
                direction = "horizontal-left";
            }
        }
    }

    if (Math.abs(source.x - target.x) / (graphLayoutSpacing.intraLevelSpacing + graphLayoutSpacing.nodeWidth) > 6) {
        degree = 0.3;
    }

    //Is there an identical edge? Change their curvature to ensure they don't overlap.
    for (let e of finalGraphEdges) {
        if (edge.source === e.source && edge.target === e.target && edge !== e) {
            //There exists a duplicate edge
            if (edge.label.localeCompare(e.label) <= 0) {
                degree = degree + 0.15;
            }
            break;
        }
    }

    return controlPoints(source, target, direction, degree, graphLayoutSpacing);
}

export const edgeRulesOther = (
    edge,
    source,
    target,
    finalGraphNodes,
    finalGraphEdges,
    graphLayoutSpacing
) => {
    let direction = "";
    let degree = 0.2;

    //Is there an identical edge? If yes than 1 go left 1 go right, else straight line
    for (let e of finalGraphEdges) {
        if (edge.source === e.source && edge.target === e.target && edge !== e) {
            //There exists a duplicate edge
            if (edge.label.localeCompare(e.label) <= 0) {
                direction = "duplicate";
                return controlPoints(source, target, direction, degree, graphLayoutSpacing);
            }
            break;
        }
    }

    //Is there a node protruding close to the line
    let xProtrusion = 0.0;
    let yProtrusion = 0.0;
    if (source.y < target.y) {
        for (let node of finalGraphNodes) {
            if (
                node.y > source.y &&
                node.y < target.y &&
                ((node.x < source.x && node.x > target.x) ||
                    (node.x > source.x && node.x < target.x))
            ) {

                let xtemp = Math.abs(node.x - source.x);
                let ytemp = Math.abs(node.y - target.y);
                if (ytemp > yProtrusion && xtemp > xProtrusion) {
                    xProtrusion = xtemp;
                    yProtrusion = ytemp;
                }
            }
        }
    }

    xProtrusion = (xProtrusion / Math.abs(target.x - source.x)).toFixed(3);
    yProtrusion = (yProtrusion / Math.abs(target.y - source.y)).toFixed(3);
    if (xProtrusion >= 0.5 && yProtrusion >= 0.5) {
        direction = "custom";
        return controlPoints(source, target, direction, degree, graphLayoutSpacing);
    }

    //Is the source node inbetween two column (due to its span) and is the target node within less of 1 columns distance from it? If so, check for a node below the source node.
    if (Math.abs(target.x - source.x) < graphLayoutSpacing.intraLevelSpacing + graphLayoutSpacing.nodeWidth) {
        for (let node of finalGraphNodes) {
            if (source.x === node.x && source.y < node.y) {
                degree = 0.1;
                if (source.x < target.x) {
                    direction = "vertical-left";
                } else {
                    direction = "vertical-right";
                }
                break;
            }
        }
    }

    return controlPoints(source, target, direction, degree, graphLayoutSpacing);
}