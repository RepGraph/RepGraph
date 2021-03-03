import React, {useContext, useState} from "react";
import {Group} from "@visx/visx";
import uuid from "react-uuid";
import {AppContext} from "../../Store/AppContextProvider";

export const Link = ({
                         link,
                         graphFormatCode,
                         tooltipData,
                         adjacentLinks,
                         tooltipOpen,
                         events
                     }) => {
    const [highlighted, setHighlighted] = useState(false);
    const {state, dispatch} = useContext(AppContext); //Provide access to global state

    const styles = state.graphStyles; //Use AppContext styles

    const handleOnClick = (event) => {

        if (events && events.hasOwnProperty('select')) {

            if (events.select === "subset") {
                //nothing to do?
            } else if (events.select === "subgraph") {

                try {
                    const newLinks = state.selectedSentenceVisualisation.links.map(oldLink => ({
                        ...oldLink,
                        selected: oldLink.type === "link" ? oldLink.id === link.id ? !oldLink.selected: oldLink.selected : oldLink.selected
                    }));

                    dispatch({type: "SET_SENTENCE_VISUALISATION",
                        payload: {
                            selectedSentenceVisualisation: {
                                ...state.selectedSentenceVisualisation,
                                links: newLinks
                            }
                        }
                    });
                } catch (e) {
                    console.log(e);
                }
            }
        }
    };

    let strokeColor = null;

    if (link.selected && events && events.hasOwnProperty("select")) {
        strokeColor = styles.linkStyles.selectedColour;
    } else if (highlighted) {
        strokeColor = styles.linkStyles.hoverColour;
    } else if (
        (graphFormatCode === "hierarchicalLongestPath" ||
            graphFormatCode === "treeLongestPath" ||
            graphFormatCode === "flatLongestPath") &&
        link.group === "longestPath"
    ) {
        strokeColor = styles.longestPathStyles.linkColour;
    } else if (
        adjacentLinks?.get(tooltipData.extraInformation.id)?.includes(link.id) &&
        tooltipOpen
    ) {
        strokeColor = styles.linkStyles.hoverColour;
    } else if (graphFormatCode === "hierarchicalCompare" || graphFormatCode === "treeCompare" || graphFormatCode === "flatCompare") {

        switch (link.group) {
            case "similar":
                strokeColor = styles.compareStyles.linkColourSimilar;
                break;
            case "dissimilar":
                strokeColor = styles.compareStyles.linkColourDissimilar;
                break;
            default:
                strokeColor = styles.linkStyles.linkColour;
                break;
        }

    } else if (graphFormatCode === "planar") {
        switch (link.group) {
            case "linkColourCross":
                strokeColor = styles.planarStyles.linkColourCross;
                break;
            default:
                strokeColor = styles.linkStyles.linkColour;
                break;
        }
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
    let mouseStartX;
    let mouseStartY;

    const handleDragEnter = (event) => {
        console.log("starting");
        mouseStartX = event.clientX;
        mouseStartY = event.clientY;
        console.log("mouseStartX, mouseStartY", mouseStartX, mouseStartY);
    };

    const handleEnd = (event) => {
        console.log("end");
        let xPos = link.x1 - (mouseStartX - event.clientX);
        let yPos = link.y1 - (mouseStartY - event.clientY);
        if (!isNaN(xPos) && !isNaN(yPos)) {
            link.x1 = xPos;
            link.y1 = yPos;
            console.log("clientx,clienty", event.clientX, event.clientY);
            console.log("link.x1,link.y1", link.x1, link.y1);
        }
    };
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

    const textPathID = uuid();

    if (link.type === "tokenLink") {
        return (
            <path
                id={`edge${link.id}${textPathID}`}
                d={`M ${link.source.x} ${link.source.y} C  ${link.x1} ${link.y1} ${link.x1} ${link.y1} ${link.target.x} ${link.target.y}`}
                stroke={strokeColor}
                strokeWidth="2"
                fill="none"
                strokeDasharray="10,10"
            />
        );
    } else {
        return (
            <Group style={{
                cursor: "pointer",
            }}>
                <path
                    id={`edge${link.id}${textPathID}`}
                    d={`M ${link.source.x} ${link.source.y} C  ${link.x1} ${link.y1} ${link.x1} ${link.y1} ${link.target.x} ${link.target.y}`}
                    stroke={strokeColor}
                    strokeWidth="2"
                    fill="none"
                />
                <path
                    id={`edge${link.id}${textPathID}2`}
                    d={`M ${link.source.x} ${link.source.y} C  ${link.x1} ${link.y1} ${link.x1} ${link.y1} ${link.target.x} ${link.target.y}`}
                    stroke="rgba(0, 0, 0, 0)"
                    strokeWidth="40"
                    fill="none"
                />
                <text textAnchor="middle" dy="-1.5px">
                    <textPath
                        href={`#edge${link.id}${textPathID}`}
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
                    draggable="true"
                    onDragEnd={handleEnd}
                    onDragEnter={handleDragEnter}
                >
                    {link.label}
                </text>
            </Group>
        );
    }
}

export default Link;
