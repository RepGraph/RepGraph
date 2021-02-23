import React, {useState, useEffect, useContext} from "react";
import {makeStyles, useTheme} from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import {AppContext} from "../../Store/AppContextProvider";
import {cloneDeep} from "lodash";

import {Graph} from "../Graph/Graph";

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
import TextField from "@material-ui/core/TextField/TextField";
import {determineAdjacentLinks, layoutHierarchy} from "../../LayoutAlgorithms/layoutHierarchy";
import {layoutTree} from "../../LayoutAlgorithms/layoutTree";
import {layoutFlat} from "../../LayoutAlgorithms/layoutFlat";
import {ParentSize} from "@visx/responsive";

const useStyles = makeStyles((theme) => ({
    root: {
        maxWidth: "100%",
        height: "100%",
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
    },
    graphDiv: {
        height: "100%",
        //border: "1px solid red",
        flex: "1",
        width: "100%"
    }
}));

// function highlightCompare(standardVisualisation, similarNodes, similarEdges) {
//     let currentStandardVisualisation = cloneDeep(standardVisualisation);
//
//     let newNodes = currentStandardVisualisation.nodes.map((node, index) => ({
//         ...node,
//         group: similarNodes.includes(node.id) ? "similarNode" :
//             node.group === "token" ? "token" :
//                 node.group === "text" ? "text" : node.group === "top" ? "top" : "differentNode"
//
//     }));
//     let newEdges = currentStandardVisualisation.edges.map((edge, index) => ({
//         ...edge,
//         color: edge.group === "tokenEdge" ? "rgba(156, 154, 154, 1)" : similarEdges.includes(edge.id)
//             ? "rgba(0,153,0,0.7)"
//             : "rgba(245, 0, 87, 0.7)"
//     }));
//
//     return { ...currentStandardVisualisation, nodes: newNodes, edges: newEdges };
// }

const updateCompareGroups = (standardVisualisation, similarNodes, similarEdges) => {
    let currentStandardVisualisation = cloneDeep(standardVisualisation);

    let newNodes = currentStandardVisualisation.nodes.map(node =>({
        ...node,
        group: similarNodes.includes(node.id) ? "similar" : node.type === "node" ? "dissimilar" : node.group
    }))

    let newLinks = currentStandardVisualisation.links.map(link =>({
        ...link,
        group: similarEdges.includes(link.id) ? "similar" : link.type === "link" ? "dissimilar" : link.group
    }))

    return { ...currentStandardVisualisation, nodes: newNodes, links: newLinks };
}

