import React, {useContext, useState} from "react";
import Chip from "@material-ui/core/Chip";
import Autocomplete from "@material-ui/lab/Autocomplete";
import {makeStyles} from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import Grid from "@material-ui/core/Grid";
import {CardContent, Typography} from "@material-ui/core";
import LocationSearchingIcon from "@material-ui/icons/LocationSearching";
import Button from "@material-ui/core/Button";

import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";

import {useHistory} from "react-router-dom";
import {AppContext} from "../../Store/AppContextProvider";
import Card from "@material-ui/core/Card";
import ListItem from "@material-ui/core/ListItem";
import {Virtuoso} from "react-virtuoso";
import SelectSubgraphVisualisation from "../Main/SelectSubgraphVisualisation";
import SubgraphVisualisation from "../Main/SubgraphVisualisation";
import MuiAlert from "@material-ui/lab/Alert";
import Snackbar from "@material-ui/core/Snackbar";
import {ParentSize} from "@visx/responsive";
import {Graph} from "../Graph/Graph";
import {determineAdjacentLinks, layoutHierarchy} from "../../LayoutAlgorithms/layoutHierarchy";
import {layoutTree} from "../../LayoutAlgorithms/layoutTree";
import {layoutFlat} from "../../LayoutAlgorithms/layoutFlat";

const useStyles = makeStyles((theme) => ({
    root: {
        width: "80%",
        "& > * + *": {
            marginTop: theme.spacing(3)
        }
    },
    autoComplete: {
        marginBottom: 10
    },
    graphDiv: {
        height: "100%",
        //border: "1px solid red",
        flex: "1",
        width: "100%"
    }
}));

