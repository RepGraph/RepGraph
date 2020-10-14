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

const useStyles = makeStyles((theme) => ({
    root: {
        height: "100%",
        width: "100%"
    }

}));

function FormalTestsTool(props) {
    const {state, dispatch} = useContext(AppContext); //Provide access to global state
    const history = useHistory(); //Provide access to router history

    const classes = useStyles(); //Use styles created above
    //Local state for checkboxes of which tests to perform
    const [tests, setTests] = React.useState({
        planar: false,
        longestPathDirected: false,
        longestPathUndirected: false,
        connected: false
    });

    //Handle change to checkboxes for formal tests
    const handleChange = (event) => {
        setTests({...tests, [event.target.name]: event.target.checked});
    };

    //Handle request to perform formal tests
    function handleFormalTests() {

        let requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        dispatch({type: "SET_LOADING", payload: {isLoading: true}}); //Show loading animation

        //Fetch formal test results from backend
        fetch(state.APIendpoint + "/TestGraph?graphID=" + state.selectedSentenceID + "&planar=" + tests.planar + "&longestPathDirected=" + tests.longestPathDirected + "&longestPathUndirected=" + tests.longestPathUndirected + "&connected=" + tests.connected, requestOptions)
            .then((response) => response.text())
            .then((result) => {
                const jsonResult = JSON.parse(result);
                console.log(jsonResult); //Debugging
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation
                dispatch({type: "SET_TEST_RESULTS", payload: {testResults: jsonResult}}); //Store the test results in global state
            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation
                console.log("error", error); //Log the error to the console
                history.push("/404"); //Take the user to the error page
            });
    }

    return (
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
                        control={<Checkbox checked={tests.planar} onChange={handleChange} color="primary"
                                           name="planar"/>}
                        label="Graph Planar?"
                    />
                    <FormControlLabel
                        control={<Checkbox checked={tests.longestPathDirected} onChange={handleChange} color="primary"
                                           name="longestPathDirected"/>}
                        label="Find Longest Directed Path"
                    />
                    <FormControlLabel
                        control={<Checkbox checked={tests.longestPathUndirected} onChange={handleChange}
                                           color="primary" name="longestPathUndirected"/>}
                        label="Find Longest Undirected Path"
                    />
                    <FormControlLabel
                        control={<Checkbox checked={tests.connected} onChange={handleChange} color="primary"
                                           name="connected"/>}
                        label="Graph Connected?"
                    />
                    <Button
                        disabled={state.selectedSentenceID === null}
                        variant="contained"
                        color="primary"
                        endIcon={<ArrowForwardIcon/>}
                        onClick={() => {
                            console.log(tests); //Debugging
                            handleFormalTests();
                        }}
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
