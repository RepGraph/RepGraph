// const nodeHeight = 40;
// const nodeWidth = 80;
// const interLevelSpacing = 80;
// const intraLevelSpacing = 50;
// const tokenLevelSpacing = 140;

import {childrenAnchors, getPath, topologicalSort} from "./layoutUtils";

export const layoutTree = (graphData, graphLayoutSpacing) => {

        const {nodeHeight, nodeWidth, interLevelSpacing, intraLevelSpacing, tokenLevelSpacing} = graphLayoutSpacing;

        const lodash = require('lodash');
        let graphClone = lodash.cloneDeep(graphData);
        console.log("graphClone", graphClone);

        let nodeMap = new Map();
        for (let node of graphClone.nodes) {
            nodeMap.set(node.id, node);
        }
        graphClone.nodes = nodeMap;

        let children = new Map();
        let parents = new Map();

        //Assign empty neighbour arrays to each node id
        for (const nodeID of graphClone.nodes.keys()) {
            children.set(nodeID, []);
            parents.set(nodeID, []);
        }

        //Fill in children node id's and parent node ids for each node.
        for (const e of graphClone.edges) {
            let temp = children.get(e.source);
            temp.push(e.target);
            children.set(e.source, temp);

            temp = parents.get(e.target);
            temp.push(e.source);
            parents.set(e.target, temp);
        }

        let topologicalStacks = new Map(); //Will hold each node's descendent nodes

        //Fill the topological stacks map with the number descendants each node has.
        for (const nodeOuter of graphClone.nodes.values()) {
            let stack = [];
            let visited = {};

            for (const nodeID of graphClone.nodes.keys()) {
                visited[nodeID.id] = false;
            }

            topologicalStacks.set(
                nodeOuter.id,
                topologicalSort(nodeOuter, children, visited, stack, graphClone)
            );
        }
        let nodesWithoutAnchors = []; //Array to keep track of nodes which originally had no anchors
        let topological = getPath(graphClone, children);

        for (let node of topological) {

            if (node.anchors === null) {
                let vis = {};
                for (let node of graphClone.nodes.values()) {
                    vis[node.id] = false;
                }
                nodesWithoutAnchors.push(node.id);
                let anch = [];
                anch.push(childrenAnchors(node, children, vis, graphClone));
                if (anch[0].from === Number.MAX_VALUE) {
                    for (let parentID of parents.get(node.id)) {
                        console.log("parentID", parentID)
                        if (graphClone.nodes.get(parentID).anchors !== null) {
                            if (graphClone.nodes.get(parentID).anchors[0].from < anch[0].from) {
                                anch[0] = graphClone.nodes.get(parentID).anchors[0];
                            }
                        }
                    }
                }

                graphClone.nodes.set(node.id, {...node, anchors: anch});

            }

        }

        let numLevels = 0;
        for (const stack of topologicalStacks.values()) {
            numLevels = Math.max(numLevels, stack.length);
        }
        //Group nodes together based on the number of descendents they have.
        let nodesInLevels = [];
        for (
            let numDescendents = 1;
            numDescendents < numLevels + 1;
            numDescendents++
        ) {
            let currentLevel = [];
            for (let node of graphClone.nodes.values()) {
                //console.log("node: ",node," Length: ",topologicalStacks.get(node.id).length);
                if (topologicalStacks.get(node.id).length === numDescendents) {
                    currentLevel.push(node);
                }
            }
            nodesInLevels.push(currentLevel);
        }

        let xPositions = new Map();
        let lowestNode = new Map();


        //Populate the xPositions map with each node's xPosition and the lowestNode map with the lowest node in each column.
        for (let level of nodesInLevels) {
            for (let n of level) {
                let column;
                if (n.anchors !== null) {
                    column = n.anchors[0].from;
                } else {
                    column = null;
                }
                xPositions.set(n.id, column);
                if (!lowestNode.has(column) && n.anchors !== null) {
                    lowestNode.set(column, n.id);
                }
            }
        }


        //This decides a nodes xPosition based on its neighbours/children.
        for (let level of nodesInLevels) {
            for (let n of level) {
                if (
                    xPositions.get(n.id) !== null &&
                    lowestNode.get(xPositions.get(n.id)) !== n.id
                ) {
                    //If the current node in the current level isn't the lowest node in its column
                    if (children.get(n.id).length === 0) {
                    } else if (children.get(n.id).length === 1) {
                        //If the current node has a single neighbour
                        xPositions.set(n.id, xPositions.get(children.get(n.id)[0])); //Set the current node's xPosition to its neighbour's
                    } else {

                        let childrenX = [];
                        for (let neighbour of children.get(n.id)) {
                            childrenX.push(xPositions.get(neighbour));
                        }
                        childrenX = childrenX.sort();
                        let middleChild = childrenX[Math.floor(childrenX.length / 2)];
                        xPositions.set(n.id, middleChild); //Set the node's x position to its middle child's
                    }
                }
            }
        }


        for (let level = 0; level < nodesInLevels.length; level++) {
            for (let n of nodesInLevels[level]) {
                if (xPositions.get(n.id) === null) {
                    if (children.get(n.id).length !== 0) {
                        xPositions.set(n.id, xPositions.get(children.get(n.id)[0]));
                    } else if (parents.get(n.id).length !== 0) {
                        let parentID = parents.get(n.id)[0];
                        let xPos = xPositions.get(parentID);
                        xPositions.set(n.id, xPos);
                    } else {
                        // ??
                    }
                }
            }
        }


        let nodesInFinalLevels = [];
        nodesInFinalLevels[0] = new Map();

        //Algorithm to resolve overlapping nodes.
        let numNodesProcessed = 0;
        let currentLevel = 0;
        while (numNodesProcessed !== graphClone.nodes.size) {
            let nodeXPos = new Map();
            nodesInFinalLevels[currentLevel + 1] = new Map();
            if (currentLevel === nodesInLevels.length - 1) {
                nodesInLevels[currentLevel + 1] = [];
            }
            for (let n of nodesInLevels[currentLevel]) {
                if (n.anchors === null &&
                    parents.get(n.id)[0] !== null &&
                    xPositions.get(parents.get(n.id)[0]) === xPositions.get(n.id) &&
                    lowestNode.get(xPositions.get(parents.get(n.id)[0])) === parents.get(n.id)[0]
                ) {
                    //Ensures that a node without anchors is not the lowest node in the column because the lowest node has the anchoring edge attached to its token below. If it is the lowest node, it will move it up above its parent in the tree.
                    if (currentLevel === 0) {
                        nodesInLevels[currentLevel + 1].push(n);
                    } else if (!nodesInLevels[currentLevel - 1].includes(parents.get(n.id)[0])) {
                        nodesInLevels[currentLevel + 1].push(n);
                    } else if (nodeXPos.has(xPositions.get(n.id))) { //If this xPosition is already taken in this level
                        //Move the current occupying node up, so the anchorless node stays with its parent.
                        let currentOccupyingNodeID = nodeXPos.get(xPositions.get(n.id));
                        nodeXPos.set(xPositions.get(n.id), n.id);
                        nodesInLevels[currentLevel + 1].push(graphClone.nodes.get(currentOccupyingNodeID));
                        nodesInFinalLevels[currentLevel].delete(currentOccupyingNodeID);
                        nodesInFinalLevels[currentLevel].set(n.id, n);
                    } else {
                        //Designate this node as the node occupying node this xPosition on this level.
                        nodeXPos.set(xPositions.get(n.id), n.id);
                        nodesInFinalLevels[currentLevel].set(n.id, n);
                        numNodesProcessed++;
                    }
                } else if (nodeXPos.has(xPositions.get(n.id))) { //If this xPosition is already taken in this level
                    if (
                        topologicalStacks.get(n.id).length <
                        topologicalStacks.get(nodeXPos.get(xPositions.get(n.id))).length
                    ) {
                        //Check which of the overlapping nodes have more descendents
                        //Set the node with the higher descendents in the next level, and remove it from this level.
                        let currentOccupyingNodeID = nodeXPos.get(xPositions.get(n.id));
                        nodeXPos.set(xPositions.get(n.id), n.id);
                        nodesInLevels[currentLevel + 1].push(graphClone.nodes.get(currentOccupyingNodeID));
                        nodesInFinalLevels[currentLevel].delete(currentOccupyingNodeID);
                        nodesInFinalLevels[currentLevel].set(n.id, n);
                    } else {
                        nodesInLevels[currentLevel + 1].push(n);
                    }
                } else {
                    //Designate this node as the node occupying node this xPosition on this level.
                    nodeXPos.set(xPositions.get(n.id), n.id);
                    nodesInFinalLevels[currentLevel].set(n.id, n);
                    numNodesProcessed++;
                }
            }
            currentLevel++;
        }

        //convert from array of maps to array of arrays
        let nodesInFinalLevelsArray = [nodesInFinalLevels.length];
        let level = 0;
        for (let i = 0; i < nodesInFinalLevels.length; i++) {
            if (nodesInFinalLevels[i].size !== 0) { //Removes levels with no nodes to create a more condensed graph.
                nodesInFinalLevelsArray[level] = [];
                for (let n of nodesInFinalLevels[i].values()) {
                    nodesInFinalLevelsArray[level].push(n);
                }
                level++;
            }
        }

        let height = nodesInFinalLevelsArray.length;

        const totalGraphHeight =
            height * nodeHeight + (height - 1) * interLevelSpacing; //number of levels times the height of each node and the spaces between them

        const tokens = graphClone.tokens.map((token) => ({
            ...token,
            id: token.index + graphClone.nodes.length,
            x: token.index * (intraLevelSpacing + nodeWidth),
            y: totalGraphHeight + tokenLevelSpacing,
            relativeX: token.index,
            label: token.form,
            type: "token",
            group: "token"
        }));

        for (let level = 0; level < nodesInFinalLevelsArray.length; level++) {
            nodesInFinalLevelsArray[level] = nodesInFinalLevelsArray[level].map(
                (node) => ({
                    ...node,
                    x: xPositions.get(node.id) * (intraLevelSpacing + nodeWidth),
                    y: totalGraphHeight - level * (totalGraphHeight / height),
                    relativeX: xPositions.get(node.id),
                    relativeY: level,
                    label: node.label,
                    type: "node",
                    nodeLevel: level,
                    group: "node",
                    span: false
                })
            );
        }

        const finalGraphNodes = nodesInFinalLevelsArray.flat().concat(tokens);

        const finalGraphEdges = graphClone.edges.map((edge, index) => {

            const sourceNodeIndex = finalGraphNodes.findIndex(
                (node) => node.id === edge.source
            );

            const targetNodeIndex = finalGraphNodes.findIndex(
                (node) => node.id === edge.target
            );

            const source = finalGraphNodes[sourceNodeIndex];
            const target = finalGraphNodes[targetNodeIndex];

            let cp;

            if (source.y === target.y) {
                cp = edgeRulesSameRow(
                    edge,
                    source,
                    target,
                    finalGraphNodes,
                    graphClone.edges,
                    graphLayoutSpacing
                );
            } else if (source.x === target.x) {
                cp = edgeRulesSameColumn(
                    edge,
                    source,
                    target,
                    finalGraphNodes,
                    graphClone.edges,
                    graphLayoutSpacing
                );
            } else {
                cp = edgeRulesOther(
                    edge,
                    source,
                    target,
                    finalGraphNodes,
                    graphClone.edges,
                    graphLayoutSpacing
                );
            }

            return {
                id: index,
                source: finalGraphNodes[sourceNodeIndex],
                target: finalGraphNodes[targetNodeIndex],
                label: edge.label,
                x1: cp.x1,
                y1: cp.y1,
                type: "link",
                group: "link"
            };
        });

        let tokenEdges = [tokens.size];
        let i = 0;
        for (let token of tokens) {
            if (lowestNode.get(token.relativeX) !== undefined) {
                let cp = controlPoints(
                    finalGraphNodes[
                        finalGraphNodes.findIndex(
                            (node) => node.id === lowestNode.get(token.relativeX)
                        )
                        ],
                    token,
                    "",
                    0, graphLayoutSpacing
                );
                let temp = {
                    source:
                        finalGraphNodes[
                            finalGraphNodes.findIndex(
                                (node) => node.id === lowestNode.get(token.relativeX)
                            )
                            ],
                    target: token,
                    type: "tokenLink",
                    label: "",
                    x1: cp.x1,
                    y1: cp.y1
                };
                tokenEdges[i] = temp;
                i++;
            }
        }

        const allEdges = finalGraphEdges.concat(tokenEdges);

        return {nodes: finalGraphNodes, links: allEdges};
    }