function SearchSubgraphPatternTool(props) {
    const classes = useStyles();
    const {state, dispatch} = useContext(AppContext); //Provide access to global state
    const history = useHistory(); //Use routing history

    //Node set state
    const [nodeSet, setNodeSet] = React.useState(null); //Local state of entered node labels
    const [openNodeSetDialog, setOpenNodeSetDialog] = React.useState(false); //Local state for search node set dialog
    const [nodeSetResult, setNodeSetResult] = React.useState(null); //Local state for node set search result
    const [nodeSetResultSearch, setNodeSetResultSearch] = React.useState(nodeSetResult); //Local state for node set search result

    //Subgraph state
    const [openSelectSubgraphDialog, setOpenSelectSubgraphDialog] = React.useState(false); //Local state for search sub-graph pattern dialog
    const [openSubgraphResultsDialog, setOpenSubgraphResultsDialog] = React.useState(false); //Local state for search sub-graph pattern results dialog
    const [subgraphResult, setSubgraphResult] = React.useState(null); //Local state for subgraph search result
    const [subgraphResultSearch, setSubgraphResultSearch] = React.useState(null); //Local state for subgraph search result

    const [subgraphResponse, setSubgraphResponse] = React.useState(null); //Local state for subgraph pattern search results


    // const [responseMessage, setResponseMessage] = React.useState(null); //Local state for response message


    const [alertOpen, setAlertOpen] = React.useState(false); //Local state for error alert

    const [selectedNodes, setSelectedNodes] = React.useState(null); //Store local state for currently selected node ids
    const [selectedLinks, setSelectedLinks] = React.useState(null); //Store local state for currently selected link ids

    let graphFormatCode = null;
    switch (state.visualisationFormat) {
        case "1":
            graphFormatCode = "hierarchical";
            break;
        case "2":
            graphFormatCode = "tree";
            break;
        case "3":
            graphFormatCode = "flat";
            break;
        default:
            graphFormatCode = "hierarchical";
            break;
    }

    //Handle click open for search sub-graph pattern dialog
    const handleClickSelectSubgraph = () => {
        setOpenSelectSubgraphDialog(true);
    };

    //Handle close for search sub-graph pattern dialog
    const handleCloseSelectSubgraphDialog = () => {
        setOpenSelectSubgraphDialog(false);
    };

    //Handle close for error alert
    const handleAlertClose = () => {
        setAlertOpen(false);
    };

    //Handle close for node set dialog
    const handleCloseNodeSet = () => {
        setOpenNodeSetDialog(false);
        setNodeSetResult(null);
        setSubgraphResponse(null);
    };

    //Handle close for subgraph results dialog
    const handleCloseSubgraphResultsDialog = () => {
        setOpenSubgraphResultsDialog(false);
        setSubgraphResult(null);
        setSubgraphResponse(null);
    };

    function searchNodeSet(value) {
        let found = [];
        for (let x of nodeSetResult) {

            if (x.input.toLowerCase().trim().includes(value.toLowerCase().trim()) || x.id.includes(value.trim())) {
                found.push(x);
            }
        }
        setNodeSetResultSearch(found);
    }

    function searchSubgraph(value) {
        let found = [];
        for (let x of subgraphResult) {

            if (x.input.toLowerCase().trim().includes(value.toLowerCase().trim()) || x.id.includes(value.trim())) {
                found.push(x);
            }
        }
        setSubgraphResultSearch(found);
    }

    //Handle search for node set from backend
    function handleSearchForNodeSet() {
        console.log(nodeSet.join(","));
        let myHeaders = new Headers();
        myHeaders.append("X-USER", state.userID);

        let requestOptions = {
            method: 'GET',
            headers: myHeaders,
            redirect: 'follow'
        };

        dispatch({type: "SET_LOADING", payload: {isLoading: true}}); //Show loading animation

        fetch(state.APIendpoint + "/SearchSubgraphNodeSet?labels=" + nodeSet.join(","), requestOptions)
            .then((response) => {
                if (!response.ok) {
                    throw "Response not OK";
                }
                return response.text();
            })
            .then((result) => {
                const jsonResult = JSON.parse(result);
                console.log(jsonResult); //Debugging

                setNodeSetResult(jsonResult.data);
                setNodeSetResultSearch(jsonResult.data)//Store the node set results

                // setResponseMessage(jsonResult.response);

                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation

                if (jsonResult.response === "Success") {
                    setOpenNodeSetDialog(true); //Display the node set results
                } else {
                    setAlertOpen(true); //Display the error alert
                }

            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation
                console.log("error", error); //Log the error
                history.push("/404"); //Take the user to the error page
            });

    }

    function searchForSelectedSubgraph() {
        let myHeaders = new Headers();
        myHeaders.append("X-USER", state.userID);

        let requestOptions = {
            method: 'GET',
            headers: myHeaders,
            redirect: 'follow'
        };

        dispatch({type: "SET_LOADING", payload: {isLoading: true}}); //Show the loading animation

        //Search the backend for matches
        fetch(state.APIendpoint + "/SearchSubgraphPattern?graphID=" + state.selectedSentenceID + "&NodeID=" + selectedNodes.join(",") + "&EdgeIndices=" + selectedLinks.join(","), requestOptions)
            .then((response) => {
                if (!response.ok) {
                    throw "Response not OK";
                }
                return response.text();
            })
            .then((result) => {
                const jsonResult = JSON.parse(result);
                console.log(jsonResult); //Debugging

                setSubgraphResult(jsonResult.data);
                setSubgraphResultSearch(jsonResult.data)//Store the subgraph results
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation

                if (jsonResult.response === "Success") {
                    setOpenSubgraphResultsDialog(true); //Show the results to the user
                } else {
                    setAlertOpen(true); //Display the error alert
                }

            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation
                console.log("error", error); //Log the error to console
                history.push("/404"); //Take the user to the error page
            });

    }

    //Get just the labels from all the nodes of the currently displayed graph
    function getLabels(sentence) {
        if (state.selectedSentenceID !== null) {
            let graph = sentence;

            let labelOptions = [];

            graph.nodes.forEach((node) => {
                if (node.group !== "token") {
                    labelOptions.push(node.label);
                }
            });

            return labelOptions;
        } else {
            return [];
        }
    }

    //Handle when user selects one of the sentences returned in the results from the backend
    function handleClickSentenceResult(sentenceId) {
        console.log(sentenceId); //Debugging
        let myHeaders = new Headers();
        myHeaders.append("X-USER", state.userID);
        let requestOptions = {
            method: 'GET',
            headers: myHeaders,
            redirect: 'follow'
        };

        dispatch({type: "SET_LOADING", payload: {isLoading: true}}); //Show the loading animation

        fetch(state.APIendpoint + "/GetGraph?graphID=" + sentenceId, requestOptions)
            .then((response) => {
                if (!response.ok) {
                    throw "Response not OK";
                }
                return response.text();
            })
            .then((result) => {
                const jsonResult = JSON.parse(result);
                console.log(jsonResult); //Debugging

                let graphData = null;

                switch (state.visualisationFormat) {
                    case "1":
                        graphData = layoutHierarchy(jsonResult, state.graphLayoutSpacing);
                        break;
                    case "2":
                        graphData = layoutTree(jsonResult, state.graphLayoutSpacing);
                        break;
                    case "3":
                        graphData = layoutFlat(jsonResult,false, state.graphLayoutSpacing);
                        break;
                    default:
                        graphData = layoutHierarchy(jsonResult, state.graphLayoutSpacing);
                        break;
                }

                setSubgraphResponse(graphData); //Store graph visualisation result
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation
            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation
                console.log("error", error); //Log the error to console
                history.push("/404"); //Take the user to the error page
            });
    }

    const events = {
        select: {
            selectMode: "subgraph",
            selectedNodesStateSetter: setSelectedNodes,
            selectedLinksStateSetter: setSelectedLinks,
        }
    }

    return (
        <React.Fragment>
            <Card variant="outlined" style={{marginBottom: "10px"}}>
                <CardContent>
                    <Grid item style={{width: "100%"}}>
                        <Typography color={"textPrimary"}>Search for a set of node labels:</Typography>
                        <Autocomplete
                            style={{width: "100%"}}
                            disabled={state.selectedSentenceID === null}
                            multiple
                            disableCloseOnSelect
                            freeSolo
                            color={"textPrimary"}
                            onChange={(event, values) => setNodeSet(values)}
                            id="tags-standard"
                            options={getLabels(state.selectedSentenceVisualisation)}
                            renderTags={(value, getTagProps) =>
                                value.map((option, index) => (
                                    <Chip

                                        variant="outlined"
                                        label={option}
                                        {...getTagProps({index})}
                                    />
                                ))
                            }
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    color={"textPrimary"}
                                    variant="standard"
                                    label="Select Node Labels or Enter Your Own"
                                />
                            )}
                        />
                    </Grid>
                    <Grid item style={{width: "100%"}}>
                        <Button
                            variant="contained"
                            color={"secondary"}
                            onClick={handleSearchForNodeSet}
                            style={{marginBottom: 10, marginTop: 10}}
                            disabled={nodeSet === null || nodeSet.length === 0}
                        >
                            Search for selected node labels
                        </Button>
                    </Grid>
                </CardContent>
                <Dialog
                    open={openNodeSetDialog}
                    fullWidth={true}
                    maxWidth="xl"
                    onClose={handleCloseNodeSet}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">
                        {"Search Results:"}
                    </DialogTitle>
                    <DialogContent>
                        <Grid
                            style={{width: "100%", height: "100%"}}
                            container
                            direction="column"
                            justify="center"
                            alignItems="center"
                            spacing={2}
                        >
                            <Grid item style={{width: "100%"}}>
                                <TextField id="outlined-basic"
                                           label="Search for a Sentence or ID"
                                           variant="outlined"
                                           fullWidth
                                           onChange={e => (searchNodeSet(e.target.value))}/>
                            </Grid>
                            <Grid container item xs={12}>
                                <Card variant="outlined" style={{width: "100%", height: "15vh"}}>

                                    <CardContent style={{width: "100%", height: "100%"}}>

                                        {nodeSetResultSearch &&
                                        <Virtuoso
                                            style={{width: "100%", height: "100%"}}
                                            totalCount={nodeSetResultSearch.length}
                                            item={(index) => {
                                                return (
                                                    <ListItem
                                                        button
                                                        key={index}
                                                        onClick={() => {
                                                            handleClickSentenceResult(nodeSetResultSearch[index].id);
                                                        }}
                                                    >
                                                        <Typography
                                                            color={"textPrimary"}>{nodeSetResultSearch[index].input}</Typography>
                                                    </ListItem>
                                                );
                                            }}
                                            footer={() => (
                                                <div style={{padding: "1rem", textAlign: "center"}}>
                                                    <Typography color={"textPrimary"}>-- end of dataset --</Typography>
                                                </div>
                                            )}
                                        />}
                                    </CardContent>
                                </Card>
                            </Grid>
                            <Grid container item xs={12}>
                                <Card variant="outlined" style={{width: "100%", height: "45vh"}}>
                                    <CardContent style={{width: "100%", height: "100%"}}>
                                        {subgraphResponse === null ?

                                            <Typography color={"textPrimary"}>Select
                                                a sentence from the results above.</Typography>

                                            :

                                            <div className={classes.graphDiv}>
                                                <ParentSize>
                                                    {parent => (
                                                        <Graph
                                                            width={parent.width}
                                                            height={parent.height}
                                                            graph={subgraphResponse}
                                                            adjacentLinks={determineAdjacentLinks(subgraphResponse)}
                                                            graphFormatCode={graphFormatCode}
                                                        />
                                                    )}
                                                </ParentSize>
                                            </div>

                                        }
                                    </CardContent>
                                </Card>
                            </Grid>
                        </Grid>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleCloseNodeSet} color={"secondary"} autoFocus>
                            Close
                        </Button>
                    </DialogActions>
                </Dialog>
            </Card>
            <Card variant="outlined">
                <CardContent>
                    <Grid item style={{width: "100%"}}>
                        <Typography color={"textPrimary"}>
                            Or visually select a sub-graph pattern on the currently displayed
                            graph:
                        </Typography>
                        <Button
                            variant="contained"
                            color={"secondary"}
                            endIcon={<LocationSearchingIcon/>}
                            onClick={handleClickSelectSubgraph}
                            style={{marginBottom: 10, marginTop: 10}}
                            disabled={state.selectedSentenceID === null}
                        >
                            Select sub-graph pattern
                        </Button>
                    </Grid>
                    <Dialog
                        open={openSelectSubgraphDialog}
                        fullWidth={true}
                        maxWidth="xl"
                        onClose={handleCloseSelectSubgraphDialog}
                        aria-labelledby="alert-dialog-title"
                        aria-describedby="alert-dialog-description"
                    >
                        <DialogTitle id="alert-dialog-title">
                            {"Select connected nodes and edges on the graph: "}
                        </DialogTitle>
                        <DialogContent style={{height: "80vh"}}>
                            <div className={classes.graphDiv}>
                                <ParentSize>
                                    {parent => (
                                        <Graph
                                            width={parent.width}
                                            height={parent.height}
                                            graph={state.selectedSentenceVisualisation}
                                            adjacentLinks={determineAdjacentLinks(state.selectedSentenceVisualisation)}
                                            graphFormatCode={graphFormatCode}
                                            events={events}
                                        />
                                    )}
                                </ParentSize>
                            </div>
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={searchForSelectedSubgraph} color="primary" autoFocus
                                    disabled={selectedNodes === null || selectedLinks === null || selectedNodes.length === 0 || selectedLinks.length === 0}>
                                Display
                            </Button>
                        </DialogActions>
                    </Dialog>
                    <Dialog
                        open={openSubgraphResultsDialog}
                        fullWidth={true}
                        maxWidth="xl"
                        onClose={handleCloseSubgraphResultsDialog}
                        aria-labelledby="alert-dialog-title"
                        aria-describedby="alert-dialog-description"
                    >
                        <DialogTitle id="alert-dialog-title">
                            {"Search Results:"}
                        </DialogTitle>
                        <DialogContent>
                            <Grid
                                style={{width: "100%", height: "100%"}}
                                container
                                direction="column"
                                justify="center"
                                alignItems="center"
                                spacing={2}
                            >
                                <Grid item style={{width: "100%"}}>
                                    <TextField id="outlined-basic"
                                               label="Search for a Sentence or ID"
                                               variant="outlined"
                                               fullWidth
                                               onChange={e => (searchSubgraph(e.target.value))}/>
                                </Grid>
                                <Grid container item xs={12}>
                                    <Card variant="outlined" style={{width: "100%", height: "15vh"}}>
                                        <CardContent style={{width: "100%", height: "100%"}}>
                                            {subgraphResultSearch &&
                                            <Virtuoso
                                                style={{width: "100%", height: "100%"}}
                                                totalCount={subgraphResultSearch.length}
                                                item={(index) => {
                                                    return (
                                                        <ListItem
                                                            button
                                                            key={index}
                                                            onClick={() => {
                                                                handleClickSentenceResult(subgraphResultSearch[index].id);
                                                            }}
                                                        >
                                                            <Typography
                                                                color={"textPrimary"}>{subgraphResultSearch[index].input}</Typography>
                                                        </ListItem>
                                                    );
                                                }}
                                                footer={() => (
                                                    <div style={{padding: "1rem", textAlign: "center"}}>
                                                        <Typography color={"textPrimary"}> -- end of dataset
                                                            -- </Typography>
                                                    </div>
                                                )}
                                            />}
                                        </CardContent>
                                    </Card>
                                </Grid>
                                <Grid container item xs={12}>
                                    <Card variant="outlined" style={{width: "100%", height: "45vh"}}>
                                        <CardContent style={{width: "100%", height: "100%"}}>
                                            {subgraphResponse=== null ?

                                                <Typography color={"textPrimary"}>Select
                                                    a sentence from the results above.</Typography>

                                                :
                                                    <div className={classes.graphDiv}>
                                                        <ParentSize>
                                                            {parent => (
                                                                <Graph
                                                                    width={parent.width}
                                                                    height={parent.height}
                                                                    graph={subgraphResponse}
                                                                    adjacentLinks={determineAdjacentLinks(subgraphResponse)}
                                                                    graphFormatCode={graphFormatCode}
                                                                />
                                                            )}
                                                        </ParentSize>
                                                    </div>
                                            }
                                        </CardContent>
                                    </Card>
                                </Grid>
                            </Grid>
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={() => setOpenSubgraphResultsDialog(false)} color="secondary" autoFocus>
                                Close
                            </Button>
                        </DialogActions>
                    </Dialog>
                </CardContent>
            </Card>

            <Snackbar open={alertOpen} autoHideDuration={6000} onClose={handleAlertClose}>
                <MuiAlert elevation={6} variant="filled" onClose={handleAlertClose} severity="error">
                    Error: Could not search for subgraph
                </MuiAlert>
            </Snackbar>
        </React.Fragment>


    );
}

export default SearchSubgraphPatternTool;
