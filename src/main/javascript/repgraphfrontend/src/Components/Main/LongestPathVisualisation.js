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

    for (let index = 0; index < path.length - 1; index++) {
        for (const [i, e] of currentStandardVisualisation.edges.entries()) {
            if (
                (path[index] === e.from && path[index + 1] === e.to) ||
                (path[index] === e.to && path[index + 1] === e.from)
            ) {
                currentStandardVisualisation.edges[i] = {
                    ...e,
                    color: "rgba(0, 0, 0, 1)",
                    shadow: true,
                    background: {
                        enabled: true,
                        color: "rgba(245, 0, 87, 0.7)"
                    }
                };
            }
        }
    }

    return {...currentStandardVisualisation, nodes: newNodes};
}

function LongestPathVisualisation({type}) {
    const classes = useStyles();
    const theme = useTheme();
    const {state, dispatch} = useContext(AppContext);
    const [activeStep, setActiveStep] = React.useState(0);
    //const maxSteps = tutorialSteps.length;
    const maxSteps = state.testResults[type].length;


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
            color: state.visualisationOptions.edges.color,
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
            color: state.visualisationOptions.groups.node.color,
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
            node: {
                shape: "box",
                color: state.visualisationOptions.groups.node.color,
                font: {size: 14, strokeWidth: 4, strokeColor: "white"},
                widthConstraint: {
                    minimum: 60,
                    maximum: 60
                },
                heightConstraint: {
                    minimum: 30
                }
            }, surfaceNode: {
                shape: "box",
                color: state.visualisationOptions.groups.surfaceNode.color,
                font: {size: 14, strokeWidth: 4, strokeColor: "white"},
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
                color: state.visualisationOptions.groups.token.color,
                font: {size: 14, strokeWidth: 4, strokeColor: "white"},
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
                color: state.visualisationOptions.groups.longestPath.color,
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
                autoplay={false}
            >
                {state.testResults[type].map((path, index) => (
                    <div className={classes.body} key={index}>
                        {Math.abs(activeStep - index) <= 2 ? (
                            <Graph
                                graph={showLongestPath(
                                    state.selectedSentenceVisualisation,
                                    path
                                )} //Modified current visualisation with longest path highlighted
                                options={state.visualisationOptions} //Options from global state
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
