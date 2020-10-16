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

const useStyles = makeStyles((theme) => ({
    root: {
        width: "80%",
        "& > * + *": {
            marginTop: theme.spacing(3)
        }
    },
    autoComplete: {
        marginBottom: 10
    }
}));

function SearchSubgraphPatternTool(props) {
    const classes = useStyles();

    const [nodeSet, setNodeSet] = React.useState(null); //Local state of entered node labels
    const {state, dispatch} = useContext(AppContext); //Provide access to global state
    const history = useHistory(); //Use routing history
    const [open, setOpen] = React.useState(false); //Local state for search sub-graph pattern dialog
    const [openNodeSet, setOpenNodeSet] = React.useState(false); //Local state for search node set dialog
    const [nodeSetResult, setNodeSetResult] = React.useState(null); //Local state for node set search result
    const [nodeSetResultSearch, setNodeSetResultSearch] = React.useState(nodeSetResult); //Local state for node set search result
    const [subgraphResponse, setSubgraphResponse] = React.useState(null); //Local state for subgraph pattern search results
    const [responseMessage, setResponseMessage] = React.useState(null); //Local state for response message
    const [alertOpen, setAlertOpen] = React.useState(false); //Local state for error alert

    //Handle click open for search sub-graph pattern dialog
    const handleClickOpen = () => {
        setOpen(true);
    };

    //Handle close for search sub-graph pattern dialog
    const handleClose = () => {
        setOpen(false);
    };

    //Handle close for error alert
    const handleAlertClose = () => {
        setAlertOpen(false);
    };

    //Handle close for node set dialog
    const handleCloseNodeSet = () => {
        setOpenNodeSet(false);
        setNodeSetResult(null);
        setSubgraphResponse(null);
    };

    function search(value) {
        let found = [];
        for (let x of nodeSetResult) {

            if (x.input.toLowerCase().trim().includes(value.toLowerCase().trim()) || x.id.includes(value.trim())) {
                found.push(x);
            }
        }
        setNodeSetResultSearch(found);

    }

    //Handle search for node set from backend
    function handleSearchForNodeSet() {
        console.log(nodeSet.join(","));
        let requestOptions = {
            method: 'GET',
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
                setResponseMessage(jsonResult.response);
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation

                if(jsonResult.response === "Success"){
                    setOpenNodeSet(true); //Display the node set results
                }else{
                    setAlertOpen(true); //Display the error alert
                }

            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation
                console.log("error", error); //Log the error
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

        let requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        dispatch({type: "SET_LOADING", payload: {isLoading: true}}); //Show the loading animation

        fetch(state.APIendpoint + "/Visualise?graphID=" + sentenceId + "&format=" + state.visualisationFormat, requestOptions)
            .then((response) => {
                if (!response.ok) {
                    throw "Response not OK";
                }
                return response.text();
            })
            .then((result) => {
                const jsonResult = JSON.parse(result);
                console.log(jsonResult); //Debugging
                setSubgraphResponse(jsonResult); //Store graph visualisation result
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation
            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation
                console.log("error", error); //Log the error to console
                history.push("/404"); //Take the user to the error page
            });
    }

    return (
        <React.Fragment>
            <Card variant="outlined" style={{marginBottom: "10px"}}>
                <CardContent>
                    <Grid item style={{width: "100%"}}>
                        <Typography>Search for a set of node labels:</Typography>
                        <Autocomplete
                            style={{width: "100%"}}
                            disabled={state.selectedSentenceID === null}
                            multiple
                            disableCloseOnSelect
                            freeSolo
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
                                    variant="standard"
                                    label="Select Node Labels or Enter Your Own"
                                />
                            )}
                        />
                    </Grid>
                    <Grid item style={{width: "100%"}}>
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleSearchForNodeSet}
                            style={{marginBottom: 10, marginTop: 10}}
                            disabled={nodeSet === null || nodeSet.length === 0}
                        >
                            Search for selected node labels
                        </Button>
                    </Grid>
                </CardContent>
                <Dialog
                    open={openNodeSet}
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
                            style={{width:"100%", height:"100%"}}
                            container
                            direction="column"
                            justify="center"
                            alignItems="center"
                            spacing={2}
                        >
                            <Grid item style={{width:"100%"}}>
                                <TextField id="outlined-basic"
                                           label="Search for a Sentence or ID"
                                           variant="outlined"
                                           fullWidth
                                           onChange={e => (search(e.target.value))}/>
                            </Grid>
                            <Grid container item xs={12}>
                                <Card variant="outlined" style={{width:"100%", height: "15vh"}}>

                                    <CardContent style={{width:"100%", height: "100%"}}>

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
                                                        <Typography>{nodeSetResultSearch[index].input}</Typography>
                                                    </ListItem>
                                                );
                                            }}
                                            footer={() => (
                                                <div style={{padding: "1rem", textAlign: "center"}}>
                                                    -- end of dataset --
                                                </div>
                                            )}
                                        />}
                                    </CardContent>
                                </Card>
                            </Grid>
                            <Grid container item xs={12}>
                                <Card variant="outlined" style={{width:"100%", height: "40vh"}}>
                                    <CardContent style={{width:"100%", height: "100%"}}>
                                        {subgraphResponse === null ?

                                                        <Typography>Select
                                                            a sentence from the results above.</Typography>

                                            :

                                                        <SubgraphVisualisation
                                                            subgraphGraphData={subgraphResponse}/>

                                        }
                                    </CardContent>
                                </Card>
                            </Grid>
                        </Grid>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleCloseNodeSet} color="primary" autoFocus>
                            Close
                        </Button>
                    </DialogActions>
                </Dialog>
            </Card>
            <Card variant="outlined">
                <CardContent><Grid item style={{width: "100%"}}>
                    <Typography>
                        Or visually select a sub-graph pattern on the currently displayed
                        graph:
                    </Typography>
                    <Button
                        variant="contained"
                        color="primary"
                        endIcon={<LocationSearchingIcon/>}
                        onClick={handleClickOpen}
                        style={{marginBottom: 10, marginTop: 10}}
                        disabled={state.selectedSentenceID === null}
                    >
                        Select sub-graph pattern
                    </Button>
                </Grid>
                    <Dialog
                        open={open}
                        fullWidth={true}
                        maxWidth="xl"
                        onClose={handleClose}
                        aria-labelledby="alert-dialog-title"
                        aria-describedby="alert-dialog-description"
                    >
                        <DialogTitle id="alert-dialog-title">
                            {"Select connected nodes and edges on the graph: "}
                        </DialogTitle>
                        <DialogContent>
                            <SelectSubgraphVisualisation/>
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={handleClose} color="primary" autoFocus>
                                Close
                            </Button>
                        </DialogActions>
                    </Dialog>
                </CardContent>
            </Card>
            <Snackbar open={alertOpen} autoHideDuration={6000} onClose={handleAlertClose}>
                <MuiAlert elevation={6} variant="filled" onClose={handleAlertClose} severity="error">
                    Error: Could not search for node labels
                </MuiAlert>
            </Snackbar>
        </React.Fragment>


    );
}

export default SearchSubgraphPatternTool;
