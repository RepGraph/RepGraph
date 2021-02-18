import React, { useState } from "react";
import { Group } from "@visx/visx";

export const Link = ({
                         link,
                         selectMultiple,
                         dispatchSelectedLinks,
                         styles,
                         graphFormatCode,
                         tooltipData,
                         adjacentLinks,
                         tooltipOpen
                     }) => {
    const [highlighted, setHighlighted] = useState(false);
    const [selected, setSelected] = useState(false);

    const handleOnClick = (event) => {
        if (selectMultiple) {
            if (!selected) {
                dispatchSelectedLinks({ type: "add", id: link.id });
                setSelected(true);
            } else {
                dispatchSelectedLinks({ type: "remove", id: link.id });
                setSelected(false);
            }
        }
    };

    // const strokeColor = highlighted
    //   ? styles.linkStyles.hoverColour
    //   : selected
    //   ? styles.linkStyles.selectedColour
    //   : styles.linkStyles.linkColour;

    // const strokeColor =
    //   highlighted ||
    //   (adjacentLinks?.get(tooltipData.extraInformation.id)?.includes(link.id) &&
    //     tooltipOpen)
    //     ? styles.linkStyles.hoverColour
    //     : selected
    //     ? styles.linkStyles.selectedColour
    //     : styles.linkStyles.linkColour;

    let strokeColor = null;

    if (selected) {
        strokeColor = styles.linkStyles.selectedColour;
    } else if (highlighted) {
        strokeColor = styles.linkStyles.hoverColour;
    } else if (
        (graphFormatCode === "hierarchicalLongestPath" ||
            graphFormatCode === "treeLongestPath") &&
        link.longestPath === true
    ) {
        strokeColor = styles.longestPathStyles.linkColour;
    } else if (
        adjacentLinks?.get(tooltipData.extraInformation.id)?.includes(link.id) &&
        tooltipOpen
    ) {
        strokeColor = styles.linkStyles.hoverColour;
    } else {
        strokeColor = styles.linkStyles.linkColour;
    }

    return (
        <Group
            onMouseEnter={(event) => {
                setHighlighted(true);
            }}
            onMouseLeave={(event) => {
                setHighlighted(false);
            }}
            onClick={handleOnClick}
        >
            {EdgeLayout(link, strokeColor)}
        </Group>
    );
};

function EdgeLayout(link, strokeColor) {
    const t = 0.5;

    const x0 = link.source.x;
    const y0 = link.source.y;
    const x1 = link.x1;
    const y1 = link.y1;
    const x2 = link.x1;
    const y2 = link.y1;
    const x3 = link.target.x;
    const y3 = link.target.y;

    const xMid =
        Math.pow(1 - t, 3) * x0 +
        Math.pow(1 - t, 2) * 3 * t * x1 +
        (1 - t) * 3 * t * t * x2 +
        Math.pow(t, 3) * x3;
    const yMid =
        Math.pow(1 - t, 3) * y0 +
        Math.pow(1 - t, 2) * 3 * t * y1 +
        (1 - t) * 3 * t * t * y2 +
        Math.pow(t, 3) * y3;

    let labelOffset = -20;

    if (
        link.source.relativeY === link.target.relativeY &&
        link.source.relativeY === 0
    ) {
        labelOffset = 20;
    }

    if (link.type === "tokenLink") {
        return (
            <path
                id={`edge${link.id}`}
                d={`M ${link.source.x} ${link.source.y} C  ${link.x1} ${link.y1} ${link.x1} ${link.y1} ${link.target.x} ${link.target.y}`}
                stroke={strokeColor}
                strokeWidth="2"
                fill="none"
                strokeDasharray="10,10"
            />
        );
    } else {
        return (
            <Group>
                <path
                    id={`edge${link.id}`}
                    d={`M ${link.source.x} ${link.source.y} C  ${link.x1} ${link.y1} ${link.x1} ${link.y1} ${link.target.x} ${link.target.y}`}
                    stroke={strokeColor}
                    strokeWidth="2"
                    fill="none"
                />
                <text textAnchor="middle" dy="-1.5px">
                    <textPath
                        href={`#edge${link.id}`}
                        startOffset={`50%`}
                        side="left"
                        stroke={strokeColor}
                        fill={strokeColor}
                        fontSize="25px"
                        dominantBaseline="central"
                    >
                        ➤
                    </textPath>
                </text>
                <text
                    textAnchor="middle"
                    fill={strokeColor}
                    x={xMid}
                    y={yMid}
                    dy={`${labelOffset}px`}
                    fontWeight="bold"
                >
                    {link.label}
                </text>
            </Group>
        );
    }
}

export default Link;
