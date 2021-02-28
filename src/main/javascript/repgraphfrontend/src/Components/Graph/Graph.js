import React, {useState, useRef, useCallback, useReducer, useContext} from "react";

import ZoomPortal from "./ZoomPortal";
import { Node } from "./Node";
import { Link } from "./Link";

import { useTooltip, useTooltipInPortal, defaultStyles } from "@visx/tooltip";
import {AppContext} from "../../Store/AppContextProvider";

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
    const {state, dispatch} = useContext(AppContext); //Provide access to global state

    const styles = state.graphStyles; //Use AppContext styles

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
                                                ? styles.nodeStyles.nodeColour
                                                : styles.tokenStyles.tokenColour
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
                                                            ? styles.nodeStyles.nodeColour
                                                            : styles.tokenStyles.tokenColour,
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
