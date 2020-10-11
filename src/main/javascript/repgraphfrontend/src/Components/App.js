import React, { useContext } from "react";
import "../../src/styles.css";
import Routes from "../../src/Routes";
import { BrowserRouter as Router } from "react-router-dom";

import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles";

import { AppContext } from "../../src/Store/AppContextProvider";
import Backdrop from "@material-ui/core/Backdrop";
import CircularProgress from "@material-ui/core/CircularProgress";
import makeStyles from "@material-ui/core/styles/makeStyles";

//import { AppContext } from "./Store/AppContext.js";

const font = "'Open Sans', 'Helvetica', 'Arial', sans-serif";


export const layoutGraph = (sentence) => {
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

    //Map the nodes in each level to the correct format
    const totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each node and the spaces between them
    let space = 130;

    for (let level = 0; level < nodesInFinalLevels.length; level++) {
        nodesInFinalLevels[level] = nodesInFinalLevels[level].map((node) => ({
            id: node.id,
            x: node.anchors[0].from * space,
            y: totalGraphHeight - level * (totalGraphHeight / height),
            label: node.label,
            type: "node",
            nodeLevel: level,
            anchors: node.anchors[0],
            group: "node"
        }));
    }

    const tokens = graph.tokens.map((token) => ({
        index: token.index,
        x: token.index * space,
        y: totalGraphHeight + 200,
        label: token.form,
        type: "token",
        group: "token"
    }));

    const finalGraphNodes = nodesInFinalLevels
        .flat()
        .concat(tokens)
        .map((node) => ({
            id: node.id,
            x: node.x,
            y: node.y,
            label: node.label,
            title: node.label + " tooltip text",
            group: node.group,
            anchors: node.anchors,
            fixed: true,
            nodeLevel: node.nodeLevel
        }));

    //Setting directed node neighbours for better edge placement
    //let directedNeighbours = [];
    let directedNeighbours = new Map();

    for(let node of graph.nodes){
        directedNeighbours[node.id] = [];
    }

    /*for (let i = 0; i < graph.nodes.length; i++) {
        directedNeighbours[i] = [];
    }*/

    for (let e of graph.edges) {
        //console.log(e);
        if(directedNeighbours.has(e.source)) {
            directedNeighbours[e.source].push(e.target);
        }
    }

    const finalGraphEdges = graph.edges.map((edge, index) => {
        const fromNode = finalGraphNodes.find((node) => node.id === edge.source);
        const toNode = finalGraphNodes.find((node) => node.id === edge.target);

        const fromID = fromNode.id;
        const toID = toNode.id;

        let edgeType = "";
        let round = 0.45;
        if (fromNode.x === toNode.x) {
            if (Math.abs(fromNode.nodeLevel - toNode.nodeLevel) === 1) {
                edgeType = "continuous";
            } else {
                edgeType = "curvedCCW";
                for (let i = 0; i < directedNeighbours[fromID].length; i++) {
                    if (
                        fromNode.x / space -
                        graph.nodes[directedNeighbours[fromID][i]].anchors[0].from ===
                        1
                    ) {
                        edgeType = "curvedCW";
                    }
                }
            }
        } else {
            if (fromNode.nodeLevel === toNode.nodeLevel) {
                edgeType = "curvedCCW";
                if (Math.abs(fromNode.x / space - toNode.x / space) > 4) {
                    round = 0.2;
                }
                if (Math.abs(fromNode.x / space - toNode.x / space) > 10) {
                    round = 0.1;
                }
                if (
                    fromNode.x / space - toNode.x / space > 0 &&
                    fromNode.nodeLevel === 0
                ) {
                    edgeType = "curvedCW";
                }
            } else {
                edgeType = "dynamic";
            }
        }

        return {
            id: index,
            from: fromID,
            to: toID,
            label: edge.label,
            smooth: { type: edgeType, roundness: round },
            endPointOffset: {
                from: 20,
                to: 0
            }
        };

    });

    const finalGraph = {
        nodes: finalGraphNodes,
        edges: finalGraphEdges
    };

    return finalGraph;
};

