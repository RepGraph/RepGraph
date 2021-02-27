import lodash from "lodash";
import {childrenAnchors, getPath} from "./layoutUtils"




const nodeHeight = 40;
const nodeWidth = 80;
const interLevelSpacing = 80;
const intraLevelSpacing = 50;
const tokenLevelSpacing = 140;

export const layoutHierarchy = (graphData) => {

        let graphClone = lodash.cloneDeep(graphData);
        console.log("graphClone", graphClone);

        let children = new Map();
        let parents = new Map();

        //Assign empty neighbour arrays to each node id
        for (const node of graphClone.nodes) {
            children.set(node.id, []);
            parents.set(node.id, []);
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

        //graphClone = createDummyNodes(graphClone, parents, children);


        let nodeMap = new Map();
        for (let node of graphClone.nodes) {
            nodeMap.set(node.id, node);
        }
        graphClone.nodes = nodeMap;

        let nodesWithoutAnchors = []; //Array to keep track of nodes which originally had no anchors

        let topological = getPath(graphClone, children);

        console.log("topological", topological)
        // let topological = topologicalSort(graphClone.nodes[graphClone.nodes.findIndex((node) => node.id === graphClone.tops)], children, visited, stack);
        // console.log("topological", topological);
        for (let nodeID of topological) {
            let node = graphClone.nodes.get(nodeID)

            if (node.anchors === null) {
                console.log("processing node", node.id)
                let vis = {};
                for (let node of graphClone.nodes.values()) {
                    vis[node.id] = false;
                }
                nodesWithoutAnchors.push(node.id);
                let anch = [];
                anch.push(childrenAnchors(node, children, vis, graphClone));
                console.log("ANCH 0 ", anch[0])
                if (anch[0].from === Number.MAX_VALUE) {
                    for (let parentID of parents.get(node.id)) {
                        console.log("parentID", parentID)
                        if (graphClone.nodes.get(parentID).anchors !== null) {
                            console.log("INSIDE", parentID)
                            console.log("ANCHS.from", anch[0].from)
                            console.log("GRAPH", graphClone)
                            console.log("PARENTS.from", graphClone.nodes.get(parentID).anchors[0].from)
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


//Determine span lengths of each node
        const graphNodeSpanLengths = Array.from(graphClone.nodes.values())
            .map((node) => node.anchors[0])
            .map((span) => span.end - span.from);

//Determine unique span lengths of all the node spans
        let uniqueSpanLengths = [];
        const map = new Map();
        for (const item of graphNodeSpanLengths) {
            if (!map.has(item)) {
                map.set(item, true); // set any value to Map
                uniqueSpanLengths.push(item);
            }
        }
//console.log("uniqueSpanLengths", uniqueSpanLengths);
        uniqueSpanLengths.sort((a, b) => a - b); //sort unique spans ascending

//Sort the nodes into each level based on their spans
        let nodesInLevels = [];
        let nodeArray = Array.from(graphClone.nodes.values());

        for (const level of uniqueSpanLengths) {
            let currentLevel = [];

            for (
                let spanIndex = 0;
                spanIndex < graphNodeSpanLengths.length;
                spanIndex++
            ) {
                if (graphNodeSpanLengths[spanIndex] === level) {
                    currentLevel.push(nodeArray[spanIndex]);
                }
            }
            nodesInLevels.push(currentLevel);
        }

//Find the nodes in each level with the same span and group them together
//Find the unique spans in each level
        let uniqueSpansInLevels = [];
        for (let level of nodesInLevels) {
            let uniqueSpans = []; //Stores the "stringified" objects
            const spanMap = new Map();
            for (const node of level) {
                if (!spanMap.has(JSON.stringify(node.anchors))) {
                    spanMap.set(JSON.stringify(node.anchors), true); // set any value to Map
                    uniqueSpans.push(JSON.stringify(node.anchors));
                }
            }
            uniqueSpansInLevels.push(uniqueSpans);
            //console.log("uniqueSpans", uniqueSpans);
        }

//Iterate through the unique spans in each level and group the same ones together
        for (let level = 0; level < nodesInLevels.length; level++) {
            let newLevelOfGroups = [];
            for (let uniqueSpan of uniqueSpansInLevels[level]) {
                //find the nodes in the level that have the same span and group them together
                let nodesWithCurrentSpan = nodesInLevels[level].filter(
                    (node) => JSON.stringify(node.anchors) === uniqueSpan
                );
                newLevelOfGroups.push(nodesWithCurrentSpan);
            }
            nodesInLevels[level] = newLevelOfGroups;
        }

//console.log("nodesInLevels", nodesInLevels);

//LevelTopology size mirrors the number of tokens there are
        const levelTopology = new Array(graphClone.tokens.length);
        levelTopology.fill(0);

//console.log("levelTopology filled:", levelTopology);

        const minTokenIndex = Math.min(
            ...graphClone.tokens.map((token) => token.index)
        );

//console.log("minTokenIndex", minTokenIndex);

        const newNodesInLevels = [];
        for (const level of nodesInLevels) {
            let newLevel = [];
            //console.log("level", level);
            for (const uniqueSpanArr of level) {
                //console.log("uniqueSpanArr", uniqueSpanArr);
                const newUniqueSpanArr = uniqueSpanArr.map((node, i) => {
                    // console.log(
                    //     "slice",
                    //     "from",
                    //     node.anchors[0].from - minTokenIndex,
                    //     "to",
                    //     node.anchors[0].end - minTokenIndex + 1,
                    //     "...",
                    //     ...levelTopology.slice(
                    //         node.anchors[0].from - minTokenIndex,
                    //         node.anchors[0].end - minTokenIndex + 1
                    //     )
                    // );

                    return {
                        ...node,
                        x: node.anchors[0].from - minTokenIndex,
                        y:
                            Math.max(
                                ...levelTopology.slice(
                                    node.anchors[0].from - minTokenIndex,
                                    node.anchors[0].end - minTokenIndex + 1
                                )
                            ) + i,
                        relativeX: node.anchors[0].from - minTokenIndex,
                        relativeY:
                            Math.max(
                                ...levelTopology.slice(
                                    node.anchors[0].from - minTokenIndex,
                                    node.anchors[0].end - minTokenIndex + 1
                                )
                            ) + i
                    };
                });
                newLevel.push(newUniqueSpanArr);

                for (
                    let index = uniqueSpanArr[0].anchors[0].from - minTokenIndex;
                    index < uniqueSpanArr[0].anchors[0].end - minTokenIndex + 1;
                    index++
                ) {
                    levelTopology[index] += uniqueSpanArr.length;
                }
            }
            newNodesInLevels.push(newLevel);
            //console.log("levelTopology:", levelTopology);
        }

//console.log("levelTopology", levelTopology);

        const levelTopologyMax = Math.max(...levelTopology);
//console.log("levelTopologyMax", levelTopology);
        const nodeSectionHeight =
            levelTopologyMax * nodeHeight + (levelTopologyMax - 1) * interLevelSpacing;
//console.log("nodeSectionHeight", nodeSectionHeight);

//console.log("levelTopology max", Math.max(...levelTopology));
//console.log("newNodesInLevels", newNodesInLevels);

        const nodes = newNodesInLevels.flat(2).map((node) => ({
            ...node,
            x:
                (node.x + (node.anchors[0].end - node.anchors[0].from) / 2) *
                (nodeWidth + intraLevelSpacing),
            y: (levelTopologyMax - node.y - 1) * (nodeHeight + interLevelSpacing),
            type: "node",
            group: "node",
            label: node.label,
            anchors: nodesWithoutAnchors.includes(node.id) ? null : node.anchors //Remove fake anchors assigned to anchorless nodes
        }));

        const tokens = graphClone.tokens.map((token) => ({
            ...token,
            x: (token.index - minTokenIndex) * (nodeWidth + intraLevelSpacing),
            y: nodeSectionHeight + tokenLevelSpacing,
            label: token.form,
            type: "token",
            group: "token"
        }));

//console.log("tokens", tokens);

//console.log(newNodesInLevels.flat(2));

        const finalGraphNodes = nodes.concat(tokens);

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
                    graphClone.edges
                );
            } else if (source.x === target.x) {
                cp = edgeRulesSameColumn(
                    edge,
                    source,
                    target,
                    finalGraphNodes,
                    graphClone.edges
                );
            } else {
                cp = edgeRulesOther(
                    edge,
                    source,
                    target,
                    finalGraphNodes,
                    graphClone.edges,
                    levelTopology
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


        console.log("layoutHierarchy return:", {nodes: finalGraphNodes, links: finalGraphEdges});
        return {nodes: finalGraphNodes, links: finalGraphEdges};
    }
;

function controlPoints(source, target, direction, degree) {
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
        y1 = Math.min(target.y + (source.x - target.x) * degree, target.y + tokenLevelSpacing);
    } else if (direction === "horizontal-right") {
        x1 = (source.x + target.x) / 2;
        y1 = y1 = Math.min(target.y - (source.x - target.x) * degree, target.y + tokenLevelSpacing);
        ;
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
    finalGraphEdges
) {
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
                if (
                    (node.y > source.y && node.y < target.y) ||
                    (node.y < source.y && node.y > target.y)
                ) {
                    //There exists a node inbetween the target and source node
                    // console.log(node);
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
                        nodeWidth + intraLevelSpacing
                    ) {
                        left = true;
                    }
                }
            }

            //Make the edge go to absolute left, need to check direction of edge.
            if (target.y < source.y) {
                degree = 0.4;
                if (left) {
                    direction = "vertical-right";
                } else {
                    direction = "vertical-left";
                }
            } else {
                degree = 0.4;
                if (left) {
                    direction = "vertical-left";
                } else {
                    direction = "vertical-right";
                }
            }
        }
    }
    return controlPoints(source, target, direction, degree);
}

function edgeRulesSameRow(
    edge,
    source,
    target,
    finalGraphNodes,
    finalGraphEdges
) {
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
            if (source.x < target.x) {
                direction = "horizontal-right";
            } else {
                direction = "horizontal-left";
            }
        }
    }

    if (Math.abs(source.x - target.x) / (intraLevelSpacing + nodeWidth) > 6) {
        degree = 0.15;
    }

    return controlPoints(source, target, direction, degree);
}