function CompareTwoGraphsVisualisation(props) {
    const classes = useStyles();
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

    //Determine graphFormatCode
    let graphFormatCode = null;
    switch (state.visualisationFormat) {
        case "1":
            graphFormatCode = "hierarchicalCompare";
            break;
        case "2":
            graphFormatCode = "treeCompare";
            break;
        case "3":
            graphFormatCode = "flatCompare";
            break;
        default:
            graphFormatCode = "hierarchicalCompare";
            break;
    }

    function handleSelectSide(side) {
        //Need to request visualisation from backend and store result
        setSelectSide(side);
        setOpen(true);
    }

    function handleClose() {
        setOpen(false);
    }

    function handleCompareClick(){

        let myHeaders = new Headers();
        myHeaders.append("X-USER", state.userID);

        let requestOptions = {
            method: 'GET',
            headers : myHeaders,
            redirect: 'follow'
        };

        fetch(state.APIendpoint +"/CompareGraphs?graphID1="+sentence1+"&graphID2="+sentence2+"&strict=false&noAbstract=false&noSurface=false", requestOptions)
            .then(response => {
                if (!response.ok) {
                    throw "Response not OK";
                }
                return response.text();
            })
            .then(result => {
                const jsonResult = JSON.parse(result);
                console.log(jsonResult); //debugging

                console.log("updateCompareGroups(compareVis1, jsonResult.SimilarNodes1, jsonResult.SimilarEdges1)",updateCompareGroups(compareVis1, jsonResult.SimilarNodes1, jsonResult.SimilarEdges1));
                console.log("updateCompareGroups(compareVis2, jsonResult.SimilarNodes2, jsonResult.SimilarEdges2)",updateCompareGroups(compareVis2, jsonResult.SimilarNodes2, jsonResult.SimilarEdges2));
                setCompareVis1(updateCompareGroups(compareVis1, jsonResult.SimilarNodes1, jsonResult.SimilarEdges1));
                setCompareVis2(updateCompareGroups(compareVis2, jsonResult.SimilarNodes2, jsonResult.SimilarEdges2));

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
        const [currentLength, setCurrentLength] = React.useState(state.dataSet.length);
        const [currentDataSet, setCurrentDataSet] = React.useState(state.dataSet);
        const history = useHistory();


        function requestSentenceFromBackend(sentenceId){

            console.log("requestSentenceFromBackend: ", sentenceId);

            let myHeaders = new Headers();
            myHeaders.append("X-USER", state.userID);

            let requestOptions = {
                method: 'GET',
                headers : myHeaders,
                redirect: 'follow'
            };

            dispatch({type: "SET_LOADING", payload: {isLoading: true}});

            fetch(state.APIendpoint + "/GetGraph?graphID=" + sentenceId, requestOptions)
                .then((response) => {
                    if (!response.ok) {
                        throw "Response not OK";
                    }
                    return response.text();
                })
                .then((result) => {
                    const jsonResult = JSON.parse(result);
                    console.log(jsonResult);

                    dispatch({type: "SET_LOADING", payload: {isLoading: false}});

                    let graphData = null;

                    switch (state.visualisationFormat) {
                        case "1":
                            graphData = layoutHierarchy(jsonResult);
                            break;
                        case "2":
                            graphData = layoutTree(jsonResult);
                            break;
                        case "3":
                            graphData = layoutFlat(jsonResult);
                            break;
                        default:
                            graphData = layoutHierarchy(jsonResult);
                            break;
                    }

                    if (selectSide === 1) {
                        setCompareVis1(graphData)
                    } else {
                        setCompareVis2(graphData)
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

        function search(value) {
            let found = [];
            for (let x of state.dataSet) {

                if (x.input.toLowerCase().trim().includes(value.toLowerCase().trim()) || x.id.includes(value.trim())) {
                    found.push(x);
                }
            }
            setCurrentLength(found.length);
            setCurrentDataSet(found);
        }
        return (
            <Grid item style={{width: "100%"}}>
                <Grid
                    container
                    direction="column"
                    justify="center"
                    alignItems="center"
                    style={{width: "100%"}}
                >
                    <Grid item style={{width:"100%"}}>
                        <TextField id="outlined-basic"
                                   label="Search for a Sentence or ID"
                                   variant="outlined"
                                   fullWidth
                                   onChange={e => (search(e.target.value))}/>
                    </Grid>
                    <Grid item style={{width:"100%"}}>
                        {state.dataSet === null ? (
                            <div>No data-set has been uploaded yet</div>
                        ) : (
                            <Virtuoso
                                style={{width: "100%", height: "400px"}}
                                totalCount={currentLength}
                                item={(index) => {
                                    return (
                                        <ListItem
                                            button
                                            key={currentDataSet[index].id}
                                            onClick={() => handleSelectSentence(currentDataSet[index].id)}
                                        >
                                            <Typography>{currentDataSet[index].input}</Typography>
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
                    </Grid>
                <Divider/>
                </Grid>
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
                <Grid item style={{width: "100%", height: "50vh"}}>
                    {
                        compareVis1 === null ? <div>Please select a sentence</div> :

                            <div className={classes.graphDiv}>
                                <ParentSize>
                                    {parent => (
                                        <Graph
                                            width={parent.width}
                                            height={parent.height}
                                            graph={compareVis1}
                                            adjacentLinks={determineAdjacentLinks(compareVis1)}
                                            graphFormatCode={graphFormatCode}
                                        />
                                    )}
                                </ParentSize>
                            </div>
                    }
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
                <Grid item style={{width: "100%", height: "50vh"}}>
                    {
                        compareVis2 === null ? <div>Please select a sentence</div> :

                            <div className={classes.graphDiv}>
                                <ParentSize>
                                    {parent => (
                                        <Graph
                                            width={parent.width}
                                            height={parent.height}
                                            graph={compareVis2}
                                            adjacentLinks={determineAdjacentLinks(compareVis2)}
                                            graphFormatCode={graphFormatCode}
                                        />
                                    )}
                                </ParentSize>
                            </div>
                    }
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
