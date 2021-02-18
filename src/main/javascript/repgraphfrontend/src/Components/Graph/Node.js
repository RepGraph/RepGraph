import React from "react";
import { useState } from "react";
import { Group } from "@visx/visx";

export const Node = ({
                         node,
                         handleMouseOver,
                         hideTooltip,
                         setTooltipData,
                         selectMultiple,
                         dispatchSelectedNodes,
                         styles,
                         graphFormatCode
                     }) => {
    const [highlighted, setHighlighted] = useState(false);
    const [selected, setSelected] = useState(false);
    let label = (
        <text
            textAnchor="middle"
            alignmentBaseline="baseline"
            fill={node.group === "token" ? "black" : "white"}
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
        width: 80,
        height: 40
    };

    const margin = 5;
    bb.x -= bb.width / 2;
    bb.y -= bb.height / 2 + margin;

    // const fillColor = highlighted
    //   ? node.group === "node"
    //     ? styles.nodeStyles.hoverColour
    //     : styles.tokenStyles.hoverColour
    //   : selected
    //   ? styles.nodeStyles.selectedColour
    //   : node.group === "token"
    //   ? styles.tokenStyles.tokenColour
    //   : styles.nodeStyles.nodeColour;

    let fillColor = null;

    switch (node.group) {
        case "node":
            if (selected) {
                fillColor = styles.nodeStyles.selectedColour;
            } else if (highlighted) {
                fillColor = styles.nodeStyles.hoverColour;
            } else if (
                (graphFormatCode === "hierarchicalLongestPath" ||
                    graphFormatCode === "treeLongestPath") &&
                node.longestPath === true
            ) {
                fillColor = styles.longestPathStyles.nodeColour;
            } else {
                fillColor = styles.nodeStyles.nodeColour;
            }
            break;
        case "token":
            if (selected) {
                fillColor = styles.tokenStyles.selectedColour;
            } else if (highlighted) {
                fillColor = styles.tokenStyles.hoverColour;
            } else if (
                (graphFormatCode === "hierarchicalLongestPath" ||
                    graphFormatCode === "treeLongestPath") &&
                node.longestPath === true
            ) {
                fillColor = styles.longestPathStyles.tokenColour;
            } else {
                fillColor = styles.tokenStyles.tokenColour;
            }
            break;
        default:
            fillColor = "black"; //dunno about this
    }

    // const fillColor = highlighted
    //   ? node.group === "node"
    //     ? styles.standardStyles.nodeStyles.hoverColour
    //     : styles.standardStyles.tokenStyles.hoverColour
    //   : selected
    //   ? styles.standardStyles.nodeStyles.selectedColour
    //   : node.group === "token"
    //   ? styles.standardStyles.tokenStyles.tokenColour
    //   : styles.standardStyles.nodeStyles.nodeColour;

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
    if (node.group !== "token" && node.span === true) {
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
        if (selectMultiple) {
            console.log(node.id);
            if (!selected) {
                dispatchSelectedNodes({ type: "add", id: node.id });
                setSelected(true);
            } else {
                dispatchSelectedNodes({ type: "remove", id: node.id });
                setSelected(false);
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
    if (node.group === "token") {
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
                        group: node.group,
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
