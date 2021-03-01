// const nodeHeight = 40;
// const nodeWidth = 80;
// const intraLevelSpacing = 50;

import {forEach} from "react-bootstrap/ElementChildren";
import uuid from "react-uuid";

export const layoutFlat = (graphData, planar, graphLayoutSpacing) => {
    console.log(graphData);

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
            x1: cp.x1,
            y1: cp.y1,
            type: "link",
            group: "link",
            selected: false
        };
    })

    // //Add the top node link
    // if(!planar){
    //     let topNodeLinkID = graphData.edges.length;
    //     //Ensure that topNodeLinkID is unique
    //     if (graphData.edges.find(edge => edge.id === topNodeLinkID) !== undefined) {
    //         topNodeLinkID = uuid();
    //     }
    //
    //     let topCP = controlPoints(
    //         finalGraphNodes.find(node => node.id === topNodeID),
    //         finalGraphNodes.find(node => node.id === graphData.tops),
    //         "",
    //         0,
    //         graphLayoutSpacing
    //     );
    //
    //     //Add the top node link
    //     finalGraphEdges.push({
    //         id: topNodeLinkID,
    //         source: finalGraphNodes.find(node => node.id === topNodeID),
    //         target: finalGraphNodes.find(node => node.id === graphData.tops),
    //         label: "",
    //         x1: topCP.x1,
    //         y1: topCP.y1,
    //         type: "tokenLink",
    //     });
    // }

    if (planar) {
        // for (const indexElement of graphData.crossingEdges) {
        //     finalGraphEdges[indexElement] = {...finalGraphEdges[indexElement], group: "linkColourCross"}
        // }
        try {
            finalGraphEdges = finalGraphEdges.map((link) => ({
                ...link,
                group:
                    graphData.crossingEdges.includes(link.id)
                        ? "linkColourCross"
                        : link.group
            }));
        }catch (e) {
            console.log(e);
        }

    }

    return {nodes: finalGraphNodes, links: finalGraphEdges};
};

function controlPoints(source, target, direction, degree, graphLayoutSpacing) {
    const {nodeHeight, nodeWidth, interLevelSpacing, intraLevelSpacing, tokenLevelSpacing} = graphLayoutSpacing;

    let x1 = 0;
    let y1 = 0;

    if (direction === "horizontal-left") {
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

function edgeRulesSameRow(source, target, finalGraphNodes, planar, graphLayoutSpacing) {

    const {nodeHeight, nodeWidth, interLevelSpacing, intraLevelSpacing, tokenLevelSpacing} = graphLayoutSpacing;

    let direction = "";
    let degree = 0.25;

    if (Math.abs(target.x - source.x) !== intraLevelSpacing + nodeWidth) {
        //On the same level and more than 1 space apart

        let found = false;
        for (let node of finalGraphNodes) {
            if (node.y === source.y && ((node.x > source.x && node.x < target.x) ||
                (node.x < source.x && node.x > target.x))) {
                //There exists a node in between the target and source node
                //console.log(node);
                found = true;
                break;
            }
        }
        if (found) {
            if (planar) {
                if (source.x < target.x) {
                    direction = "horizontal-left";
                } else {
                    direction = "horizontal-right";
                }
            } else {
                direction = "horizontal-right";
            }
            let distance = Math.abs(source.x - target.x) / (intraLevelSpacing + nodeWidth);
            if (distance > 10 && !planar) {
                degree = 0.15;
            }
        }


    }


    return controlPoints(source, target, direction, degree, graphLayoutSpacing);
}