import React, {useContext} from "react";
import { useState } from "react";
import { Group } from "@visx/visx";
import {AppContext} from "../../Store/AppContextProvider";

export const Node = ({
                         node,
                         handleMouseOver,
                         hideTooltip,
                         setTooltipData,
                         styles,
                         graphFormatCode,
                         events
                     }) => {
    const [highlighted, setHighlighted] = useState(false);
    const {state, dispatch} = useContext(AppContext); //Provide access to global state

    let label = (
        <text
            textAnchor="middle"
            alignmentBaseline="baseline"
            fill={node.type === "token" ? styles.tokenStyles.labelColour : styles.nodeStyles.labelColour}
            fontWeight="bold"
            // transform="translate(0,45)"
        >
            {node.label.length > 10
                ? node.label.substring(0, 7).concat("...")
                : node.label}
        </text>
    );

    //console.log(node);

    let bb = {
        x: 0,
        y: 0,
        width: state.graphLayoutSpacing.nodeWidth,
        height: state.graphLayoutSpacing.nodeHeight
    };

    const margin = 5;
    bb.x -= bb.width / 2;
    bb.y -= bb.height / 2 + margin;

    let fillColor = null;

    switch (node.type) {
        case "node":
            if (node.selected && events && events.hasOwnProperty("select")) {
                fillColor = styles.nodeStyles.selectedColour;
            } else if (highlighted) {
                fillColor = styles.nodeStyles.hoverColour;
            } else if (
                (graphFormatCode === "hierarchicalLongestPath" ||
                    graphFormatCode === "treeLongestPath"||
                    graphFormatCode === "flatLongestPath") &&
                node.group === "longestPath"
            ) {
                fillColor = styles.longestPathStyles.nodeColour;
            } else if (graphFormatCode === "hierarchicalCompare" || graphFormatCode === "treeCompare"|| graphFormatCode === "flatCompare"){

                switch (node.group) {
                    case "similar":
                        fillColor = styles.compareStyles.nodeColourSimilar;
                        break;
                    case "dissimilar":
                        fillColor = styles.compareStyles.nodeColourDissimilar;
                        break;
                    default:
                        fillColor = styles.nodeStyles.nodeColour;
                        break;
                }

            }
            else {
                fillColor = styles.nodeStyles.nodeColour;
            }
            break;
        case "token":
            if (node.selected && events && events.hasOwnProperty("select")) {
                fillColor = styles.tokenStyles.selectedColour;
            } else if (highlighted) {
                fillColor = styles.tokenStyles.hoverColour;
            } else if (
                (graphFormatCode === "hierarchicalLongestPath" ||
                    graphFormatCode === "treeLongestPath"||
                    graphFormatCode === "flatLongestPath") &&
                node.group === "longestPath"
            ) {
                fillColor = styles.longestPathStyles.tokenColour;
            } else {
                fillColor = styles.tokenStyles.tokenColour;
            }
            break;
        default:
            fillColor = "black"; //dunno about this
    }

    const outline = (
        <rect
            x={bb.x}
            y={bb.y}
            rx={8}
            ry={8}
            width={bb.width}
            height={bb.height}
            className="labeloutline"
            fill={fillColor}
        ></rect>
    );

    // const outline = <circle r={25} fill={fillColor} opacity={1} />;

    let span = null;
    if (node.type !== "token" && node.span === true) {
        const fromX =
            ((node.anchors[0].from - node.anchors[0].end) / 2) * 130 - bb.width / 2;

        const fromY = bb.y + bb.height + 10;

        const toX =
            (((node.anchors[0].end - node.anchors[0].from) * 1.0) / 2) * 130 +
            bb.width / 2;
        const toY = bb.y + bb.height + 10;

        span = (
            <polyline
                points={`${fromX},${fromY + 5} ${fromX},${fromY} ${toX},${toY} ${toX},${
                    toY + 5
                }`}
                fill="none"
                strokeWidth="4"
                stroke={styles.nodeStyles.spanColour}
                strokeLinecap="round"
                strokeLinejoin="round"
            />
        );
    }

    const handleOnClick = (event) => {

        if(events && events.hasOwnProperty('select')){

            if(events.select === "subset" && node.type === "node"){

                try {

                    const newNodes = state.selectedSentenceVisualisation.nodes.map(oldNode => ({
                        ...oldNode,
                        selected: oldNode.type === "node" ? (oldNode.id === node.id ? true: false) : false
                    }));

                    dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: {...state.selectedSentenceVisualisation , nodes: newNodes} }});

                }catch (e) {
                    console.log(e);
                }

            }else if(events.select === "subgraph" && node.type === "node"){

                try {

                const newNodes = state.selectedSentenceVisualisation.nodes.map(oldNode => ({
                    ...oldNode,
                    selected: oldNode.type === "node" ? (oldNode.id === node.id ? true: oldNode.selected) : false
                }));

                dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: {...state.selectedSentenceVisualisation , nodes: newNodes} }});
                }catch (e) {
                    console.log(e);
                }
            }

        }
    };

    let notAllowed = [
        "x",
        "y",
        "type",
        "group",
        "label",
        "relativeX",
        "relativeY",
        "nodeLevel",
        "span"
    ]; //Extra information object keys to be excluded from tooltip

    //Remove id property from tree-like token tooltip
    if (node.type === "token") {
        notAllowed = notAllowed.concat("id");
    }

    const filteredExtraInformation = Object.keys(node)
        .filter((key) => !notAllowed.includes(key))
        .reduce((obj, key) => {
            obj[key] = node[key];
            return obj;
        }, {});

    return (
        <g>
            <Group
                onMouseEnter={(event) => {
                    //console.log(node.label);
                    setHighlighted(true);
                    setTooltipData({
                        label: node.label,
                        type: node.type,
                        extraInformation: filteredExtraInformation
                    });
                }}
                onMouseLeave={(event) => {
                    //console.log(node.label);
                    setHighlighted(false);
                }}
                onMouseOver={handleMouseOver}
                onMouseOut={hideTooltip}
                onClick={handleOnClick}
                top={node.y}
                left={node.x}
            >
                {span}
                {outline}
                {label}
            </Group>
        </g>
    );
};

export default Node;
