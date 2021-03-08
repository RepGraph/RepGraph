import uuid from "react-uuid";
import {addTokenSpanText} from "./layoutUtils";

export const layoutFlat = (graphData, planar, graphLayoutSpacing, framework) => {

    const {nodeHeight, nodeWidth, interLevelSpacing, intraLevelSpacing, tokenLevelSpacing} = graphLayoutSpacing;


    const nodes = graphData.nodes.map((node, index) => ({
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

    addTokenSpanText(graphData);

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

    let finalGraphEdges = graphData.edges.map((edge, index) => {
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

    //Add top node and corresponding link to graphData
    if (addTopNode) {

        //Get top node's associated node
        const associatedNode = finalGraphNodes.find(node => node.id === graphData.tops);
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
                type: "tokenLink",
            });

        }
    }

    if (planar) {
        try {
            finalGraphEdges = finalGraphEdges.map((link) => ({
                ...link,
                group:
                    graphData.crossingEdges.includes(link.id)
                        ? "linkColourCross"
                        : link.group
            }));
        } catch (e) {
            console.log(e);
        }

    }

    return {nodes: finalGraphNodes, links: finalGraphEdges};
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