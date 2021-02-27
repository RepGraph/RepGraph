
export const layoutHierarchy = (graphData, graphLayoutSpacing) => {

    const nodeHeight = graphLayoutSpacing.nodeHeight;
    const nodeWidth = graphLayoutSpacing.nodeWidth;
    const interLevelSpacing = graphLayoutSpacing.interLevelSpacing;
    const intraLevelSpacing = graphLayoutSpacing.intraLevelSpacing;
    const tokenLevelSpacing = graphLayoutSpacing.tokenLevelSpacing;

    console.log(graphData);

    let children = new Map();
    let parents = new Map();

    //Assign empty neighbour arrays to each node id
    for (const node of graphData.nodes) {
        children.set(node.id, []);
        parents.set(node.id, []);
    }

    //Fill in children node id's and parent node ids for each node.
    for (const e of graphData.edges) {
        let temp = children.get(e.source);
        graphData.nodes.findIndex((node) => node.id === e.target);
        temp.push(
            graphData.nodes[graphData.nodes.findIndex((node) => node.id === e.target)]
        );
        children.set(e.source, temp);

        temp = parents.get(e.target);
        graphData.nodes.findIndex((node) => node.id === e.source);
        temp.push(
            graphData.nodes[graphData.nodes.findIndex((node) => node.id === e.source)]
        );
        parents.set(e.target, temp);
    }

    let nodesWithoutAnchors = []; //Array to keep track of nodes which originally had no anchors

    //Add anchors to nodes without anchors. Fake anchors are added in order to run the layout algorithm, and are decided using either a node's children or parent node's anchors.
    const nodesWithAnchorsAdded = graphData.nodes.map((node, i) => {
        if (node.anchors === null) {
            let anchorFrom, anchorEnd;
            if (children.get(node.id).length !== 0) {
                anchorFrom = children.get(node.id)[0].anchors[0].from;
                anchorEnd = children.get(node.id)[0].anchors[0].end;
            } else if (parents.get(node.id).length !== 0) {
                anchorFrom = parents.get(node.id)[0].anchors[0].from;
                anchorEnd = parents.get(node.id)[0].anchors[0].end;
            } else {
                // ??
            }
            let anchorArray = [];
            //let anchorValue = anchorFrom + (anchorFrom - anchorEnd) / 2;
            anchorArray.push({
                from: anchorFrom,
                end: anchorEnd
            });
            nodesWithoutAnchors.push(node.id);
            return {
                ...node,
                anchors: anchorArray,
                span: false
            };
        } else {
            return { ...node, span: true };
        }
    });
    //Determine span lengths of each node
    const graphNodeSpanLengths = nodesWithAnchorsAdded
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

    for (const level of uniqueSpanLengths) {
        let currentLevel = [];

        for (
            let spanIndex = 0;
            spanIndex < graphNodeSpanLengths.length;
            spanIndex++
        ) {
            if (graphNodeSpanLengths[spanIndex] === level) {
                currentLevel.push(nodesWithAnchorsAdded[spanIndex]);
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
    const levelTopology = new Array(graphData.tokens.length);
    levelTopology.fill(0);

    //console.log("levelTopology filled:", levelTopology);

    const minTokenIndex = Math.min(
        ...graphData.tokens.map((token) => token.index)
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
        anchors: nodesWithoutAnchors.includes(node.id) ? null : node.anchors, //Remove fake anchors assigned to anchorless nodes
        selected: false
    }));

    const tokens = graphData.tokens.map((token) => ({
        ...token,
        x: (token.index - minTokenIndex) * (nodeWidth + intraLevelSpacing),
        y: nodeSectionHeight + tokenLevelSpacing,
        label: token.form,
        type: "token",
        group: "token",
        selected: false
    }));

    //console.log("tokens", tokens);

    //console.log(newNodesInLevels.flat(2));

    const finalGraphNodes = nodes.concat(tokens);

    const finalGraphEdges = graphData.edges.map((edge, index) => {
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
                graphData.edges,
                graphLayoutSpacing
            );
        } else if (source.x === target.x) {
            cp = edgeRulesSameColumn(
                edge,
                source,
                target,
                finalGraphNodes,
                graphData.edges,
                graphLayoutSpacing
            );
        } else {
            cp = edgeRulesOther(
                edge,
                source,
                target,
                finalGraphNodes,
                graphData.edges,
                levelTopology,
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
            group: "link",
            selected: false
        };
    });

    console.log("layoutHierarchy return:", { nodes: finalGraphNodes, links: finalGraphEdges });
    return { nodes: finalGraphNodes, links: finalGraphEdges };
};

