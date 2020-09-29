import React, { useContext } from "react";
import Divider from "@material-ui/core/Divider";
import List from "@material-ui/core/List";
import Tooltip from "@material-ui/core/Tooltip";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Zoom from "@material-ui/core/Zoom";

import {
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Grid,
  Paper,
  Typography
} from "@material-ui/core";

import { AppContext } from "../../Store/AppContextProvider.js";

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

const layoutGraph = (sentence) => {
  let graph = sentence;

  //Determine span lengths of each node
  const graphNodeSpanLengths = graph.nodes
    .map((node) => node.anchors[0])
    .map((span) => span.end - span.from);
  //Determine unique span lengths of all the node spans
  let uniqueSpanLengths = [];
  const map = new Map();
  for (const item of graphNodeSpanLengths) {
    if (!map.has(item)) {
      map.set(item, true); // set any value to Map
      uniqueSpanLengths.push(item);
    }
  }
  uniqueSpanLengths.sort((a, b) => a - b); //sort unique spans ascending

  //Sort the nodes into each level based on their spans
  let nodesInLevels = [];
  for (const level of uniqueSpanLengths) {
    let currentLevel = [];

    for (
      let spanIndex = 0;
      spanIndex < graphNodeSpanLengths.length;
      spanIndex++
    ) {
      if (graphNodeSpanLengths[spanIndex] === level) {
        currentLevel.push(graph.nodes[spanIndex]);
      }
    }

    nodesInLevels.push(currentLevel);
  }
  //Find the nodes in each level with the same span and group them together
  //Find the unique spans in each level
  let uniqueSpansInLevels = [];
  for (let level of nodesInLevels) {
    let uniqueSpans = []; //Stores the "stringified" objects
    const spanMap = new Map();
    for (const node of level) {
      if (!spanMap.has(JSON.stringify(node.anchors))) {
        spanMap.set(JSON.stringify(node.anchors), true); // set any value to Map
        uniqueSpans.push(JSON.stringify(node.anchors));
      }
    }
    uniqueSpansInLevels.push(uniqueSpans);
    //console.log(uniqueSpans);
  }

  //Iterate through the unique spans in each level and group the same ones together
  for (let level = 0; level < nodesInLevels.length; level++) {
    let newLevelOfGroups = [];
    for (let uniqueSpan of uniqueSpansInLevels[level]) {
      //find the nodes in the level that have the same span and group them together
      let nodesWithCurrentSpan = nodesInLevels[level].filter(
        (node) => JSON.stringify(node.anchors) === uniqueSpan
      );
      newLevelOfGroups.push(nodesWithCurrentSpan);
    }
    nodesInLevels[level] = newLevelOfGroups;
  }

  //Determine the actual number of levels needed
  let height = 0;
  let previousLevelHeights = [0];
  for (let level of nodesInLevels) {
    let maxLevelHeight = 0;
    for (let item of level) {
      maxLevelHeight = Math.max(maxLevelHeight, item.length);
    }
    previousLevelHeights.push(maxLevelHeight);
    height += maxLevelHeight;
  }
  //console.log({height});
  //console.log({nodesInLevels});
  //console.log({previousLevelHeights});

  //Sort the nodes into the final levels
  let nodesInFinalLevels = [];
  for (let index = 0; index < height; index++) {
    nodesInFinalLevels.push([]);
  }
  for (let level = 0; level < nodesInLevels.length; level++) {
    //console.log(nodesInLevels[level]);
    for (let group of nodesInLevels[level]) {
      //console.log({group});
      for (
        let nodeGroupIndex = 0;
        nodeGroupIndex < group.length;
        nodeGroupIndex++
      ) {
        //console.log(group[nodeGroupIndex]);
        let finalLevel =
          previousLevelHeights
            .slice(0, level + 1)
            .reduce((accumulator, currentValue) => accumulator + currentValue) +
          nodeGroupIndex;
        nodesInFinalLevels[finalLevel].push(group[nodeGroupIndex]);
      }
    }
  }
  //console.log({ nodesInFinalLevels });

  //Map the nodes in each level to the correct format

  const totalGraphHeight = height * 50 + (height - 1) * 30; //number of levels times the height of each node and the spaces between them

  for (let level = 0; level < nodesInFinalLevels.length; level++) {
    nodesInFinalLevels[level] = nodesInFinalLevels[level].map((node) => ({
      id: node.id,
      x: node.anchors[0].from * 90,
      y: totalGraphHeight - level * (totalGraphHeight / height),
      label: node.label,
      type: "node",
      nodeLevel: level,
      anchors: node.anchors[0]
    }));
  }

  const tokens = graph.tokens.map((token) => ({
    index: token.index,
    x: token.index * 90,
    y: totalGraphHeight + 100,
    label: token.form,
    type: "token"
  }));

  //this.setState({graphData: nodesInFinalLevels.flat().concat(tokens)});

  const finalGraphNodes = nodesInFinalLevels
    .flat()
    .concat(tokens)
    .map((node) => ({
      id: node.id,
      x: node.x,
      y: node.y,
      label: node.label,
      title: node.label + " tootip text",
      type: node.type,
      anchors: node.anchors,
      fixed: true,
      nodeLevel: node.nodeLevel
    }));

  const finalGraphEdges = graph.edges.map((edge, index) => {
    const fromID =
      finalGraphNodes[
        finalGraphNodes.findIndex((node) => node.id === edge.source)
      ].id;
    const toID =
      finalGraphNodes[
        finalGraphNodes.findIndex((node) => node.id === edge.target)
      ].id;

    let edgeType = "";

    if (fromID === toID) {
      edgeType = "curvedCW";
    } else {
      edgeType = "dynamic";
    }

    return {
      id: index,
      from: fromID,
      to: toID,
      label: edge.label,
      smooth: { type: edgeType, roundness: 1 }
    };
    /*source: testGraphNodes[edge.source],
                target: testGraphNodes[edge.target],*/
  });

  const finalGraph = {
    nodes: finalGraphNodes,
    edges: finalGraphEdges
  };

  return finalGraph;
};

export default function SentenceList() {
  const { state, dispatch } = useContext(AppContext);

  function handleSelectSentence(sentenceId) {
    //Request formatted sentence data from the back-end
    //Set the Context state accordingly through dispatch

    //Just for debugging:
    const selectedSentence = state.dataSet.find(
      (sentence) => sentence.id === sentenceId
    );

    //const randomSentence = state.dataSet[Math.floor(Math.random() * 5)];
    const formattedGraph = layoutGraph(selectedSentence);
    console.log(selectedSentence);
    dispatch({
      type: "SET_SENTENCE",
      payload: { selectedSentence: formattedGraph }
    });
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