;

function controlPoints(source, target, direction, degree, graphLayoutSpacing) {

    const {nodeHeight, nodeWidth, interLevelSpacing, intraLevelSpacing, tokenLevelSpacing} = graphLayoutSpacing;

    let x1 = 0;
    let y1 = 0;

    if (direction === "vertical-left") {
        if (source.y < target.y) {
            x1 = Math.min(
                target.x - (source.y - target.y) * degree,
                target.x + intraLevelSpacing + nodeWidth - 25
            );
        } else {
            x1 = Math.max(
                target.x - (source.y - target.y) * degree,
                target.x - intraLevelSpacing - nodeWidth + 25
            );
        }
        y1 = (source.y + target.y) / 2;
    } else if (direction === "vertical-right") {
        if (source.y < target.y) {
            x1 = Math.max(
                target.x + (source.y - target.y) * degree,
                target.x - intraLevelSpacing - nodeWidth + 25
            );
        } else {
            x1 = Math.min(
                target.x + (source.y - target.y) * degree,
                target.x + intraLevelSpacing + nodeWidth - 25
            );
        }
        y1 = (source.y + target.y) / 2;
    } else if (direction === "horizontal-left") {
        x1 = (source.x + target.x) / 2;
        y1 = target.y + (source.x - target.x) * degree;
    } else if (direction === "horizontal-right") {
        x1 = (source.x + target.x) / 2;
        y1 = target.y - (source.x - target.x) * degree;
    } else if (direction === "custom") {
        x1 = degree;
        y1 = source.y + nodeHeight;
    } else {
        x1 = (source.x + target.x) / 2;
        y1 = (source.y + target.y) / 2;
    }

    return {x1, y1};
}

