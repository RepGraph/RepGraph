import uuid from "react-uuid";
import {addTokenSpanText, createDummyNodes} from "./layoutUtils";
import lodash from "lodash";

export const layoutFlat = (graphData, planar, graphLayoutSpacing, framework) => {

    const {nodeHeight, nodeWidth, interLevelSpacing, intraLevelSpacing, tokenLevelSpacing} = graphLayoutSpacing;

    let graphClone = lodash.cloneDeep(graphData);


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

    let nodeMap = new Map();
    for (let node of graphClone.nodes) {
        nodeMap.set(node.id, node);
    }
    graphClone.nodes = nodeMap;
    if (!planar) {
        graphClone = createDummyNodes(graphClone, parents, children, false)

        let nodesNoAnchors = Array.from(graphClone.nodes.values()).filter(function (node) {
            return node.anchors === null
        });

        let nodesWithAnchors = Array.from(graphClone.nodes.values()).filter(function (node) {
            return node.anchors !== null
        });

        function compareAnchors(node1, node2) {
            if (node1.anchors[0].from === node2.anchors[0].from) {
                if (node1.anchors[0].end < node2.anchors.end) {
                    return -1;
                } else if (node1.anchors[0].end > node2.anchors.end) {
                    return 1;
                } else {
                    return 0;
                }
            }
            if (node1.anchors[0].from < node2.anchors[0].from) {
                return -1;
            } else {
                return 1;
            }
        }

        nodesWithAnchors.sort(compareAnchors);

        graphClone.nodes = nodesWithAnchors.concat(nodesNoAnchors);

    }


    const nodes = Array.from(graphClone.nodes.values()).map((node, index) => ({
        ...node,
        x: index * (nodeWidth + intraLevelSpacing),
        y: nodeHeight,
        label: node.label,
        type: "node",
        group: "node",
        span: false

    }));


    let addTopNode = false;

    switch (framework) {
        case "1":
            addTopNode = true;
            break;
        case "2":
            addTopNode = true;
            break;
        case "3":
            addTopNode = true;
            break;
        case "4":
            addTopNode = true;
            break;
        case "5":
            addTopNode = true;
            break;
        default:
    }

    addTokenSpanText(graphClone);

    let finalGraphNodes = nodes;

    if (planar) {
        let count = 0;
        let lastCount = -1;
        let height = 0;
        for (let i = 0; i < finalGraphNodes.length - 1; i++) {
            if (lastCount === count) {
                height += interLevelSpacing;
            } else {
                height = 0;
            }
            finalGraphNodes[i] = {
                ...finalGraphNodes[i],
                x: count * (nodeWidth + intraLevelSpacing),
                y: nodeHeight + height,
                label: finalGraphNodes[i].label,
                type: "node",
                group: "node",
                span: false
            }
            lastCount = count;
            if (!(finalGraphNodes[i].anchors[0].from === finalGraphNodes[i + 1].anchors[0].from && finalGraphNodes[i].anchors[0].end === finalGraphNodes[i + 1].anchors[0].end)) {
                count += 1
            }
        }
        if (lastCount === count) {
            height += interLevelSpacing;
        } else {
            height = 0;
        }
        const lastIndex = finalGraphNodes.length - 1
        finalGraphNodes[lastIndex] = {
            ...finalGraphNodes[lastIndex],
            x: count * (nodeWidth + intraLevelSpacing),
            y: nodeHeight + height,
            label: finalGraphNodes[lastIndex].label,
            type: "node",
            group: "node",
            span: false,
            selected: false
        }

    }

    let finalGraphEdges = graphClone.edges.map((edge, index) => {
        const sourceNodeIndex = finalGraphNodes.findIndex(
            (node) => node.id === edge.source
        );
        const targetNodeIndex = finalGraphNodes.findIndex(
            (node) => node.id === edge.target
        );

        const source = finalGraphNodes[sourceNodeIndex];
        const target = finalGraphNodes[targetNodeIndex];

        let cp;

        cp = edgeRulesSameRow(source, target, finalGraphNodes, planar, graphLayoutSpacing);

        return {
            id: index,
            source: finalGraphNodes[sourceNodeIndex],
            target: finalGraphNodes[targetNodeIndex],
            label: edge.label,
            labelOffsetX: cp.offsetX,
            labelOffsetY: cp.offsetY,
            x1: cp.x1,
            y1: cp.y1,
            type: "link",
            group: "link",
            selected: false
        };
    })

    //Add top node and corresponding link to graphClone
    if (addTopNode) {

        //Get top node's associated node
        const associatedNode = finalGraphNodes.find(node => node.id === graphClone.tops);
        //console.log("associatedNode", associatedNode);

        if (associatedNode) {
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
                type: "topLink",
            });

        }
    }

    if (planar) {
        try {
            finalGraphNodes = finalGraphNodes.map((node) => ({
                ...node,
                dummy: node.label.includes("Dummy Span") ? true : false
            }))
            finalGraphEdges = finalGraphEdges.map((link) => ({
                ...link,
                group:
                    graphClone.crossingEdges.includes(link.id)
                        ? "linkColourCross"
                        : link.group
            }));
        } catch (e) {
            console.log(e);
        }

    }
    const gwidth = (graphLayoutSpacing.nodeWidth+graphLayoutSpacing.intraLevelSpacing)*nodes.length;
    const gheight = graphLayoutSpacing.nodeHeight*4;
    return {nodes: finalGraphNodes, links: finalGraphEdges,graphHeight :gheight,graphWidth : gwidth };
};

function controlPoints(source, target, direction, degree, graphLayoutSpacing) {
    const {nodeHeight, nodeWidth, interLevelSpacing, intraLevelSpacing, tokenLevelSpacing} = graphLayoutSpacing;

    let x1 = 0;
    let y1 = 0;
    let offsetX = 0;
    let offsetY = 0;

    if (direction === "horizontal-left") {
        x1 = (source.x + target.x) / 2;
        y1 = target.y + (source.x - target.x) * degree;
        offsetY = source.x < target.x ? -20 : 20;
    } else if (direction === "horizontal-right") {
        x1 = (source.x + target.x) / 2;
        y1 = target.y - (source.x - target.x) * degree;
        offsetY = source.x < target.x ? 20 : -20;
    } else {
        x1 = (source.x + target.x) / 2;
        y1 = (source.y + target.y) / 2;
    }

    return {x1, y1, offsetX, offsetY};
}

function edgeRulesSameRow(source, target, finalGraphNodes, planar, graphLayoutSpacing) {

    const {nodeHeight, nodeWidth, interLevelSpacing, intraLevelSpacing, tokenLevelSpacing} = graphLayoutSpacing;

    let direction = "horizontal-left";
    let degree = 0.25;

    if (planar) {
        if (source.x < target.x) {
            direction = "horizontal-left";
        } else {
            direction = "horizontal-right";
        }
    }
    let distance = Math.abs(source.x - target.x) / (intraLevelSpacing + nodeWidth);
    if (distance > 10 && !planar) {
        degree = 0.15;
    }

    return controlPoints(source, target, direction, degree, graphLayoutSpacing);
}