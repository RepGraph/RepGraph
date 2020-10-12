import React, { useState, useEffect, useContext } from "react";
import { makeStyles, useTheme } from "@material-ui/core/styles";
import MobileStepper from "@material-ui/core/MobileStepper";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import KeyboardArrowLeft from "@material-ui/icons/KeyboardArrowLeft";
import KeyboardArrowRight from "@material-ui/icons/KeyboardArrowRight";
import SwipeableViews from "react-swipeable-views";
import { autoPlay } from "react-swipeable-views-utils";

import { AppContext } from "../../Store/AppContextProvider";
import { cloneDeep } from "lodash";
import Graph from "react-graph-vis";

const AutoPlaySwipeableViews = autoPlay(SwipeableViews);

export const layoutGraph = (sentence) => {
    let graph = sentence;

    //Determine span lengths of each node
    const graphNodeSpanLengths = graph.nodes
        .map((node) => node.anchors[0])
        .map((span) => span.end - span.from);
    //Determine unique span lengths of all the node spans
    let uniqueSpanLengths = [];
    const map = new Map();
    for (const item of graphNodeSpanLengths) {
        if (!map.has(item)) {
            map.set(item, true); // set any value to Map
            uniqueSpanLengths.push(item);
        }
    }
    uniqueSpanLengths.sort((a, b) => a - b); //sort unique spans ascending

    //Sort the nodes into each level based on their spans
    let nodesInLevels = [];
    for (const level of uniqueSpanLengths) {
        let currentLevel = [];

        for (
            let spanIndex = 0;
            spanIndex < graphNodeSpanLengths.length;
            spanIndex++
        ) {
            if (graphNodeSpanLengths[spanIndex] === level) {
                currentLevel.push(graph.nodes[spanIndex]);
            }
        }

        nodesInLevels.push(currentLevel);
    }
    //Find the nodes in each level with the same span and group them together
    //Find the unique spans in each level
    let uniqueSpansInLevels = [];
    for (let level of nodesInLevels) {
        let uniqueSpans = []; //Stores the "stringified" objects
        const spanMap = new Map();
        for (const node of level) {
            if (!spanMap.has(JSON.stringify(node.anchors))) {
                spanMap.set(JSON.stringify(node.anchors), true); // set any value to Map
                uniqueSpans.push(JSON.stringify(node.anchors));
            }
        }
        uniqueSpansInLevels.push(uniqueSpans);
        //console.log(uniqueSpans);
    }

    //Iterate through the unique spans in each level and group the same ones together
    for (let level = 0; level < nodesInLevels.length; level++) {
        let newLevelOfGroups = [];
        for (let uniqueSpan of uniqueSpansInLevels[level]) {
            //find the nodes in the level that have the same span and group them together
            let nodesWithCurrentSpan = nodesInLevels[level].filter(
                (node) => JSON.stringify(node.anchors) === uniqueSpan
            );
            newLevelOfGroups.push(nodesWithCurrentSpan);
        }
        nodesInLevels[level] = newLevelOfGroups;
    }

    //Determine the actual number of levels needed
    let height = 0;
    let previousLevelHeights = [0];
    for (let level of nodesInLevels) {
        let maxLevelHeight = 0;
        for (let item of level) {
            maxLevelHeight = Math.max(maxLevelHeight, item.length);
        }
        previousLevelHeights.push(maxLevelHeight);
        height += maxLevelHeight;
    }
    //console.log({height});
    //console.log({nodesInLevels});
    //console.log({previousLevelHeights});

    //Sort the nodes into the final levels
    let nodesInFinalLevels = [];
    for (let index = 0; index < height; index++) {
        nodesInFinalLevels.push([]);
    }
    for (let level = 0; level < nodesInLevels.length; level++) {
        //console.log(nodesInLevels[level]);
        for (let group of nodesInLevels[level]) {
            //console.log({group});
            for (
                let nodeGroupIndex = 0;
                nodeGroupIndex < group.length;
                nodeGroupIndex++
            ) {
                //console.log(group[nodeGroupIndex]);
                let finalLevel =
                    previousLevelHeights
                        .slice(0, level + 1)
                        .reduce((accumulator, currentValue) => accumulator + currentValue) +
                    nodeGroupIndex;
                nodesInFinalLevels[finalLevel].push(group[nodeGroupIndex]);
            }
        }
    }
    //console.log({ nodesInFinalLevels });

    //Map the nodes in each level to the correct format

    const totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each node and the spaces between them

    for (let level = 0; level < nodesInFinalLevels.length; level++) {
        nodesInFinalLevels[level] = nodesInFinalLevels[level].map((node) => ({
            id: node.id,
            x: node.anchors[0].from * 110,
            y: totalGraphHeight - level * (totalGraphHeight / height),
            label: node.label,
            type: "node",
            nodeLevel: level,
            anchors: node.anchors[0],
            group: "node"
        }));
    }

    const tokens = graph.tokens.map((token) => ({
        index: token.index,
        x: token.index * 110,
        y: totalGraphHeight + 100,
        label: token.form,
        type: "token",
        group: "token"
    }));

    //this.setState({graphData: nodesInFinalLevels.flat().concat(tokens)});

    const finalGraphNodes = nodesInFinalLevels
        .flat()
        .concat(tokens)
        .map((node) => ({
            id: node.id,
            x: node.x,
            y: node.y,
            label: node.label,
            title: node.label + " tooltip text",
            group: node.group,
            anchors: node.anchors,
            fixed: true,
            nodeLevel: node.nodeLevel
        }));

    const finalGraphEdges = graph.edges.map((edge, index) => {
        /*const fromID =
              finalGraphNodes[
                  finalGraphNodes.findIndex((node) => node.id === edge.source)
                  ].id;
          const toID =
              finalGraphNodes[
                  finalGraphNodes.findIndex((node) => node.id === edge.target)
                  ].id;*/

        const fromNode = finalGraphNodes.find((node) => node.id === edge.source);
        const toNode = finalGraphNodes.find((node) => node.id === edge.target);

        const fromID = fromNode.id;
        const toID = toNode.id;

        let edgeType = "";

        if (fromNode.nodeLevel === toNode.nodeLevel && fromNode.nodeLevel === 0) {
            edgeType = "curvedCW";
        } else {
            edgeType = "dynamic";
        }

        return {
            id: index,
            from: fromID,
            to: toID,
            label: edge.label,
            smooth: { type: edgeType, roundness: 0.4 },
            endPointOffset: {
                from: 20,
                to: 0
            }
        };
        /*source: testGraphNodes[edge.source],
                      target: testGraphNodes[edge.target],*/
    });

    const finalGraph = {
        nodes: finalGraphNodes,
        edges: finalGraphEdges
    };

    return finalGraph;
};

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
                /*enabled: true,
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
        }
    },
    nodes: {
        shape: "box",
        color: "rgba(97,195,238,0.5)",
        font: { size: 14, strokeWidth: 4, strokeColor: "white" },
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
    interaction: { hover: true },
    groups: {
        node: {
            shape: "box",
            color: "rgba(84, 135, 237, 0.5)",
            font: { size: 14, strokeWidth: 4, strokeColor: "white" },
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        },
        token: {
            shape: "box",
            color: "rgba(84, 237, 110, 0.7)",
            font: { size: 14, strokeWidth: 4, strokeColor: "white" },
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        },
        longestPath: {
            shape: "box",
            color: "rgba(245, 0, 87, 0.7)",
            font: { size: 14, strokeWidth: 4, strokeColor: "white" },
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

const useStyles = makeStyles((theme) => ({
    root: {
        maxWidth: "100%",
        flexGrow: 1
    },
    header: {
        display: "flex",
        alignItems: "center",
        height: 50,
        paddingLeft: theme.spacing(4),
        backgroundColor: theme.palette.background.default
    },
    body: {
        height: "70vh",
        display: "block",
        maxWidth: "100%",
        overflow: "hidden",
        width: "100%"
    }
}));

function showLongestPath(standardVisualisation, path) {
    let currentStandardVisualisation = cloneDeep(standardVisualisation);
    //console.log(currentStandardVisualisation);
    let newNodes = currentStandardVisualisation.nodes.map((node, index) => ({
        ...node,
        label: path.includes(node.id)
            ? path.findIndex((nodeID) => nodeID === node.id) + " " + node.label
            : node.label,
        group: path.includes(node.id) ? "longestPath" : node.group
    }));
    //console.log(newNodes);
    return { ...currentStandardVisualisation, nodes: newNodes };
}

function LongestPathVisualisation({ type }) {
    const classes = useStyles();
    const theme = useTheme();
    const { state, dispatch } = useContext(AppContext);
    const [activeStep, setActiveStep] = React.useState(0);
    //const maxSteps = tutorialSteps.length;
    const maxSteps = state.testResults[type].length;

    const handleNext = () => {
        setActiveStep((prevActiveStep) => prevActiveStep + 1);
    };

    const handleBack = () => {
        setActiveStep((prevActiveStep) => prevActiveStep - 1);
    };

    const handleStepChange = (step) => {
        setActiveStep(step);
    };

    const events = {
        select: function (event) {
            let { nodes, edges } = event;
            console.log(nodes, edges);
            dispatch({
                type: "SET_SELECT_NODE_EDGE",
                payload: { selectedNodeAndEdges: { nodes, edges } }
            });
        }
    };

    return (
        <div className={classes.root}>
            <Paper square elevation={0} className={classes.header}>
                <Typography>
                    {"The path is (in terms of Node IDs): " +
                    JSON.stringify(state.testResults[type][activeStep])}
                </Typography>
            </Paper>
            <AutoPlaySwipeableViews
                axis={theme.direction === "rtl" ? "x-reverse" : "x"}
                index={activeStep}
                onChangeIndex={handleStepChange}
                enableMouseEvents
            >
                {state.testResults[type].map((path, index) => (
                    <div className={classes.body} key={index}>
                        {Math.abs(activeStep - index) <= 2 ? (
                            <Graph
                                graph={showLongestPath(
                                    state.selectedSentenceVisualisation,
                                    path
                                )}
                                options={options}
                                events={events}
                                style={{ width: "100%", height: "100%" }}
                                getNetwork={(network) => {
                                    //  if you want access to vis.js network api you can set the state in a parent component using this property
                                }}
                            />
                        ) : null}
                    </div>
                ))}
            </AutoPlaySwipeableViews>
            <MobileStepper
                steps={maxSteps}
                position="static"
                variant="text"
                activeStep={activeStep}
                nextButton={
                    <Button
                        size="small"
                        onClick={handleNext}
                        disabled={activeStep === maxSteps - 1}
                    >
                        Next
                        {theme.direction === "rtl" ? (
                            <KeyboardArrowLeft />
                        ) : (
                            <KeyboardArrowRight />
                        )}
                    </Button>
                }
                backButton={
                    <Button size="small" onClick={handleBack} disabled={activeStep === 0}>
                        {theme.direction === "rtl" ? (
                            <KeyboardArrowRight />
                        ) : (
                            <KeyboardArrowLeft />
                        )}
                        Back
                    </Button>
                }
            />
        </div>
    );
}

export default LongestPathVisualisation;