function edgeRulesOther(
    edge,
    source,
    target,
    finalGraphNodes,
    finalGraphEdges,
    levelTopology
) {
    let direction = "";
    let degree = 0.2;

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
    xProtrusion = xProtrusion / Math.abs(target.x - source.x);
    yProtrusion = yProtrusion / Math.abs(target.y - source.y);
    //console.log(source.label, target.label, xProtrusion, yProtrusion);
    if (xProtrusion >= 0.5 && yProtrusion >= 0.5) {
        direction = "custom";
        degree = target.x;
        return controlPoints(source, target, direction, degree);
    }
    // let maxColInRange = 0;

    // const newSourceRelX = Math.floor(
    //   (source.anchors[0].end + source.anchors[0].from) / 2
    // );
    // const newTargetRelX = Math.floor(
    //   (target.anchors[0].end + target.anchors[0].from) / 2
    // );

    // console.log(source.label, " new Rel X: ", newSourceRelX);
    // console.log(target.label, " new Rel X: ", newTargetRelX);

    // if (newTargetRelX < newSourceRelX) {
    //   console.log(Math.max(...levelTopology.slice(newTargetRelX, newSourceRelX)));
    // }

    // if (
    //   (target.anchors[0].from + target.anchors[0].end) / 2 <
    //   (source.anchors[0].from + source.anchors[0].end) / 2
    // ) {
    //   maxColInRange = Math.max(
    //     ...levelTopology.slice(
    //       (target.anchors[0].from + target.anchors[0].end) / 2 + 1,
    //       (source.anchors[0].from + source.anchors[0].end) / 2
    //     )
    //   );
    // } else {
    //   maxColInRange = Math.max(
    //     ...levelTopology.slice(
    //       (source.anchors[0].from + source.anchors[0].end) / 2 + 1,
    //       (target.anchors[0].from + target.anchors[0].end) / 2
    //     )
    //   );
    // }

    // console.log("maxColInRange", maxColInRange, source.label,target.label);

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

    //Is the source node inbetween two column (due to its span) and is the target node within less of 1 columns distance from it? If so, check for a node below the source node.
    if (Math.abs(target.x - source.x) < intraLevelSpacing + nodeWidth) {
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

    return controlPoints(source, target, direction, degree);
}

//Can ultimately have this information placed within the returned graph data
export const determineAdjacentLinks = (graphClone) => {
    let adjacentLinkMap = new Map();

    for (const node of graphClone.nodes) {
        adjacentLinkMap.set(node.id, []);
    }

    for (const link of graphClone.links) {
        if (link.type === "link") {
            adjacentLinkMap.set(
                link.source.id,
                adjacentLinkMap.get(link.source.id).concat(link.id)
            );
            adjacentLinkMap.set(
                link.target.id,
                adjacentLinkMap.get(link.target.id).concat(link.id)
            );
        }
    }

    return adjacentLinkMap;
};
