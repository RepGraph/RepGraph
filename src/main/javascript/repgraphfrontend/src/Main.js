import React, { useContext } from "react";
import {
  Card,
  Grid,
  Paper,
  Typography,
  CardContent,
  CardActions
} from "@material-ui/core";

import { AppContext } from "./Store/AppContextProvider.js";
import SentenceList from "./Components/Main/SentenceList.js";
import Header from "./Components/Layouts/Header.js";
import AnalysisAccordion from "./Components/Main/AnalysisAccordion";
import VisualisationControls from "./Components/Main/VisualisationControls";
import GraphVisualisation from "./Components/Main/GraphVisualisation";

import "./Components/Main/network.css";

const testingEndpoint = "http://192.168.0.135:8080";

export default function Main() {
  const { state, dispatch } = useContext(AppContext);

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
              .reduce(
                (accumulator, currentValue) => accumulator + currentValue
              ) + nodeGroupIndex;
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

  return (
    <React.Fragment>
      <Header />
      <Grid container spacing={2} direction="column">
        <Grid item xs={12}>
          <Card>
            <CardContent>
              <Typography variant="h6">Data-set Sentences</Typography>
            </CardContent>
            <CardActions>
              <SentenceList />
            </CardActions>
          </Card>
        </Grid>
        <Grid item xs={12}>
          <Card>
            <CardContent>
              <Typography variant="h6">Visualisation Area</Typography>
            </CardContent>
            <CardActions>
              <VisualisationControls />
            </CardActions>
            <CardContent>
              {state.selectedSentence === null ? (
                <div>Select a sentence</div>
              ) : (
                <GraphVisualisation />
              )}
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12}>
          <Card>
            <CardContent>
              <Typography variant="h6">Analysis Features</Typography>
            </CardContent>
            <CardActions>
              <AnalysisAccordion />
            </CardActions>
          </Card>
        </Grid>
      </Grid>
    </React.Fragment>
  );
}