function edgeRulesSameColumn(
    edge,
    source,
    target,
    finalGraphNodes,
    finalGraphEdges,
    graphLayoutSpacing
) {
    const {nodeHeight, nodeWidth, interLevelSpacing, intraLevelSpacing, tokenLevelSpacing} = graphLayoutSpacing;

    let direction = "";
    let degree = 0.2;

    if (Math.abs(source.relativeY - target.relativeY) === 1) {
        //In the same column and 1 level apart
        //Is there an identical edge? If yes than 1 go left 1 go right, else straight line
        for (let e of finalGraphEdges) {
            if (source.id === e.from && target.id === e.to && edge !== e) {
                //There exists a duplicate edge
                if (edge.label.localeCompare(e.label) <= 0) {
                    direction = "vertical-right";
                } else {
                    direction = "vertical-left";
                }

                break;
            }
        }
    } else {
        //In the same column and more than level apart
        let found = false;
        for (let node of finalGraphNodes) {
            if (node.x === source.x) {
                if (node.y > source.y && node.y < target.y) {
                    //There exists a node inbetween the target and source node
                    //console.log(node);
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
                    if (source.x - neighbourNode.x === intraLevelSpacing + nodeWidth) {
                        left = true;
                    }
                }
            }

            degree = 0.4;
            if (left) {
                direction = "vertical-left";
            } else {
                direction = "vertical-right";
            }
        }
    }
    return controlPoints(source, target, direction, degree, graphLayoutSpacing);
}

