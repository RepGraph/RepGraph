import React, {useState, useEffect, useContext} from "react";
import {makeStyles, useTheme} from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import {AppContext} from "../../Store/AppContextProvider";
import {cloneDeep} from "lodash";
import Graph from "react-graph-vis";
import layoutGraph from "../App";

import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";

import AddCircleOutlineIcon from "@material-ui/icons/AddCircleOutline";
import BuildIcon from "@material-ui/icons/Build";

import {Chip} from "@material-ui/core";
import Tooltip from "@material-ui/core/Tooltip";

import ListItem from "@material-ui/core/ListItem";

import Divider from "@material-ui/core/Divider";
import {Virtuoso} from "react-virtuoso";
import {Link, useHistory} from "react-router-dom";

const useStyles = makeStyles((theme) => ({
    root: {
        maxWidth: "100%",
        height: "100%",
        border: "1px solid red"
    },
    header: {
        display: "flex",
        alignItems: "center",
        height: 50,
        paddingLeft: theme.spacing(4),
        backgroundColor: theme.palette.background.default
    },
    body: {
        height: "100%",
        display: "block"
    }
}));

//These are the vis.js options - look at vis.js docs
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
        physics: true,
        arrows: {
            to: {
                scaleFactor: 1.3
            }
        },
        arrowStrikethrough: false,
        endPointOffset: {
            from: 0,
            to: 0
        },
        scaling: {
            min: 1,
            max: 6
        },
        value: 1
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
        node: {
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
        token: {
            shape: "box",
            color: "rgba(33,195,238,0.7)",
            font: {size: 14, strokeWidth: 4, strokeColor: "white"},
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        },
        similarNode: {
            shape: "box",
            color: "rgba(0,153,0,0.7)",
            font: {size: 14, strokeWidth: 4, strokeColor: "white"},
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        },
        differentNode: {
            shape: "box",
            color: "rgba(245, 0, 87, 0.7)",
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

//The events object is how you enable the vis.js events - look at vis.js docs
const events = {
    select: function (event) {
        let {nodes, edges} = event;
        console.log(nodes, edges); //logs the nodes and edges that were selected on the graph to console
        /*dispatch({
          type: "SET_SELECT_NODE_EDGE",
          payload: { selectedNodeAndEdges: { nodes, edges } }
        });*/
    }
};

function highlightCompare(standardVisualisation, similarNodes, similarEdges) {
    let currentStandardVisualisation = cloneDeep(standardVisualisation);

    let newNodes = currentStandardVisualisation.nodes.map((node, index) => ({
        ...node,
        group: similarNodes.includes(node.id)
            ? "similarNode"
            : node.group === "token"
                ? "token"
                : "differentNode"
    }));
    let newEdges = currentStandardVisualisation.edges.map((edge, index) => ({
        ...edge,
        /*group: similarEdges.includes(edge.id)
          ? "similarEdge"
          : edge.group,*/
        //background: similarEdges.includes(edge.id) ? true : true,
        color: similarEdges.includes(edge.id)
            ? "rgba(0,153,0,0.7)"
            : "rgba(245, 0, 87, 0.7)"
        //  width: similarEdges.includes(edge.id) ? "4" : "4"
    }));

    return {...currentStandardVisualisation, nodes: newNodes, edges: newEdges};
}

//This response is for debugging
const response = {
    SimilarNodes1: [0, 1, 3, 9, 2],
    SimilarEdges1: [3, 2, 0],
    SimilarEdges2: [3, 2],
    SimilarNodes2: [1, 2, 4]
};

function CompareTwoGraphsVisualisation(props) {
    const classes = useStyles();
    const theme = useTheme();
    const {state, dispatch} = useContext(AppContext);
    const history = useHistory();

    const [compareVis1, setCompareVis1] = React.useState(
        null
    );
    const [compareVis2, setCompareVis2] = React.useState(
        null
    );

    const [sentence1, setSentence1] = React.useState(null);
    const [sentence2, setSentence2] = React.useState(null);
    const [open, setOpen] = React.useState(false);
    const [selectSide, setSelectSide] = React.useState(null);

    function handleSelectSide(side) {
        //Need to request visualisation from backend and store result
        setSelectSide(side);
        setOpen(true);
    }

    function handleClose() {
        setOpen(false);
    }

    function handleCompareClick(){

        let requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        fetch(state.APIendpoint +"/CompareGraphs?graphID1="+sentence1+"&graphID2="+sentence2, requestOptions)
            .then(response => response.text())
            .then(result => {
                const jsonResult = JSON.parse(result);
                console.log(jsonResult); //debugging

                setCompareVis1(highlightCompare(compareVis1, jsonResult.SimilarNodes1, jsonResult.SimilarEdges1))
                setCompareVis2(highlightCompare(compareVis2, jsonResult.SimilarNodes2, jsonResult.SimilarEdges2))
            })
            .catch(error => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}});
                console.log("error", error);
                history.push("/404"); //for debugging
            });

    }

    //Component to let user select sentence from list of sentences uploaded in data-set
    function SelectSentenceList() {
        const {state, dispatch} = useContext(AppContext);
        const history = useHistory();

        function requestSentenceFromBackend(sentenceId){

            console.log("requestSentenceFromBackend: ", sentenceId);

            let requestOptions = {
                method: 'GET',
                redirect: 'follow'
            };

            //dispatch({type: "SET_LOADING", payload: {isLoading: true}});
            //state.APIendpoint+"/DisplayPlanarGraph?graphID=20001001

            fetch(state.APIendpoint + "/Visualise?graphID=" + sentenceId + "&format=1", requestOptions)
                .then((response) => response.text())
                .then((result) => {
                    const jsonResult = JSON.parse(result);
                    console.log(jsonResult);
                    //console.log(jsonResult);
                    //console.log(jsonResult.response);
                    //const formattedGraph = layoutGraph(jsonResult);
                    //dispatch({type: "SET_LOADING", payload: {isLoading: false}});
                    if (selectSide === 1) {
                        setCompareVis1(jsonResult)
                    } else {
                        setCompareVis2(jsonResult)
                    }
                })
                .catch((error) => {
                    dispatch({type: "SET_LOADING", payload: {isLoading: false}});
                    console.log("error", error);
                    history.push("/404"); //for debugging
                });
        }

        function handleSelectSentence(sentenceId) {
            console.log(sentenceId);
            setOpen(false);

            if (selectSide === 1) {
                setSentence1(sentenceId); //stores the id of the sentence selected for graph 1

                //Request visualisation from back-end
                requestSentenceFromBackend(sentenceId);

            } else {
                setSentence2(sentenceId); //stores the id of the sentence selected for graph 2

                //Request visualisation from back-end
                requestSentenceFromBackend(sentenceId);
            }
        }

        return (
            <Grid item style={{width: "100%"}}>
                {state.dataSet === null ? (
                    <div>No data-set has been uploaded yet</div>
                ) : (
                    <Virtuoso
                        style={{width: "100%", height: "400px"}}
                        totalCount={state.dataSet.length}
                        item={(index) => {
                            return (
                                <ListItem
                                    button
                                    key={state.dataSet[index].id}
                                    onClick={() => handleSelectSentence(state.dataSet[index].id)}
                                >
                                    <Typography>{state.dataSet[index].input}</Typography>
                                </ListItem>
                            );
                        }}
                        footer={() => (
                            <div style={{padding: "1rem", textAlign: "center"}}>
                                -- end of dataset --
                            </div>
                        )}
                    />
                )}
                <Divider/>
            </Grid>
        );
    }

    return (
        <Grid
            className={classes.root}
            container
            direction="row"
            justify="center"
            alignItems="center"
            spacing={1}
        >
            <Dialog
                fullWidth
                maxWidth="md"
                open={open}
                onClose={handleClose}
                aria-labelledby="select comparison sentence"
            >
                <DialogTitle id="select-comparison-sentence-title">
                    Select Graph: {selectSide}
                </DialogTitle>
                <DialogContent>
                    <SelectSentenceList/>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="primary">
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
            <Grid container item xs={6} className={classes.body} spacing={1}>
                <Grid
                    item
                    container
                    direction="column"
                    justify="center"
                    alignItems="center"
                >
                    <Tooltip
                        arrow
                        title={sentence1 === null ? "Select Sentence" : "Change Sentence"}
                    >
                        <Chip
                            onClick={() => {
                                console.log("chip clicked");
                                //Show the dialog to select a sentence
                                handleSelectSide(1);
                            }}
                            color="inherit"
                            label={sentence1 === null ? "No Sentence Selected" : sentence1}
                            icon={
                                sentence1 === null ? <AddCircleOutlineIcon/> : <BuildIcon/>
                            }
                        />
                    </Tooltip>
                </Grid>
                <Grid item style={{width: "100%", height: "70vh"}}>
                    {
                        compareVis1 === null ? <div>Please select a sentence</div> :

                            <Graph
                                graph={compareVis1} //The visualisation data for the current graph
                                options={options} //options object from above
                                events={events} //events object from above
                                style={{width: "100%", height: "100%"}}
                                getNetwork={(network) => {
                                    //  if you want access to vis.js network api you can set the state in a parent component using this property
                                }}
                            />}
                </Grid>
            </Grid>
            <Grid container item xs={6} className={classes.body} spacing={1}>
                <Grid
                    item
                    container
                    direction="column"
                    justify="center"
                    alignItems="center"
                >
                    <Tooltip
                        arrow
                        title={sentence2 === null ? "Select Sentence" : "Change Sentence"}
                    >
                        <Chip
                            onClick={() => {
                                console.log("chip clicked");
                                //Show the dialog to select a sentence
                                handleSelectSide(2);
                            }}
                            color="inherit"
                            label={sentence2 === null ? "No Sentence Selected" : sentence2}
                            icon={
                                sentence2 === null ? <AddCircleOutlineIcon/> : <BuildIcon/>
                            }
                        />
                    </Tooltip>
                </Grid>
                <Grid item style={{width: "100%", height: "70vh"}}>
                    {
                        compareVis2 === null ? <div>Please select a sentence</div> :

                            <Graph
                                graph={compareVis2} //The visualisation data for the current graph
                                options={options} //options object from above
                                events={events} //events object from above
                                style={{width: "100%", height: "100%"}}
                                getNetwork={(network) => {
                                    //  if you want access to vis.js network api you can set the state in a parent component using this property
                                }}
                            />}
                </Grid>
            </Grid>
            <Grid item>
                <Button variant="contained" color="primary" disabled={compareVis1 === null || compareVis2 === null} onClick={handleCompareClick}>
                    Compare
                </Button>
            </Grid>
        </Grid>
    );
}

export default CompareTwoGraphsVisualisation;
