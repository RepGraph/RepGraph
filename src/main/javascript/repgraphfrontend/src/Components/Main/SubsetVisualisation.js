import React, {useState, useEffect, useContext} from "react";
import Graph from "react-graph-vis";
import {AppContext} from "../../Store/AppContextProvider.js";

const options = {
    physics: {
        enabled: true,
        forceAtlas2Based: {
            gravitationalConstant: -50000,
            centralGravity: 0.0,
            springConstant: 0.08,
            springLength: 100,
            damping: 0,
            avoidOverlap: 1
        }
    },
    autoResize: true,
    edges: {
        color: "rgba(156, 154, 154, 1)",
        smooth: true,
        /*smooth: {
          enabled: true,
          type: "dynamic",
          roundness: 1
        },*/
        physics: true,
        arrows: {
            to: {
                /*
                enabled: true,
                type: "image",
                imageWidth: 25,
                imageHeight: 20,
                src: "data:image/svg+xml,%3Csvg version='1.1' id='Layer_1' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' x='0px' y='0px' viewBox='0 0 122.88 66.91' style='enable-background:new 0 0 122.88 66.91' xml:space='preserve'%3E%3Cg%3E%3Cpath style='fill:rgba(156, 154, 154, 1);' d='M11.68,64.96c-2.72,2.65-7.08,2.59-9.73-0.14c-2.65-2.72-2.59-7.08,0.13-9.73L56.87,1.97l4.8,4.93l-4.81-4.95 c2.74-2.65,7.1-2.58,9.76,0.15c0.08,0.08,0.15,0.16,0.23,0.24L120.8,55.1c2.72,2.65,2.78,7.01,0.13,9.73 c-2.65,2.72-7,2.78-9.73,0.14L61.65,16.5L11.68,64.96L11.68,64.96z'/%3E%3C/g%3E%3C/svg%3E",
                */
                scaleFactor: 1.3
            }
        },
        arrowStrikethrough: false,
        endPointOffset: {
            from: 20,
            to: 0
        },
    },
    nodes: {
        shape: "box",
        color: "rgba(97,195,238,0.5)",
        font: {size: 14, strokeWidth: 4, strokeColor: "white"},
        widthConstraint: {
            minimum: 60,
            maximum: 60
        },
        heightConstraint: {
            minimum: 30
        }
    },
    height: "100%",
    width: "100%",
    interaction: {hover: true},
    groups: {
        'node': {
            shape: "box",
            color: "rgba(84, 135, 237, 0.5)",
            font: {size: 14, strokeWidth: 4, strokeColor: "white"},
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        },
        'token': {
            shape: "box",
            color: "rgba(84, 237, 110, 0.7)",
            font: {size: 14, strokeWidth: 4, strokeColor: "white"},
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        }
    }
};


const SubsetVisualisation = (props) => {
        const {state, dispatch} = useContext(AppContext);
        const {subsetGraphData} = props;

        const events = {
        };

        return (
            <Graph
                graph={subsetGraphData}
                options={options}
                events={events}
                style={{width: "100%", height: "100%"}}
                getNetwork={(network) => {
                    // if you want access to vis.js network api you can set the state in a parent component using this property
                    network.on("hoverNode", function (params) {
                        network.canvas.body.container.style.cursor = 'pointer'
                    });
                }}
            />
        );
    }
;

export default SubsetVisualisation;
