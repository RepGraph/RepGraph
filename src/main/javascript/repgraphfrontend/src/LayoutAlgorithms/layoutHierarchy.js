import lodash from "lodash";
import {edgeRulesSameColumn, edgeRulesSameRow, edgeRulesOther, setAnchors, controlPoints} from "./layoutUtils"
import uuid from "react-uuid";

export const layoutHierarchy = (graphData, graphLayoutSpacing, framework) => {

    console.log("graphData", graphData);

        const {nodeHeight, nodeWidth, interLevelSpacing, intraLevelSpacing, tokenLevelSpacing} = graphLayoutSpacing;

        let addTopNode = false;

        switch (framework) {
            case "1":
                addTopNode = true;
                break;
            case "2":
                addTopNode = true;
                break;
            case "3":
                break;
            case "4":
                break;
            case "5":
                break;
            default:
        }

        let graphClone = lodash.cloneDeep(graphData);

        // if (addTopNode) {
        //     graphClone.nodes.push({
        //         id: "top",
        //         label: "TOP",
        //         anchors: graphData.nodes.find(node => node.id === graphData.tops).anchors,
        //     })
        // }

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

    if(framework === "3" || framework === "5"){
        setAnchors(graphClone, children, parents, nodesWithoutAnchors);
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

//LevelTopology size mirrors the number of tokens there are
        const levelTopology = new Array(graphClone.tokens.length);
        levelTopology.fill(0);

        const minTokenIndex = Math.min(
            ...graphClone.tokens.map((token) => token.index)
        );

        const newNodesInLevels = [];
        for (const level of nodesInLevels) {
            let newLevel = [];
            for (const uniqueSpanArr of level) {
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
        }


        const levelTopologyMax = Math.max(...levelTopology);
        const nodeSectionHeight =
            levelTopologyMax * nodeHeight + (levelTopologyMax - 1) * interLevelSpacing;

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

        const tokens = graphClone.tokens.map((token) => ({
            ...token,
            x: (token.index - minTokenIndex) * (nodeWidth + intraLevelSpacing),
            y: nodeSectionHeight + tokenLevelSpacing,
            label: token.form,
            type: "token",
            group: "token"
        }));
        // //Add top node and corresponding link to graphData
        //
        // let topNodeID = graphData.nodes.length.toString();
        // //Ensure that topNodeID is unique
        // if (graphData.nodes.find(node => node.id === topNodeID) !== undefined) {
        //     topNodeID = uuid();
        // }
        //
        // //Get top node's x coordinate from its associated node
        // const topNodeX = nodes.find(node => node.id === graphData.tops).x;
        //
        // //Add the top node to the array of nodes
        // nodes.push({
        //     id: topNodeID,
        //     x: topNodeX,
        //     y: 0 - (nodeHeight + interLevelSpacing),
        //     type: "topNode",
        //     group: "top",
        //     label: "TOP",
        //     anchors: nodes.find(node => node.id === graphData.tops).anchors,
        //     span: false
        // });

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
                group: "link",
                selected: false
            };
        });

        //Add top node and corresponding link to graphData
        if(addTopNode){

            //Get top node's associated node
            const associatedNode = finalGraphNodes.find(node => node.id === graphData.tops);
            //console.log("associatedNode", associatedNode);

            if(associatedNode){
                //Add the top node to the array of nodes
                finalGraphNodes.push({
                    id: "TOP",
                    x: associatedNode.x,
                    y: 0 - (nodeHeight + interLevelSpacing),
                    type: "topNode",
                    group: "top",
                    label: "TOP",
                    anchors: associatedNode.anchors,
                    span: false
                });

                const addedTopNode = finalGraphNodes.find(node => node.id === "TOP")

                let topCP = controlPoints(
                    addedTopNode,
                    associatedNode,
                    "",
                    0,
                    graphLayoutSpacing
                );

                //Add the top node link
                finalGraphEdges.push({
                    id: "TOPLINK",
                    source: addedTopNode,
                    target: associatedNode,
                    label: "",
                    x1: topCP.x1,
                    y1: topCP.y1,
                    type: "tokenLink",
                });

            }
        }

    console.log("layoutHierarchy", {nodes: finalGraphNodes, links: finalGraphEdges});
        return {nodes: finalGraphNodes, links: finalGraphEdges};
    }
;

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
