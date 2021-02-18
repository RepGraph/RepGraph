import React, { useState, useRef, useCallback, useReducer } from "react";

import ZoomPortal from "./ZoomPortal";
import { Node } from "./Node";
import { Link } from "./Link";

import { useTooltip, useTooltipInPortal, defaultStyles } from "@visx/tooltip";

const reducer = (state, action) => {
    switch (action.type) {
        case "add":
            return [...state, action.id];
        case "remove":
            return state.filter((item) => item !== action.id);
        default:
            throw new Error();
    }
};

export const Graph = ({
                          graph,
                          height,
                          width,
                          selectMultiple,
                          graphFormatCode,
                          adjacentLinks
                      }) => {
    const { nodes, links } = graph;
    const [tooltipData, setTooltipData] = useState({ extraInformation: {} });
    const [selectedNodes, dispatchSelectedNodes] = useReducer(reducer, []);
    const [selectedLinks, dispatchSelectedLinks] = useReducer(reducer, []);

    // const styles = {
    //   backgroundColour: "#efefef",
    //   standardStyles: {
    //     nodeStyles: {
    //       nodeColour: "rgba(0,172,237,1)",
    //       hoverColour: "rgba(82, 208, 255,1)",
    //       spanColour: "rgba(0,0,0,0.3)",
    //       selectedColour: "#3de68c"
    //     },
    //     linkStyles: {
    //       linkColour: "rgba(0,0,0,1)",
    //       hoverColour: "rgba(0,0,0,0.5)",
    //       selectedColour: "#3de68c"
    //     },
    //     tokenStyles: {
    //       tokenColour: "rgba(255, 220, 106,1)",
    //       hoverColour: "rgba(255, 232, 156,1)",
    //       selectedColour: "#3de68c"
    //     }
    //   },
    //   longestPathStyles: {
    //     linkColour: "rgba(225, 9, 9, 1)",
    //     nodeColour: "rgba(225, 9, 9, 1)",
    //     hoverColour: "rgba(248, 84, 84, 1)"
    //   },
    //   compareStyles: {
    //     linkColourDifferent: "rgba(225, 9, 9, 1)",
    //     linkColourSame: "rgba(67, 220, 24, 1)",
    //     nodeColourDifferent: "rgba(225, 9, 9, 1)",
    //     nodeColourSame: "rgba(67, 220, 24, 1)",
    //     hoverNodeColourSame: "rgba(125, 237, 94, 1)",
    //     hoverNodeColourDifferent: "rgba(248, 84, 84, 1)",
    //     hoverLinkColourSame: "rgba(125, 237, 94, 1)",
    //     hoverLinkColourDifferent: "rgba(248, 84, 84, 1)"
    //   },
    //   planarStyles: {
    //     linkColourCross: "rgba(225, 9, 9, 1)",
    //     hoverColour: "rgba(248, 84, 84, 1)"
    //   }
    // };

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
            linkColourDifferent: "rgba(225, 9, 9, 1)",
            linkColourSame: "rgba(67, 220, 24, 1)",
            nodeColourDifferent: "rgba(225, 9, 9, 1)",
            nodeColourSame: "rgba(67, 220, 24, 1)",
            hoverNodeColourSame: "rgba(125, 237, 94, 1)",
            hoverNodeColourDifferent: "rgba(248, 84, 84, 1)",
            hoverLinkColourSame: "rgba(125, 237, 94, 1)",
            hoverLinkColourDifferent: "rgba(248, 84, 84, 1)"
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
                height: "100%"
                // touchAction: "none"
            }}
        >
            {/*<div>*/}
            {/*    <h4>selectedNodes:{JSON.stringify(selectedNodes)}</h4>*/}
            {/*    <h4>selectedLinks:{JSON.stringify(selectedLinks)}</h4>*/}
            {/*</div>*/}
            <ZoomPortal
                width={width}
                height={height}
                backgroundColour={styles.backgroundColour}
            >
                {links.map((link, i) => (
                    <Link
                        key={`link-${i}`}
                        link={link}
                        selectMultiple={selectMultiple}
                        dispatchSelectedLinks={dispatchSelectedLinks}
                        styles={graphStyles}
                        graphFormatCode={graphFormatCode}
                        tooltipData={tooltipData}
                        adjacentLinks={adjacentLinks}
                        tooltipOpen={tooltipOpen}
                    />
                ))}
                {nodes.map((node, i) => (
                    <Node
                        key={`node-${i}`}
                        node={node}
                        handleMouseOver={handleMouseOver}
                        hideTooltip={hideTooltip}
                        setTooltipData={setTooltipData}
                        selectMultiple={selectMultiple}
                        dispatchSelectedNodes={dispatchSelectedNodes}
                        styles={graphStyles}
                        graphFormatCode={graphFormatCode}
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
                            padding: "0.4rem"
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
                                            tooltipData.group === "node"
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
                                                        tooltipData.group === "node"
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
