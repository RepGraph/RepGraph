import React, { useContext } from "react";
import Divider from "@material-ui/core/Divider";
import List from "@material-ui/core/List";
import Tooltip from "@material-ui/core/Tooltip";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Zoom from "@material-ui/core/Zoom";
import { Link, useHistory } from "react-router-dom";

import {
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Grid,
  Paper,
  Typography
} from "@material-ui/core";

import { AppContext } from "../../Store/AppContextProvider.js";

import {layoutGraph} from "../App";

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

export default function SentenceList() {
  const { state, dispatch } = useContext(AppContext);
  const history = useHistory();

  function handleSelectSentence(sentenceId) {
    //Request formatted sentence data from the back-end
    //Set the Context state accordingly through dispatch

    let requestOptions = {
      method: 'GET',
      redirect: 'follow'
    };

    dispatch({ type: "SET_LOADING", payload: { isLoading: true } });

    fetch(state.APIendpoint+"/Visualise?graphID="+sentenceId+"&format=1", requestOptions)
        .then((response) => response.text())
        .then((result) => {

          const jsonResult = JSON.parse(result);
          console.log(jsonResult);
          //console.log(jsonResult);
          //console.log(jsonResult.response);
          const formattedGraph = layoutGraph(jsonResult);
          dispatch({ type: "SET_SENTENCE", payload: { selectedSentence: formattedGraph } });
          dispatch({ type: "SET_SELECTED_SENTENCE_ID", payload: { selectedSentenceID: jsonResult.id} });

            dispatch({ type: "SET_LOADING", payload: { isLoading: false } });
        })
        .catch((error) => {
            dispatch({ type: "SET_LOADING", payload: { isLoading: false } });
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
    <Grid item style={{ width: "100%" }}>
      <Paper style={styles.SentencePaper}>
        <List component="nav" aria-label="features">
          <List component="ul">
            {state.dataSet === null ? (
              <div>No data-set has been uploaded yet</div>
            ) : (
              state.dataSet.map((sentence) => (
                <Tooltip
                  key={sentence.id}
                  TransitionComponent={Zoom}
                  title="Display Graph"
                  placement="top"
                >
                  <div>
                    <ListItem
                      button
                      onClick={() => handleSelectSentence(sentence.id)}
                    >
                      <ListItemText primary={sentence.input}></ListItemText>
                    </ListItem>
                    <Divider />
                  </div>
                </Tooltip>
              ))
            )}
          </List>
        </List>
      </Paper>

      <Divider />
    </Grid>
  );
}