function controlPoints(source, target, direction, degree, graphLayoutSpacing) {

    const nodeHeight = graphLayoutSpacing.nodeHeight;
    const nodeWidth = graphLayoutSpacing.nodeWidth;
    const interLevelSpacing = graphLayoutSpacing.interLevelSpacing;
    const intraLevelSpacing = graphLayoutSpacing.intraLevelSpacing;
    const tokenLevelSpacing = graphLayoutSpacing.tokenLevelSpacing;

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
        y1 = Math.min(target.y + (source.x - target.x) * degree,target.y + tokenLevelSpacing);
    } else if (direction === "horizontal-right") {
        x1 = (source.x + target.x) / 2;
        y1 =  y1 = Math.min(target.y - (source.x - target.x) * degree,target.y + tokenLevelSpacing);;
    } else if (direction === "custom") {
        x1 = degree;
        y1 = source.y + nodeHeight;
    } else {
        x1 = (source.x + target.x) / 2;
        y1 = (source.y + target.y) / 2;
    }

    return { x1, y1 };
}

function edgeRulesSameColumn(
    edge,
    source,
    target,
    finalGraphNodes,
    finalGraphEdges,
    graphLayoutSpacing
) {

    const nodeHeight = graphLayoutSpacing.nodeHeight;
    const nodeWidth = graphLayoutSpacing.nodeWidth;
    const interLevelSpacing = graphLayoutSpacing.interLevelSpacing;
    const intraLevelSpacing = graphLayoutSpacing.intraLevelSpacing;
    const tokenLevelSpacing = graphLayoutSpacing.tokenLevelSpacing;

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
    const nodeHeight = graphLayoutSpacing.nodeHeight;
    const nodeWidth = graphLayoutSpacing.nodeWidth;
    const interLevelSpacing = graphLayoutSpacing.interLevelSpacing;
    const intraLevelSpacing = graphLayoutSpacing.intraLevelSpacing;
    const tokenLevelSpacing = graphLayoutSpacing.tokenLevelSpacing;

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

    if (Math.abs(source.x - target.x)/(intraLevelSpacing+nodeWidth) > 6){
        degree = 0.15;
    }

    return controlPoints(source, target, direction, degree, graphLayoutSpacing);
}

function edgeRulesOther(
    edge,
    source,
    target,
    finalGraphNodes,
    finalGraphEdges,
    levelTopology,
    graphLayoutSpacing
) {
    const nodeHeight = graphLayoutSpacing.nodeHeight;
    const nodeWidth = graphLayoutSpacing.nodeWidth;
    const interLevelSpacing = graphLayoutSpacing.interLevelSpacing;
    const intraLevelSpacing = graphLayoutSpacing.intraLevelSpacing;
    const tokenLevelSpacing = graphLayoutSpacing.tokenLevelSpacing;

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
        return controlPoints(source, target, direction, degree, graphLayoutSpacing);
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

    return controlPoints(source, target, direction, degree, graphLayoutSpacing);
}

//Can ultimately have this information placed within the returned graph data
export const determineAdjacentLinks = (graphData) => {
    let adjacentLinkMap = new Map();

    for (const node of graphData.nodes) {
        adjacentLinkMap.set(node.id, []);
    }

    for (const link of graphData.links) {
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
