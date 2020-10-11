import React, {useContext} from "react";
import Divider from "@material-ui/core/Divider";
import List from "@material-ui/core/List";
import Tooltip from "@material-ui/core/Tooltip";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Zoom from "@material-ui/core/Zoom";
import {Link, useHistory} from "react-router-dom";

import {
    Accordion,
    AccordionSummary,
    AccordionDetails,
    Grid,
    Paper,
    Typography
} from "@material-ui/core";

import {AppContext} from "../../Store/AppContextProvider.js";

import {layoutGraph} from "../App";
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
        props.closeSelectSentence();
        dispatch({type: "SET_LOADING", payload: {isLoading: true}});

        //state.APIendpoint+"/DisplayPlanarGraph?graphID=20001001

        fetch(state.APIendpoint + "/Visualise?graphID=" + sentenceId + "&format=1", requestOptions)
            .then((response) => response.text())
            .then((result) => {

                const jsonResult = JSON.parse(result);
                console.log(jsonResult);
                //console.log(jsonResult);
                //console.log(jsonResult.response);
                //const formattedGraph = layoutGraph(jsonResult);
                dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: jsonResult}});
                dispatch({type: "SET_SELECTED_SENTENCE_ID", payload: {selectedSentenceID: jsonResult.id}});
                dispatch({ type: "SET_TEST_RESULTS", payload: { testResults: null } });
                dispatch({type: "SET_LOADING", payload: {isLoading: false}});
            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}});
                console.log("error", error);
                history.push("/404"); //for debugging

            });

        //Just for debugging:
        /*const selectedSentence = state.dataSet.find(
          (sentence) => sentence.id === sentenceId
        );

        //const randomSentence = state.dataSet[Math.floor(Math.random() * 5)];
        const formattedGraph = layoutGraph(selectedSentence);
        console.log(selectedSentence);
        dispatch({
          type: "SET_SENTENCE",
          payload: { selectedSentence: formattedGraph }
        });*/
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
                            -- end of dataset --
                        </div>
                    )}
                />
            )}
            <Divider/>
        </Grid>
    );
}
