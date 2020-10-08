import React, {useContext} from 'react';
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/core/styles";
import FormLabel from "@material-ui/core/FormLabel";
import FormControl from "@material-ui/core/FormControl";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Radio from "@material-ui/core/Radio";
import ArrowForwardIcon from "@material-ui/icons/ArrowForward";
import Button from "@material-ui/core/Button";
import LocationSearchingIcon from '@material-ui/icons/LocationSearching';
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogActions from "@material-ui/core/DialogActions";

import { AppContext } from "../../Store/AppContextProvider.js";
import {useHistory} from "react-router-dom";
import GraphVisualisation from "../Main/GraphVisualisation";
import Chip from "@material-ui/core/Chip";

import {layoutGraph} from "../App";
import SubsetVisualisation from "../Main/SubsetVisualisation";

const useStyles = makeStyles((theme) => ({
    root: {
        width: "100%",
        marginBottom: 10
    }

}));


function DisplaySubsetTool(props){
    const { state, dispatch } = useContext(AppContext);
    const [subsetResponse, setSubsetResponse] = React.useState(null);

    const history = useHistory();

    const [open, setOpen] = React.useState(false);
    const [resultOpen, setResultOpen] = React.useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    const handleResultClose = () => {
        setResultOpen(false);
    };

    const [value, setValue] = React.useState('adjacent');

    const handleChange = (event) => {
        setValue(event.target.value);
    };

    const classes = useStyles();

    function handleDisplaySubset(){
        setOpen(false);

        let requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        console.log(state.selectedSentenceID, state.selectedNodeAndEdges.nodes[0], value);
        dispatch({ type: "SET_LOADING", payload: { isLoading: true } });

        fetch(state.APIendpoint+"/DisplaySubset?graphID="+state.selectedSentenceID+"&NodeID="+state.selectedNodeAndEdges.nodes[0]+"&SubsetType="+value, requestOptions)
            .then((response) => response.text())
            .then((result) => {
                console.log(result);//debugging
                console.log(JSON.parse(result));//debugging
                const jsonResult = JSON.parse(result);

                const formattedGraph = layoutGraph(jsonResult); //will remove this step before final

                //dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: formattedGraph}});
                //dispatch({type: "SET_SELECTED_SENTENCE_ID", payload: {selectedSentenceID: jsonResult.id}});
                setSubsetResponse(formattedGraph);
                setResultOpen(true);
                dispatch({ type: "SET_LOADING", payload: { isLoading: false } });

            })
            .catch((error) => {
                dispatch({ type: "SET_LOADING", payload: { isLoading: false } });
                console.log("error", error);
                history.push("/404"); //for debugging
            });
    }

    return(
        <Grid
            container
            direction="column"
            justify="center"
            alignItems="center"
            className={classes.root}
        >
            <FormControl component="fieldset">
                <FormLabel component="legend">Select Type of Subset</FormLabel>
                <RadioGroup aria-label="subset-type" name="subset" value={value} onChange={handleChange}>
                    <FormControlLabel value="adjacent" control={<Radio color="primary"/>} label="Display Adjacent Nodes" />
                    <FormControlLabel value="descendent" control={<Radio color="primary"/>} label="Display Descendent Nodes" />
                </RadioGroup>
                <Button
                    variant="contained" color="primary" onClick={handleClickOpen}
                    endIcon={<LocationSearchingIcon/>}
                    disabled={state.selectedSentenceID === null}
                >
                    Select Subset
                </Button>
                <Dialog
                    open={open}
                    fullWidth={true}
                    maxWidth="xl"
                    onClose={handleClose}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">{"Select a node on the graph:"}</DialogTitle>
                    <DialogContent style={{height:"80vh"}}>
                        {state.selectedSentenceID === null ? (
                            <div>Select a sentence first.</div>
                        ) : (
                            <GraphVisualisation />
                        )}
                    </DialogContent>
                    <DialogActions>
                        <Chip label= {`Selected Node: ${JSON.stringify(state.selectedNodeAndEdges)}`} />
                        <Button onClick={handleDisplaySubset} color="primary" autoFocus >
                            Display
                        </Button>
                    </DialogActions>
                </Dialog>
                <Dialog
                    open={resultOpen}
                    fullWidth={true}
                    maxWidth="xl"
                    onClose={handleResultClose}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">{"Subset result based upon node selection:"}</DialogTitle>
                    <DialogContent style={{height:"80vh"}}>
                        {subsetResponse === null ? (
                            <div>No result to display</div>
                        ) : (
                            <SubsetVisualisation subsetGraphData={subsetResponse}/>
                        )}
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleResultClose} color="primary" autoFocus >
                            close
                        </Button>
                    </DialogActions>
                </Dialog>
            </FormControl>
        </Grid>
    );

}

export default DisplaySubsetTool;
