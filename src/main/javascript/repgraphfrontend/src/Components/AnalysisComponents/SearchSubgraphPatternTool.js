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
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogActions from "@material-ui/core/DialogActions";

import {useHistory} from "react-router-dom";
import {AppContext} from "../../Store/AppContextProvider";
import Divider from "@material-ui/core/Divider";
import Card from "@material-ui/core/Card";
import {node} from "prop-types";
import ListItem from "@material-ui/core/ListItem";
import {Virtuoso} from "react-virtuoso";
import SelectSubgraphVisualisation from "../Main/SelectSubgraphVisualisation";
import SubgraphVisualisation from "../Main/SubgraphVisualisation";

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

    const [nodeSet, setNodeSet] = React.useState(null);
    const {state, dispatch} = useContext(AppContext);
    const history = useHistory();
    const [open, setOpen] = React.useState(false);
    const [openNodeSet, setOpenNodeSet] = React.useState(false);
    const [nodeSetResult, setNodeSetResult] = React.useState(null);
    const [subgraphResponse, setSubgraphResponse] = React.useState(null);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    const handleCloseNodeSet = () => {
        setOpenNodeSet(false);
    };

    function handleSearchForNodeSet() {
        console.log(nodeSet.join(","));
        let requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        dispatch({type: "SET_LOADING", payload: {isLoading: true}});

        fetch(state.APIendpoint + "/SearchSubgraphNodeSet?labels=" + nodeSet.join(","), requestOptions)
            .then((response) => response.text())
            .then((result) => {
                const jsonResult = JSON.parse(result);
                console.log(jsonResult);
                setNodeSetResult(jsonResult.data); //Store the results
                setOpenNodeSet(true); //Display the results
                dispatch({type: "SET_LOADING", payload: {isLoading: false}});
            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}});
                console.log("error", error);
                history.push("/404");
            });

    }

    function getLabels(sentence) {
        if (state.selectedSentenceID !== null) {
            let graph = sentence;

            let labelOptions = [];

            graph.nodes.forEach((node) => {
                if (node.group !== "token") {
                    labelOptions.push(node.label);
                }
            });
            //console.log(labelOptions); //debugging
            return labelOptions;
        } else {
            return [];
        }
    }

    function handleClickSentenceResult(sentenceId) {
        console.log(sentenceId);

        let requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        dispatch({type: "SET_LOADING", payload: {isLoading: true}});

        fetch(state.APIendpoint + "/Visualise?graphID=" + sentenceId + "&format=" + state.visualisationFormat, requestOptions)
            .then((response) => response.text())
            .then((result) => {

                const jsonResult = JSON.parse(result);
                console.log(jsonResult);
                //console.log(jsonResult);
                //console.log(jsonResult.response);
                //const formattedGraph = layoutGraph(jsonResult);
                setSubgraphResponse(jsonResult);
                dispatch({type: "SET_LOADING", payload: {isLoading: false}});
            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}});
                console.log("error", error);
                history.push("/404"); //for debugging

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
                    <DialogContent style={{height: "80vh"}}>
                        {nodeSetResult && <Virtuoso
                            style={{width: "100%", height: "30%"}}
                            totalCount={nodeSetResult.length}
                            item={(index) => {
                                return (
                                    <ListItem
                                        button
                                        key={index}
                                        onClick={() => {
                                            handleClickSentenceResult(nodeSetResult[index].id);
                                        }}
                                    >
                                        <Typography>{nodeSetResult[index].input}</Typography>
                                    </ListItem>
                                );
                            }}
                            footer={() => (
                                <div style={{padding: "1rem", textAlign: "center"}}>
                                    -- end of dataset --
                                </div>
                            )}
                        />}
                        {subgraphResponse === null ?
                            <Grid
                                container
                                direction="column"
                                justify="center"
                                alignItems="center">
                                <Grid item>
                                    <Card style={{height: "100%", width: "100%"}} variant="outlined"><CardContent><Typography>Select
                                        a sentence from the results above.</Typography></CardContent></Card>
                                </Grid>
                            </Grid>
                             :
                            <SubgraphVisualisation subgraphGraphData={subgraphResponse}/>
                        }

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
                        onClick={() => setOpen(true)}
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
                            {"Select a node on the graph:"}
                        </DialogTitle>
                        <DialogContent>
                            <SelectSubgraphVisualisation/>
                        </DialogContent>
                        <DialogActions>
                            <Chip
                                label={`Selected Nodes and Edges: still need to get this working`}
                            />
                            <Button onClick={handleClose} color="primary" autoFocus>
                                Close
                            </Button>
                        </DialogActions>
                    </Dialog>

                </CardContent>

            </Card>
        </React.Fragment>


    );
}

export default SearchSubgraphPatternTool;