function edgeRulesSameRow(
    edge,
    source,
    target,
    finalGraphNodes,
    finalGraphEdges,
    graphLayoutSpacing
) {

    const {nodeHeight, nodeWidth, interLevelSpacing, intraLevelSpacing, tokenLevelSpacing} = graphLayoutSpacing;

    let direction = "";
    let degree = 0.25;

    if (Math.abs(target.x - source.x) !== intraLevelSpacing + nodeWidth) {
        //On the same level and more than 1 space apart

        let found = false;
        for (let node of finalGraphNodes) {
            if (node.y === source.y) {
                if (
                    (node.x > source.x && node.x < target.x) ||
                    (node.x < source.x && node.x > target.x)
                ) {
                    //There exists a node in between the target and source node
                    //console.log(node);
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            direction = "horizontal-left";
        }
    }

    return controlPoints(source, target, direction, degree, graphLayoutSpacing);
}

function edgeRulesOther(
    edge,
    source,
    target,
    finalGraphNodes,
    finalGraphEdges,
    graphLayoutSpacing
) {

    const {nodeHeight, nodeWidth, interLevelSpacing, intraLevelSpacing, tokenLevelSpacing} = graphLayoutSpacing;

    let direction = "";
    let degree = 0.2;

    // //Is there a node protruding close to the line
    // let xProtrusion = 0.0;
    // let yProtrusion = 0.0;
    // for (let node of finalGraphNodes) {
    //   if (
    //     ((node.y < source.y && node.y > target.y) ||
    //       (node.y > source.y && node.y < target.y)) &&
    //     ((node.x < source.x && node.x > target.x) ||
    //       (node.x > source.x && node.x < target.x))
    //   ) {
    //     let xtemp = Math.abs(node.x - source.x);
    //     let ytemp = Math.abs(node.y - target.y);
    //     if (ytemp > yProtrusion && xtemp > xProtrusion) {
    //       xProtrusion = xtemp;
    //       yProtrusion = ytemp;
    //     }
    //   }
    // }

    // xProtrusion = xProtrusion / Math.abs(target.x - source.x);
    // yProtrusion = yProtrusion / Math.abs(target.y - source.y);
    // //console.log(source.label, target.label, xProtrusion, yProtrusion);
    // if (xProtrusion >= 0.5 && yProtrusion >= 0.5) {
    //   direction = "custom";
    //   degree = target.x;
    //   return controlPoints(source, target, direction, degree);
    // }

    //Is there an identical edge? If yes than 1 go left 1 go right, else straight line
    for (let e of finalGraphEdges) {
        if (source.id === e.from && target.id === e.to && edge !== e) {
            //There exists a duplicate edge
            if (edge.label.localeCompare(e.label) <= 0) {
                direction = "vertical-right";
            }

            break;
        }
    }

    return controlPoints(source, target, direction, degree, graphLayoutSpacing);
}