/*
export const layoutGraph = (sentence) => {
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

    const totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each node and the spaces between them

    for (let level = 0; level < nodesInFinalLevels.length; level++) {
        nodesInFinalLevels[level] = nodesInFinalLevels[level].map((node) => ({
            id: node.id,
            x: node.anchors[0].from * 110,
            y: totalGraphHeight - level * (totalGraphHeight / height),
            label: node.label,
            type: "node",
            nodeLevel: level,
            anchors: node.anchors[0],
            group: 'node'
        }));
    }

    const tokens = graph.tokens.map((token) => ({
        index: token.index,
        x: token.index * 110,
        y: totalGraphHeight + 100,
        label: token.form,
        type: "token",
        group: 'token'
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
            title: node.label + " tooltip text",
            group: node.group,
            anchors: node.anchors,
            fixed: true,
            nodeLevel: node.nodeLevel,

        }));

    const finalGraphEdges = graph.edges.map((edge, index) => {
        // const fromID =
        //     finalGraphNodes[
        //         finalGraphNodes.findIndex((node) => node.id === edge.source)
        //         ].id;
        // const toID =
        //     finalGraphNodes[
        //         finalGraphNodes.findIndex((node) => node.id === edge.target)
        //         ].id;

        const fromNode = finalGraphNodes.find((node) => node.id === edge.source);
        const toNode = finalGraphNodes.find((node) => node.id === edge.target);

        const fromID = fromNode.id;
        const toID = toNode.id;

        let edgeType = "";

        if ((fromNode.nodeLevel === toNode.nodeLevel) && (fromNode.nodeLevel == 0)) {
            edgeType = "curvedCW";
        } else {
            edgeType = "dynamic";
        }

        return {
            id: index,
            from: fromID,
            to: toID,
            label: edge.label,
            smooth: { type: edgeType, roundness: 0.4 },
            endPointOffset: {
                from: 20,
                to: 0,
            }
        };
    });

    const finalGraph = {
        nodes: finalGraphNodes,
        edges: finalGraphEdges
    };

    return finalGraph;
};*/

const palette = {
    primary: {},
    secondary: {},
    error: {},
    warning: {},
    success: {},
    palette: {
        primary:{
            main: "#fafafa",
            contrastText: '#fff',
        },
        secondary:{
            main: "#00b0ff",
            contrastText: '#000',
        }}
};

export const theme = createMuiTheme({

    typography: {
        fontFamily: font,
        fontSize: 14,
        fontWeightBold: 700,
        fontWeightLight: 300,
        fontWeightMedium: 600,
        fontWeightRegular: 400,
        body1: {
            fontFamily: font,
            fontSize: "1rem",
            fontWeight: 400,
            letterSpacing: "0.00938em",
            lineHeight: 1.5
        },
        body2: {
            fontFamily: font,
            fontSize: "0.875rem",
            fontWeight: 400,
            letterSpacing: "0.01071em",
            lineHeight: 1.43
        },
        button: {
            fontFamily: font,
            fontSize: "0.875rem",
            fontWeight: 500,
            letterSpacing: "0.02857em",
            lineHeight: 1.75,
            textTransform: "uppercase"
        },
        caption: {
            fontFamily: font,
            fontSize: "0.75rem",
            fontWeight: 400,
            letterSpacing: "0.03333em",
            lineHeight: 1.66
        },
        h1: {
            fontFamily: font,
            fontSize: "6rem",
            fontWeight: 300,
            letterSpacing: "-0.01562em",
            lineHeight: 1.167
        },
        h2: {
            fontFamily: font,
            fontSize: "3.75rem",
            fontWeight: 300,
            letterSpacing: "-0.00833em",
            lineHeight: 1.2
        },
        h3: {
            fontFamily: font,
            fontSize: "3rem",
            fontWeight: 400,
            letterSpacing: "0em",
            lineHeight: 1.167
        },
        h4: {
            fontFamily: font,
            fontSize: "2.125rem",
            fontWeight: 400,
            letterSpacing: "0.00735em",
            lineHeight: 1.235
        },
        h5: {
            fontFamily: font,
            fontSize: "1.5rem",
            fontWeight: 400,
            letterSpacing: "0em",
            lineHeight: 1.334
        },
        h6: {
            fontFamily: font,
            fontSize: "1.25rem",
            fontWeight: 500,
            letterSpacing: "0.0075em",
            lineHeight: 1.6
        },
        subtitle1: {
            fontFamily: font,
            fontSize: "1rem",
            fontWeight: 400,
            letterSpacing: "0.00938em",
            lineHeight: 1.75
        },
        subtitle2: {
            fontFamily: font,
            fontSize: "0.875rem",
            fontWeight: 500,
            letterSpacing: "0.00714em",
            lineHeight: 1.57
        }
    }
});


const useStyles = makeStyles((theme) => ({
    backdrop: {
        zIndex: theme.zIndex.drawer + 1,
        color: '#fff',
    },
}));

export default function App() {
    const { state, dispatch } = useContext(AppContext);
    const classes = useStyles();

    return (
        <MuiThemeProvider theme={theme}>
            <Backdrop className={classes.backdrop} open={state.isLoading} >
                <CircularProgress color="inherit" />
            </Backdrop>
            <Router>
                <Routes />
            </Router>
        </MuiThemeProvider>
    );
}
