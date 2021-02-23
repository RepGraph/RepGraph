import React, { useState, useRef, useCallback, useReducer } from "react";

import ZoomPortal from "./ZoomPortal";
import { Node } from "./Node";
import { Link } from "./Link";

import { useTooltip, useTooltipInPortal, defaultStyles } from "@visx/tooltip";

export const Graph = ({
                          graph,
                          height,
                          width,
                          graphFormatCode,
                          adjacentLinks,
                          events
                      }) => {
    const { nodes, links } = graph;
    const [tooltipData, setTooltipData] = useState({ extraInformation: {} });

    const reducerNodes = (state, action) => {
        switch (action.type) {
            case "add":
                switch (events.select.selectMode) {
                    case "subset":
                        if (state.length !== 0) {
                            events.select.selectedNodesStateSetter(state);
                            return state;
                        } else {
                            events.select.selectedNodesStateSetter([action.id]);
                            return [action.id];
                        }
                    case "subgraph":
                        events.select.selectedNodesStateSetter([...state, action.id]);
                        return [...state, action.id];
                    default:
                        events.select.selectedNodesStateSetter([...state, action.id]);
                        return [...state, action.id];
                }

            case "remove":
                switch (events.select.selectMode) {
                    case "subset":
                        if (state.length === 1 && state[0] === action.id) {
                            events.select.selectedNodesStateSetter([]);
                            return [];
                        } else {
                            events.select.selectedNodesStateSetter(state);
                            return state;
                        }
                    case "subgraph":
                        events.select.selectedNodesStateSetter(state.filter((item) => item !== action.id));
                        return state.filter((item) => item !== action.id);
                    default:
                        events.select.selectedNodesStateSetter(state.filter((item) => item !== action.id));
                        return state.filter((item) => item !== action.id);
                }

            default:
                throw new Error();
        }
    };

    const reducerLinks = (state, action) => {
        switch (action.type) {
            case "add":
                switch (events.select.selectMode) {
                    case "subset":
                        events.select.selectedLinksStateSetter([]);
                        return [];
                    case "subgraph":
                        events.select.selectedLinksStateSetter([...state, action.id]);
                        return [...state, action.id];
                    default:
                        events.select.selectedLinksStateSetter([...state, action.id]);
                        return [...state, action.id];
                }
            case "remove":
                switch (events.select.selectMode) {
                    case "subset":
                        events.select.selectedLinksStateSetter([]);
                        return [];
                    case "subgraph":
                        events.select.selectedLinksStateSetter(state.filter((item) => item !== action.id));
                        return state.filter((item) => item !== action.id);
                    default:
                        events.select.selectedLinksStateSetter(state.filter((item) => item !== action.id));
                        return state.filter((item) => item !== action.id);
                }
            default:
                throw new Error();
        }
    };

    const [selectedNodes, dispatchSelectedNodes] = useReducer(reducerNodes, []);
    const [selectedLinks, dispatchSelectedLinks] = useReducer(reducerLinks, []);

    const styles = {
        backgroundColour: "#efefef",
        hierarchicalStyles: {
            nodeStyles: {
                nodeColour: "rgba(0,172,237,1)",
                hoverColour: "rgba(82, 208, 255,1)",
                spanColour: "rgba(0,0,0,0.3)",
                selectedColour: "#3de68c"
            },
            linkStyles: {
                linkColour: "rgba(0,0,0,1)",
                hoverColour: "rgba(0,0,0,0.5)",
                selectedColour: "#3de68c"
            },
            tokenStyles: {
                tokenColour: "rgba(255, 220, 106,1)",
                hoverColour: "rgba(255, 232, 156,1)",
                selectedColour: "#3de68c"
            }
        },
        treeStyles: {
            nodeStyles: {
                nodeColour: "rgba(0,172,237,1)",
                hoverColour: "rgba(82, 208, 255,1)",
                spanColour: "rgba(0,0,0,0.3)",
                selectedColour: "#3de68c"
            },
            linkStyles: {
                linkColour: "rgba(0,0,0,1)",
                hoverColour: "rgba(0,0,0,0.5)",
                selectedColour: "#3de68c"
            },
            tokenStyles: {
                tokenColour: "rgba(255, 220, 106,1)",
                hoverColour: "rgba(255, 232, 156,1)",
                selectedColour: "#3de68c"
            }
        },
        longestPathStyles: {
            linkColour: "rgba(225, 9, 9, 1)",
            nodeColour: "rgba(225, 9, 9, 1)",
            hoverColour: "rgba(248, 84, 84, 1)"
        },
        compareStyles: {
            linkColourDissimilar: "rgba(225, 9, 9, 1)",
            linkColourSimilar: "rgba(67, 220, 24, 1)",
            nodeColourDissimilar: "rgba(225, 9, 9, 1)",
            nodeColourSimilar: "rgba(67, 220, 24, 1)",
            hoverNodeColourSimilar: "rgba(125, 237, 94, 1)",
            hoverNodeColourDissimilar: "rgba(248, 84, 84, 1)",
            hoverLinkColourSimilar: "rgba(125, 237, 94, 1)",
            hoverLinkColourDissimilar: "rgba(248, 84, 84, 1)"
        },
        planarStyles: {
            linkColourCross: "rgba(225, 9, 9, 1)",
            hoverColour: "rgba(248, 84, 84, 1)"
            //still to do
        }
    };

    let graphStyles = null;

    switch (graphFormatCode) {
        case "hierarchical":
            graphStyles = { ...styles.hierarchicalStyles };
            break;
        case "tree":
            graphStyles = { ...styles.treeStyles };
            break;
        case "hierarchicalLongestPath":
            graphStyles = {
                ...styles.hierarchicalStyles,
                longestPathStyles: styles.longestPathStyles
            };
            break;
        case "treeLongestPath":
            graphStyles = {
                ...styles.hierarchicalStyles,
                longestPathStyles: styles.longestPathStyles
            };
            break;
        case "hierarchicalCompare":
            graphStyles = {
                ...styles.hierarchicalStyles,
                compareStyles: styles.compareStyles
            };
            break;
        case "treeCompare":
            graphStyles = {
                ...styles.treeStyles,
                compareStyles: styles.compareStyles
            };
            break;
        default:
            graphStyles = { ...styles.hierarchicalStyles };
            break;
    }

    //console.log("graphStyles", graphStyles);

    const {
        tooltipLeft,
        tooltipTop,
        tooltipOpen,
        showTooltip,
        hideTooltip
    } = useTooltip();

    const { containerRef, containerBounds, TooltipInPortal } = useTooltipInPortal(
        {
            // use TooltipWithBounds
            detectBounds: true,
            // when tooltip containers are scrolled, this will correctly update the Tooltip position
            scroll: true
        }
    );

    const handleMouseOver = useCallback(
        (event) => {
            // coordinates should be relative to the container in which Tooltip is rendered
            const containerX =
                ("clientX" in event ? event.clientX : 0) - containerBounds.left;
            const containerY =
                ("clientY" in event ? event.clientY : 0) - containerBounds.top;
            showTooltip({
                tooltipLeft: containerX,
                tooltipTop: containerY,
                tooltipData
            });
        },
        [showTooltip, containerBounds, tooltipData]
    );

    return (
        <div
            ref={containerRef}
            style={{
                display: "flex",
                flexDirection: "column",
                border: "0px solid red",
                height: "100%",
                // touchAction: "none"
            }}
        >
            <ZoomPortal
                width={width}
                height={height}
                backgroundColour={styles.backgroundColour}
            >
                {links.map((link, i) => (
                    <Link
                        key={`link-${i}`}
                        link={link}
                        dispatchSelectedLinks={dispatchSelectedLinks}
                        selectedLinks={selectedLinks}
                        styles={graphStyles}
                        graphFormatCode={graphFormatCode}
                        tooltipData={tooltipData}
                        adjacentLinks={adjacentLinks}
                        tooltipOpen={tooltipOpen}
                        events={events}
                    />
                ))}
                {nodes.map((node, i) => (
                    <Node
                        key={`node-${i}`}
                        node={node}
                        handleMouseOver={handleMouseOver}
                        hideTooltip={hideTooltip}
                        setTooltipData={setTooltipData}
                        dispatchSelectedNodes={dispatchSelectedNodes}
                        selectedNodes={selectedNodes}
                        styles={graphStyles}
                        graphFormatCode={graphFormatCode}
                        events={events}
                    />
                ))}
                {tooltipOpen && (
                    <TooltipInPortal
                        // set this to random so it correctly updates with parent bounds
                        key={Math.random()}
                        top={tooltipTop}
                        left={tooltipLeft}
                        style={{
                            ...defaultStyles,
                            borderRadius: "8px",
                            padding: "0.4rem",
                            zIndex: 10000
                        }}
                    >
                        <div className="tooltip">
                            <div
                                className="tooltipSection"
                                style={{
                                    marginBottom: "0.4rem"
                                }}
                            >
                                <div
                                    style={{
                                        color: "black",
                                        marginBottom: "0.4rem"
                                    }}
                                >
                                    Label:
                                </div>
                                <div
                                    style={{
                                        color:
                                            tooltipData.type === "node"
                                                ? graphStyles.nodeStyles.nodeColour
                                                : graphStyles.tokenStyles.tokenColour
                                    }}
                                >
                                    {tooltipData.label}
                                </div>
                            </div>
                            <div className="tooltipSection">
                                <div
                                    style={{
                                        color: "black"
                                    }}
                                >
                                    Extra Information:
                                </div>
                                <div className="tooltipDivider"></div>
                                {Object.entries(tooltipData.extraInformation).map(
                                    (entry, i) => {
                                        return (
                                            <div
                                                key={`extraInfo-${i}`}
                                                className="extraInfo"
                                                style={{
                                                    color:
                                                        tooltipData.type === "node"
                                                            ? graphStyles.nodeStyles.nodeColour
                                                            : graphStyles.tokenStyles.tokenColour,
                                                    marginTop: "0.4rem",
                                                    width: "100%"
                                                }}
                                            >
                                                <div
                                                    style={{
                                                        color: "black",
                                                        marginLeft: "0.4rem",
                                                        marginRight: "0.4rem",
                                                        width: "auto"
                                                    }}
                                                >
                                                    {entry[0]}:
                                                </div>
                                                <div style={{ marginRight: "0.4rem", width: "auto" }}>
                                                    {JSON.stringify(entry[1])}
                                                </div>
                                            </div>
                                        );
                                    }
                                )}
                            </div>
                        </div>
                    </TooltipInPortal>
                )}
            </ZoomPortal>
        </div>
    );
};

export default Graph;
