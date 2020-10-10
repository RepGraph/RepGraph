import React, {useContext} from 'react';
import FormControl from "@material-ui/core/FormControl";
import FormLabel from "@material-ui/core/FormLabel";
import FormGroup from "@material-ui/core/FormGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import Button from "@material-ui/core/Button";
import ArrowForwardIcon from "@material-ui/icons/ArrowForward";
import FormHelperText from "@material-ui/core/FormHelperText";
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/core/styles";
import {AppContext} from "../../Store/AppContextProvider";
import {useHistory} from "react-router-dom";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import GraphVisualisation from "../Main/GraphVisualisation";
import DialogActions from "@material-ui/core/DialogActions";
import Chip from "@material-ui/core/Chip";
import Dialog from "@material-ui/core/Dialog";
import FormalTestResultsDisplay from "./FormalTestResultsDisplay";

const useStyles = makeStyles((theme) => ({
    root: {
        height: "100%",
        width: "100%"
    }

}));

function FormalTestsTool(props){
    const { state, dispatch } = useContext(AppContext);
    const history = useHistory();

    const classes = useStyles();

    const [testResults, setTestResults ] = React.useState(null);

    const [tests, setTests] = React.useState({
        planar: false,
        longestPathDirected: false,
        longestPathUndirected: false,
        connected: false
    });


    const handleChange = (event) => {
        setTests({ ...tests, [event.target.name]: event.target.checked });
    };


    function handleFormalTests(){

        let requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        dispatch({ type: "SET_LOADING", payload: { isLoading: true } });

        fetch(state.APIendpoint+"/TestGraph?graphID="+state.selectedSentenceID+"&planar="+tests.planar+"&longestPathDirected="+tests.longestPathDirected+"&longestPathUndirected="+tests.longestPathUndirected+"&connected="+tests.connected, requestOptions)
            .then((response) => response.text())
            .then((result) => {

                const jsonResult = JSON.parse(result);
                console.log(jsonResult);
                dispatch({ type: "SET_LOADING", payload: { isLoading: false } });
                dispatch({ type: "SET_TEST_RESULTS", payload: { testResults: jsonResult } });
                setTestResults(jsonResult);
            })
            .catch((error) => {
                dispatch({ type: "SET_LOADING", payload: { isLoading: false } });
                console.log("error", error);
                history.push("/404"); //for debugging
            });
    }

    const { planar, longestPathDirected, longestPathUndirected, connected } = state;

    return(
        <Grid
            container
            direction="column"
            justify="center"
            alignItems="center"
            className={classes.root}
        >
            <FormControl component="fieldset">
                <FormLabel color="primary" component="legend">Graph Property Tests</FormLabel>
                <FormGroup>
                    <FormControlLabel
                        control={<Checkbox checked={planar} onChange={handleChange} color="secondary"  name="planar" />}
                        label="Graph Planar?"
                    />
                    <FormControlLabel
                        control={<Checkbox  checked={longestPathDirected} onChange={handleChange} color="secondary" name="longestPathDirected" />}
                        label="Find Longest Directed Path"
                    />
                    <FormControlLabel
                        control={<Checkbox  checked={longestPathUndirected} onChange={handleChange} color="secondary" name="longestPathUndirected" />}
                        label="Find Longest Undirected Path"
                    />
                    <FormControlLabel
                        control={<Checkbox  checked={connected} onChange={handleChange} color="secondary" name="connected" />}
                        label="Graph Connected?"
                    />
                    <Button
                        disabled={state.selectedSentenceID === null}
                        variant="contained"
                        color="primary"
                        endIcon={<ArrowForwardIcon/>}
                        onClick={() => {console.log(tests);
                        handleFormalTests();}}
                    >
                        Run Tests
                    </Button>
                </FormGroup>
                <FormHelperText/>
            </FormControl>
        </Grid>
    );

}

export default FormalTestsTool;
