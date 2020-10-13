import React, {useContext} from "react";
import Divider from "@material-ui/core/Divider";
import ListItem from "@material-ui/core/ListItem";
import {Link, useHistory} from "react-router-dom";

import {
    Grid,
    Typography
} from "@material-ui/core";

import {AppContext} from "../../Store/AppContextProvider.js";

import {Virtuoso} from "react-virtuoso";

const styles = {
    Paper: {
        padding: 20,
        marginBottom: 10,
        height: "100%",
        overflow: "auto"
    },
    SentencePaper: {
        height: 300,
        overflow: "auto"
    }
};
/*Component that shows the uploaded sentences to the user in a virtualized list component and allows the user to select one.*/
export default function SentenceList(props) {
    const {state, dispatch} = useContext(AppContext);
    const history = useHistory();

    function handleSelectSentence(sentenceId) {
        //Request formatted sentence data from the back-end
        //Set the Context state accordingly through dispatch

        let requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };
        props.closeSelectSentence(); //Close the dialog
        dispatch({type: "SET_LOADING", payload: {isLoading: true}}); //Show the loading animation

        fetch(state.APIendpoint + "/Visualise?graphID=" + sentenceId + "&format="+state.visualisationFormat, requestOptions)
            .then((response) => response.text())
            .then((result) => {
                const jsonResult = JSON.parse(result);
                console.log(jsonResult);
                //Update the global state:
                dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: jsonResult}});
                dispatch({type: "SET_SELECTED_SENTENCE_ID", payload: {selectedSentenceID: jsonResult.id}});
                dispatch({type: "SET_TEST_RESULTS", payload: { testResults: null } });
                dispatch({type: "SET_LOADING", payload: {isLoading: false}});
            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}});
                console.log("error", error);
                history.push("/404");
            });
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
                            <ListItem button key={state.dataSet[index].id}
                                      onClick={() => handleSelectSentence(state.dataSet[index].id)}>
                                <Typography>{state.dataSet[index].input}</Typography>
                            </ListItem>
                        );
                    }}
                    footer={() => (
                        <div style={{padding: "1rem", textAlign: "center"}}>
                            <Typography>
                                -- end of dataset --
                            </Typography>
                        </div>
                    )}
                />
            )}
            <Divider/>
        </Grid>
    );
}
