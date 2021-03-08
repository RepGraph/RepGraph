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
                        selected: oldLink.type === "link" ? oldLink.id === link.id ? !oldLink.selected : oldLink.selected : oldLink.selected
                    }));

                    dispatch({
                        type: "SET_SENTENCE_VISUALISATION",
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

    let mouseStartX,
        mouseStartY,
        linkStartX,
        linkStartY,
        click = false;

    const onMouseDown = (event) => {
        click = false;
        mouseStartX = event.clientX;
        mouseStartY = event.clientY;
        // console.log("mouse start: ", mouseStartX, mouseStartY);
        document.addEventListener("mouseup", onMouseUp);

        console.log("DOWN", click);
        // document.body.style.cursor="move"
    };

    const onMouseUp = (event) => {
        linkStartX = link.x1;
        linkStartY = link.y1;
        // console.log("link start: ", linkStartX, linkStartY);
        // console.log("mouse end: ", event.clientX, event.clientY);
        let changeX = mouseStartX - event.clientX;
        let changeY = mouseStartY - event.clientY;

        link.x1 = linkStartX - changeX;
        link.y1 = linkStartY - changeY;
        // console.log("change: ", changeX, changeY);
        // console.log("link end: ", link.x1, link.y1);
        document.removeEventListener("mouseup", onMouseUp);
        setHighlighted(true);
        setHighlighted(false);
        console.log("UP", click);
        //  document.getElementById(`dot${link.id}`).style.visibility = "visible";
    };
    const onClick = (event) => {
        console.log("click", click);
        if (click) {
            console.log("TRUE CLICK");
            document.getElementById(`dot${link.id}`).style.visibility = "hidden";
            document.removeEventListener("click", onClick);
        } else {
            click = true;
            console.log("CLICK BUT NOT CLICK");
        }
    };

    const handleDoubleClick = (event) => {
        click = true;
        if (
            document.getElementById(`dot${link.id}`).style.visibility === "visible"
        ) {
            document.getElementById(`dot${link.id}`).style.visibility = "hidden";
        } else {
            document.getElementById(`dot${link.id}`).style.visibility = "visible";
        }
        //  document.getElementById(`dot${link.id}`).style.visibility = "visible";
        document.addEventListener("click", onClick);
        console.log("DOUBLE", click);
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
            {EdgeLayout(link, strokeColor,onMouseDown, handleDoubleClick)}
        </Group>
    );
};

function EdgeLayout(link, strokeColor, onMouseDown, handleDoubleClick) {
    //let mouseStartX, mouseStartY, startLinkX, startLinkY, startOffsetX, startOffsetY;

    // const onMoveArrow = (event) => {
    //     console.log("mouse pos:", event.clientX, event.clientY);
    //     let changeX = mouseStartX - event.clientX;
    //     let changeY = mouseStartY - event.clientY;
    //     console.log("change x , y", changeX, changeY);
    //
    //     link.x1 = startLinkX - changeX;
    //     link.y1 = startLinkY - changeY;
    //     console.log("final link pos", link.x1, link.y1);
    // };
    //
    // const onClickArrow = (event) => {
    //     document.removeEventListener("mousemove", onMoveArrow);
    //     document.removeEventListener("click", onClickArrow);
    // };
    //
    // const handleDoubleClickArrow = (event) => {
    //     mouseStartX = event.clientX;
    //     mouseStartY = event.clientY;
    //     startLinkX = link.x1;
    //     startLinkY = link.y1;
    //     console.log("start pos:", link.x1, link.y1);
    //     console.log("mouseStartX, mouseStartY", mouseStartX, mouseStartY);
    //     document.addEventListener("mousemove", onMoveArrow);
    //     document.addEventListener("click", onClickArrow);
    // };

    // const onClickArrow = (event) => {
    //     console.log("mouse pos:", event.clientX, event.clientY);
    //     let changeX = mouseStartX - event.clientX;
    //     let changeY = mouseStartY - event.clientY;
    //     console.log("change x , y", changeX, changeY);
    //
    //     link.x1 = startLinkX - changeX;
    //     link.y1 = startLinkY - changeY;
    //     console.log("final link pos", link.x1, link.y1);
    //     document.removeEventListener("click", onClickArrow);
    // };
    //
    // const handleDoubleClickArrow = (event) => {
    //     mouseStartX = event.clientX;
    //     mouseStartY = event.clientY;
    //     startLinkX = link.x1;
    //     startLinkY = link.y1;
    //     console.log("start pos:", link.x1, link.y1);
    //     console.log("mouseStartX, mouseStartY", mouseStartX, mouseStartY);
    //     document.addEventListener("click", onClickArrow);
    // };

    // const onMoveLabel = (event) => {
    //     let changeX = mouseStartX - event.clientX;
    //     let changeY = mouseStartY - event.clientY;
    //     link.labelOffsetX = startOffsetX - changeX;
    //     link.labelOffsetY = startOffsetY - changeY;
    // };
    //
    // const onClickLabel = (event) => {
    //     document.removeEventListener("mousemove", onMoveLabel);
    //     document.removeEventListener("click", onClickLabel);
    // };
    //
    // const handleDoubleClickLabel = (event) => {
    //     mouseStartX = event.clientX;
    //     mouseStartY = event.clientY;
    //     startOffsetX = link.labelOffsetX;
    //     startOffsetY = link.labelOffsetY;
    //     document.addEventListener("mousemove", onMoveLabel);
    //     document.addEventListener("click", onClickLabel);
    // };

    //DIDN'T WORK
    // const onMouseUpArrow = (event) => {
    //     let changeX = mouseStartX - event.clientX;
    //     let changeY = mouseStartY - event.clientY;
    //     link.x1 = startLinkX - changeX;
    //     link.y1 = startLinkY - changeY;
    //
    //     document.removeEventListener("onmouseup", onMouseUpArrow);
    // };
    //
    // const onMouseDownArrow = (event) => {
    //     mouseStartX = event.clientX;
    //     mouseStartY = event.clientY;
    //     startLinkX = link.x1;
    //     startLinkY = link.y1;
    //     document.addEventListener("onmouseup", onMouseUpArrow);
    // };

    //DRAG END HAS NAN ISSUES
    // const onDragEnd = (event) => {
    //     console.log("mouse pos:", event.clientX, event.clientY);
    //     let changeX = mouseStartX - event.clientX;
    //     let changeY = mouseStartY - event.clientY;
    //     console.log("change x , y", changeX, changeY);
    //     link.x1 = startLinkX - changeX;
    //     link.y1 = startLinkY - changeY;
    // };
    //
    // const onDragEnter = (event) => {
    //     mouseStartX = event.clientX;
    //     mouseStartY = event.clientY;
    //     startLinkX = link.x1;
    //     startLinkY = link.y1;
    //
    //     console.log("start pos:", link.x1, link.y1);
    //     console.log("mouseStartX, mouseStartY", mouseStartX, mouseStartY);
    // };

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
            <Group rel="stylesheet" href="../../styles.css" class="unselectable">
                <path
                    id={`edge${link.id}${textPathID}`}
                    d={`M ${link.source.x} ${link.source.y} C  ${link.x1} ${link.y1} ${link.x1} ${link.y1} ${link.target.x} ${link.target.y}`}
                    stroke={strokeColor}
                    strokeWidth="2"
                    fill="none"
                />
                {/*<path*/}
                {/*    id={`edge${link.id}${textPathID}2`}*/}
                {/*    d={`M ${link.source.x} ${link.source.y} C  ${link.x1} ${link.y1} ${link.x1} ${link.y1} ${link.target.x} ${link.target.y}`}*/}
                {/*    stroke="rgba(0, 0, 0, 0)"*/}
                {/*    strokeWidth="40"*/}
                {/*    fill="none"*/}
                {/*/>*/}
                <text textAnchor="middle" dy="-1.5px">
                    <textPath
                        href={`#edge${link.id}${textPathID}`}
                        startOffset={`50%`}
                        side="left"
                        stroke={strokeColor}
                        fill={strokeColor}
                        fontSize="25px"
                        dominantBaseline="central"
                        onDoubleClick={handleDoubleClick}
                        // onMouseDown={onMouseDownArrow}
                        //   onDragEnter={onDragEnter}
                        //   onDragEnd={onDragEnd}
                    >
                        ➤
                    </textPath>
                </text>
                <text
                    textAnchor="middle"
                    fill={strokeColor}
                    x={xMid}
                    y={yMid}
                    dy={`${link.labelOffsetY}px`}
                    dx={`${link.labelOffsetX}px`}
                    fontWeight="bold"
                    // onDragEnd={handleEnd}
                    // onDragEnter={handleDragEnter}
                    // onDoubleClick={handleDoubleClickLabel}
                    // cursor="move"
                >
                    {link.label}
                </text>
                <circle
                    id={`dot${link.id}`}
                    cx={link.x1}
                    cy={link.y1}
                    r="5"
                    fill="red"
                    visibility="hidden"
                    onMouseDown={onMouseDown}
                    cursor="move"
                ></circle>
            </Group>
        );
    }
}

export default Link;
