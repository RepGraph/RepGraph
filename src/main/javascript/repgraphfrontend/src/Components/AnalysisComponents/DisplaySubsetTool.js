import React, {useContext} from 'react';
import {makeStyles} from "@material-ui/core/styles";
import FormLabel from "@material-ui/core/FormLabel";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Radio from "@material-ui/core/Radio";
import Button from "@material-ui/core/Button";
import LocationSearchingIcon from '@material-ui/icons/LocationSearching';
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";

import {AppContext} from "../../Store/AppContextProvider.js";
import {useHistory} from "react-router-dom";
import GraphVisualisation from "../Main/GraphVisualisation";
import Chip from "@material-ui/core/Chip";

import SubsetVisualisation from "../Main/SubsetVisualisation";
import CardContent from "@material-ui/core/CardContent";

const useStyles = makeStyles((theme) => ({
    root: {
        height: "100%",
        width: "100%",
    }
}));

function DisplaySubsetTool(props) {
    const {state, dispatch} = useContext(AppContext); //Provide access to global state
    const [subsetResponse, setSubsetResponse] = React.useState(null); //Store selected subset response in local state

    const history = useHistory(); //Use history for page routing

    const [open, setOpen] = React.useState(false); //Store local state for whether dialog is open or not
    const [resultOpen, setResultOpen] = React.useState(false); //Store local state for whether result dialog is open or not

    //Handle click open for first dialog to select subset
    const handleClickOpen = () => {
        setOpen(true);
    };
    //Handle close for first dialog to select subset
    const handleClose = () => {
        setOpen(false);
    };

    //handle close for second dialog to display subset result
    const handleResultClose = () => {
        setResultOpen(false);
    };

    const [value, setValue] = React.useState('adjacent'); //Store subset type in local state

    //Handle change of selected subset type
    const handleChange = (event) => {
        setValue(event.target.value);
    };

    const classes = useStyles();

    function handleDisplaySubset() {
        setOpen(false);

        let requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        console.log(state.selectedSentenceID, state.selectedNodeAndEdges.nodes[0], value); //Debugging
        dispatch({type: "SET_LOADING", payload: {isLoading: true}}); //Show loading animation

        console.log(state.visualisationFormat); //Debugging

        console.log(state.APIendpoint + "/DisplaySubset?graphID=" + state.selectedSentenceID + "&NodeID=" + state.selectedNodeAndEdges.nodes[0] + "&SubsetType=" + value +"&format="+state.visualisationFormat); //Debugging

        //Request subset from backend
        fetch(state.APIendpoint + "/DisplaySubset?graphID=" + state.selectedSentenceID + "&NodeID=" + state.selectedNodeAndEdges.nodes[0] + "&SubsetType=" + value +"&format="+state.visualisationFormat, requestOptions)
            .then((response) => response.text())
            .then((result) => {
                console.log(result);//debugging
                console.log(JSON.parse(result));//debugging
                const jsonResult = JSON.parse(result); //Parse response into JSON
                setSubsetResponse(jsonResult); //Store the response in local state
                setResultOpen(true); //Show the result in dialog
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation

            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation
                console.log("error", error); //Log the error
                history.push("/404"); //Take user to error page
            });
    }

    return (


        <CardContent>
            <FormLabel component="legend">Select Type of Subset</FormLabel>
            <RadioGroup aria-label="subset-type" name="subset" value={value} onChange={handleChange}>
                <FormControlLabel value="adjacent" control={<Radio color="primary"/>} label="Display Adjacent Nodes"/>
                <FormControlLabel value="descendent" control={<Radio color="primary"/>}
                                  label="Display Descendent Nodes"/>
            </RadioGroup>
            <Button
                variant="contained" color="primary" onClick={handleClickOpen}
                endIcon={<LocationSearchingIcon/>}
                disabled={state.selectedSentenceID === null}
            >
                Select Node
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
                <DialogContent style={{height: "80vh"}}>
                    {state.selectedSentenceID === null ? (
                        <div>Select a sentence first.</div>
                    ) : (
                        <GraphVisualisation/>
                    )}
                </DialogContent>
                <DialogActions>
                    <Chip label={`Selected Node: ${JSON.stringify(state.selectedNodeAndEdges)}`}/>
                    <Button onClick={handleDisplaySubset} color="primary" autoFocus>
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
                <DialogContent style={{height: "80vh"}}>
                    {subsetResponse === null ? (
                        <div>No result to display</div>
                    ) : (
                        <SubsetVisualisation subsetGraphData={subsetResponse}/>
                    )}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleResultClose} color="primary" autoFocus>
                        close
                    </Button>
                </DialogActions>
            </Dialog>
        </CardContent>
    );

}

export default DisplaySubsetTool;
