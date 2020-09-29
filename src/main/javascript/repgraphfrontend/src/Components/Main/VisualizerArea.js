import React, { useRef, useState } from "react";
import Typography from "@material-ui/core/Typography";
import Paper from "@material-ui/core/Paper";
import { Divider } from "@material-ui/core";
import TextareaAutosize from "@material-ui/core/TextareaAutosize";
import Backdrop from "@material-ui/core/Backdrop";
import makeStyles from "@material-ui/core/styles/makeStyles";

import FormLabel from "@material-ui/core/FormLabel";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Radio from "@material-ui/core/Radio";
import FormControl from "@material-ui/core/FormControl";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import AccordionDetails from "@material-ui/core/AccordionDetails";

import { Graph, DefaultLink, DefaultNode } from "@vx/network";
import { Text } from "@vx/text";
import { Zoom } from "@vx/zoom";
import { localPoint } from "@vx/event";
import { RectClipPath } from "@vx/clip-path";
import { LinearGradient } from "@vx/gradient";
import { ParentSize } from "@vx/responsive";
import { Group } from '@vx/group';
import { MarkerArrow} from '@vx/marker';
import { Line, LinkHorizontalCurve, LinkVerticalCurve, LinkVertical, LinePath } from '@vx/shape';
import { curveNatural } from '@vx/curve';
import Tooltip from "@material-ui/core/Tooltip";

const initialTransform = {
  scaleX: 1.27,
  scaleY: 1.27,
  translateX: -211.62,
  translateY: 162.59,
  skewX: 0,
  skewY: 0,
};

const testGraph = JSON.parse(
  '{"id":"20001001","source":"wsj00a","input":"Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29.","tokens":[{"index":0,"form":"pierre","lemma":"Pierre","carg":"Pierre"},{"index":1,"form":"Vinken,","lemma":"Vinken","carg":"Vinken"},{"index":2,"form":"61","lemma":"61","carg":"61"},{"index":3,"form":"years","lemma":"year"},{"index":4,"form":"old,","lemma":"old"},{"index":5,"form":"will","lemma":"will"},{"index":6,"form":"join","lemma":"join"},{"index":7,"form":"the","lemma":"the"},{"index":8,"form":"board","lemma":"board"},{"index":9,"form":"as","lemma":"as"},{"index":10,"form":"a","lemma":"a"},{"index":11,"form":"nonexecutive","lemma":"nonexecutive"},{"index":12,"form":"director","lemma":"director"},{"index":13,"form":"nov.","lemma":"Nov","carg":"Nov"},{"index":14,"form":"29.","lemma":"29","carg":"29"}],"nodes":[{"id":0,"label":"proper_q","anchors":[{"from":0,"end":0}]},{"id":1,"label":"named","anchors":[{"from":0,"end":0}]},{"id":2,"label":"named","anchors":[{"from":1,"end":1}]},{"id":3,"label":"compound","anchors":[{"from":0,"end":1}]},{"id":4,"label":"card","anchors":[{"from":2,"end":2}]},{"id":5,"label":"_year_n_1","anchors":[{"from":3,"end":3}]},{"id":6,"label":"measure","anchors":[{"from":2,"end":3}]},{"id":7,"label":"udef_q","anchors":[{"from":2,"end":3}]},{"id":8,"label":"_old_a_1","anchors":[{"from":4,"end":4}]},{"id":9,"label":"proper_q","anchors":[{"from":0,"end":4}]},{"id":10,"label":"_join_v_1","anchors":[{"from":6,"end":6}]},{"id":11,"label":"_the_q","anchors":[{"from":7,"end":7}]},{"id":12,"label":"_board_n_of","anchors":[{"from":8,"end":8}]},{"id":13,"label":"_as_p","anchors":[{"from":9,"end":9}]},{"id":14,"label":"_a_q","anchors":[{"from":10,"end":10}]},{"id":15,"label":"_nonexecutive_u_unknown","anchors":[{"from":11,"end":11}]},{"id":16,"label":"_director_n_of","anchors":[{"from":12,"end":12}]},{"id":17,"label":"mofy","anchors":[{"from":13,"end":13}]},{"id":18,"label":"def_explicit_q","anchors":[{"from":13,"end":13}]},{"id":19,"label":"of_p","anchors":[{"from":13,"end":13}]},{"id":20,"label":"def_implicit_q","anchors":[{"from":13,"end":13}]},{"id":21,"label":"dofm","anchors":[{"from":14,"end":14}]},{"id":22,"label":"loc_nonsp","anchors":[{"from":13,"end":14}]}],"edges":[{"source":9,"target":2,"label":"RSTR","post-label":"H"},{"source":3,"target":2,"label":"ARG1","post-label":"EQ"},{"source":3,"target":1,"label":"ARG2","post-label":"NEQ"},{"source":0,"target":1,"label":"RSTR","post-label":"H"},{"source":6,"target":8,"label":"ARG1","post-label":"EQ"},{"source":6,"target":5,"label":"ARG2","post-label":"NEQ"},{"source":7,"target":5,"label":"RSTR","post-label":"H"},{"source":4,"target":5,"label":"ARG1","post-label":"EQ"},{"source":8,"target":2,"label":"ARG1","post-label":"EQ"},{"source":10,"target":2,"label":"ARG1","post-label":"NEQ"},{"source":10,"target":12,"label":"ARG2","post-label":"NEQ"},{"source":11,"target":12,"label":"RSTR","post-label":"H"},{"source":13,"target":10,"label":"ARG1","post-label":"EQ"},{"source":13,"target":16,"label":"ARG2","post-label":"NEQ"},{"source":14,"target":16,"label":"RSTR","post-label":"H"},{"source":15,"target":16,"label":"ARG1","post-label":"EQ"},{"source":22,"target":10,"label":"ARG1","post-label":"EQ"},{"source":22,"target":21,"label":"ARG2","post-label":"NEQ"},{"source":18,"target":21,"label":"RSTR","post-label":"H"},{"source":19,"target":21,"label":"ARG1","post-label":"EQ"},{"source":19,"target":17,"label":"ARG2","post-label":"NEQ"},{"source":20,"target":17,"label":"RSTR","post-label":"H"}],"tops":[10]}'
);


/*
fetch('http://192.168.101.127:8080/UploadSingle',
    {
        method: 'POST', // or 'PUT'
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(testGraph),
    }
)
    .then(data => {
        console.log('Success:', data);
    })
    .catch((error) => {
        console.error('Error:', error);
    });


fetch('http://192.168.101.127:8080/Visualise?graphID=20001001&format=1')
    .then(response => response.json())
    .then(data => console.log(data))
    .catch((error) => {
        console.error('Error:', error);
    });
*/

function layoutGraphTesting(graph){
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
    for(const level of uniqueSpanLengths){
        let currentLevel = [];

        for(let spanIndex = 0; spanIndex < graphNodeSpanLengths.length;spanIndex++){
            if(graphNodeSpanLengths[spanIndex] === level){
                currentLevel.push(graph.nodes[spanIndex]);
            }
        }

        nodesInLevels.push(currentLevel);
    }
    //Find the nodes in each level with the same span and group them together
    //Find the unique spans in each level
    let uniqueSpansInLevels = []
    for(let level of nodesInLevels){
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
    for(let level = 0;level< nodesInLevels.length ;level++){
        let newLevelOfGroups = [];
        for(let uniqueSpan of uniqueSpansInLevels[level]){
            //find the nodes in the level that have the same span and group them together
            let nodesWithCurrentSpan = nodesInLevels[level].filter(node => JSON.stringify(node.anchors) === uniqueSpan);
            newLevelOfGroups.push(nodesWithCurrentSpan);
        }
        nodesInLevels[level] = newLevelOfGroups;
    }

    //Determine the actual number of levels needed
    let height = 0;
    let previousLevelHeights = [0];
    for(let level of nodesInLevels){
        let maxLevelHeight = 0;
        for(let item of level){
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
    for(let index = 0; index < height ; index++){
        nodesInFinalLevels.push([]);
    }
    for(let level = 0; level < nodesInLevels.length; level++){
        //console.log(nodesInLevels[level]);
        for(let group of nodesInLevels[level]){
            //console.log({group});
            for(let nodeGroupIndex = 0; nodeGroupIndex < group.length; nodeGroupIndex++){
                //console.log(group[nodeGroupIndex]);
                let finalLevel = previousLevelHeights.slice(0, level+1).reduce((accumulator, currentValue) => accumulator + currentValue) + nodeGroupIndex;
                nodesInFinalLevels[finalLevel].push(group[nodeGroupIndex]);
            }
        }
    }
    console.log({nodesInFinalLevels});

    //Map the nodes in each level to the correct format

    const totalGraphHeight = height*50 + (height-1)*30; //number of levels times the height of each node and the spaces between them

    for(let level=0; level < nodesInFinalLevels.length; level++){
        nodesInFinalLevels[level] = nodesInFinalLevels[level].map((node)=>({
            id: node.id,
            x: node.anchors[0].from * 90,
            y: totalGraphHeight - level * (totalGraphHeight / height),
            label: node.label,
            type: "node",
            nodeLevel: level,
            anchors: node.anchors[0],
        }));
    }

    const tokens = graph.tokens.map((token) => ({
        index: token.index,
        x: token.index * 90,
        y: totalGraphHeight + 100,
        label: token.form,
        type: "token",
    }));

    return nodesInFinalLevels.flat().concat(tokens);
}

/*
function layoutGraph(graph){

    //determine span lengths of each node
    const graphNodeSpanLengths = graph.nodes
        .map((node) => node.anchors[0])
        .map((span) => span.end - span.from);
    //determine unique span lengths of all the node spans
    let uniqueSpanLengths = [];
    const map = new Map();
    for (const item of graphNodeSpanLengths) {
        if (!map.has(item)) {
            map.set(item, true); // set any value to Map
            uniqueSpanLengths.push(item);
        }
    }
    uniqueSpanLengths.sort((a, b) => a - b); //sort unique spans ascending

    //sort the nodes into each level based on their spans
    let nodesInLevels = [];
    for(const level of uniqueSpanLengths){
        let currentLevel = [];

        for(let spanIndex = 0; spanIndex < graphNodeSpanLengths.length;spanIndex++){
            if(graphNodeSpanLengths[spanIndex] === level){
                currentLevel.push(graph.nodes[spanIndex]);
            }
        }

        nodesInLevels.push(currentLevel);
    }

    //map the nodes in each level to the correct format
    for(let level = 0 ; level < nodesInLevels.length ; level++){
        nodesInLevels[level] = nodesInLevels[level].map((node, index)=>({
            id: node.id,
            x: node.anchors[0].from * 90,
            y: 400 - level * (400 / (uniqueSpanLengths.length - 1)),
            label: node.label,
            type: "node",
            nodeLevel: level,
            anchors: node.anchors[0],
        }));
    }

    //Handle the layout edge cases:

    //Need to find all the nodes in the same levels that have the same unique span
    //Find the unique spans in each level
    let uniqueSpansInLevels = []
    for(let level of nodesInLevels){
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
    for(let level = 0;level< nodesInLevels.length ;level++){

        for(let uniqueSpan of uniqueSpansInLevels[level]){
            //console.log({uniqueSpan});
            //find the nodes in the level that have the same span and group them together
            let nodesWithCurrentSpan = nodesInLevels[level].filter(node => JSON.stringify(node.anchors) === uniqueSpan);
            //handle if there is more than one:
            //console.log(nodesWithCurrentSpan);
            if(nodesWithCurrentSpan.length > 1){
                let xCoord = nodesWithCurrentSpan[0].x;
                for(let index = 1; index < nodesWithCurrentSpan.length; index ++){
                    let actualNodeIndex = nodesInLevels[level].findIndex(node => node.id === nodesWithCurrentSpan[index].id);
                    //console.log({actualNodeIndex});
                    nodesInLevels[level][actualNodeIndex].x = xCoord;
                    nodesInLevels[level][actualNodeIndex].y = nodesInLevels[level][actualNodeIndex].y - 40*index; //Place them above each other
                    //console.log(nodesInLevels[level][actualNodeIndex]);
                }
            }
        }

    }

    return nodesInLevels;
}*/

//Lay out the graph:
//const testGraphNodes = layoutGraph(testGraph).flat();
const testGraphNodes = layoutGraphTesting(testGraph);
//console.log(testingGraphNodes);

//Map the graph edge data to the correct format for the links
const testGraphEdges = testGraph.edges.map((edge) => ({
    source: testGraphNodes[testGraphNodes.findIndex(node => node.id === edge.source)],
    target: testGraphNodes[testGraphNodes.findIndex(node => node.id === edge.target)],
    label: edge.label,
  /*source: testGraphNodes[edge.source],
  target: testGraphNodes[edge.target],*/
}));

const testGraphTokens = testGraph.tokens.map((token) => ({
  index: token.index,
  x: token.index * 90,
  y: 550,
  label: token.form,
  type: "token",
}));

const testGraphData = {
  nodes: testGraphNodes,
  links: testGraphEdges,
};

const Node = ({ node }) => {
    //console.log(node.label,node.anchors);
  return (

      <Group>
          <rect
              x="-40"
              y="-15"
              rx="10"
              ry="10"
              width="80"
              height="30"
              fill={node.type === "node" ? "#ffea00" : "#00e676"}
              fillOpacity={0.2}
              strokeWidth="0"
              stroke="#000"
              onClick={() => alert("clicked node: "+node.id)}
          />
          {node.type === "node" && <polyline
              points={`-40,25 -40,20 ${40 + 90*(node.anchors.end - node.anchors.from)},20 ${40 + 90*(node.anchors.end - node.anchors.from)},25 `} //${node.x},${node.y} ${node.x+80},${node.y}
              stroke="black"
              strokeWidth={2}
              fill={"none"}
          />}
          {node.label && (
              <Text
                  width={80}
                  fill="Black"
                  dx="-30"
                  dy="0"
                  fontFamily="Arial"
                  textAnchor={"start"}
                  verticalAnchor={"middle"}
                  clipPath="url(#nodeClipPath)"
              >
                  {node.label}
              </Text>
          )}
          <RectClipPath id={"nodeClipPath"} height={30} width={100} x={-40} y={-15}/>
      </Group>


  );
};

const Link = ({ link }) => {

    let fromX = link.source.x;
    let toX = link.target.x;
    let fromY = link.source.y;
    let toY = link.target.y;
    //let LinkComponent;

    let textDx = 0;
    let textDy = 0;

    //Control points for cubic bezier paths
    let bezierPoints = {
        start: [fromX, fromY],
        cp1: [fromX, fromY],
        cp2: [toX, toY],
        end: [toX, toY]
    }
    //Control points for quadratic bezier paths
    let bezierPointsQuadratic = {
        start: [fromX, fromY],
        cp: [((fromX+toX)/2), ((fromY+toY)/2)+100],
        end: [toX, toY]
    }

    let linkData = "";
    if(link.source.nodeLevel === link.target.nodeLevel){//if both the source and target nodes are on the same level
        //LinkComponent = LinkVerticalCurve;
        bezierPoints.start[0] += 0;
        bezierPoints.start[1] += 15;
        //If source is to the left of the target
        if(link.source.anchors.from < link.target.anchors.from){
            bezierPoints.cp1[0] += 20;
            bezierPoints.cp1[1] += 80;
            bezierPoints.cp2[0] += -20;
            bezierPoints.cp2[1] += 80;
        }else{ //source is to the right of the target
            bezierPoints.cp1[0] += -20;
            bezierPoints.cp1[1] += 80;
            bezierPoints.cp2[0] += 20;
            bezierPoints.cp2[1] += 80;
        }

        textDy = 0;

        bezierPoints.end[0] += 0;
        bezierPoints.end[1] += 20;
        linkData = `M ${bezierPoints.start[0]},${bezierPoints.start[1]} C ${bezierPoints.cp1[0]} ${bezierPoints.cp1[1]} ${bezierPoints.cp2[0]} ${bezierPoints.cp2[1]} ${bezierPoints.end[0]} ${bezierPoints.end[1]}`;
        bezierPointsQuadratic.start[1] += 20;
        bezierPointsQuadratic.end[1] += 20;

        //linkData = `M  ${bezierPointsQuadratic.start[0]},${bezierPointsQuadratic.start[1]} Q ${bezierPointsQuadratic.cp[0]} ${bezierPointsQuadratic.cp[1]} ${bezierPointsQuadratic.end[0]} ${bezierPointsQuadratic.end[1]}`;

    }else if(link.source.nodeLevel > link.target.nodeLevel){//if source is above target
        //LinkComponent = LinkVertical;
        //Should also check whether target is to the right or left
        bezierPoints.start[0] += 0;
        bezierPoints.start[1] += 15;
        //If source is to the left of the target
        if(link.source.anchors.from < link.target.anchors.from){
            bezierPoints.cp1[0] += 0;
            bezierPoints.cp1[1] += 45;
            bezierPoints.cp2[0] += 0;
            bezierPoints.cp2[1] += -45;
        }else{ //source is to the right of the target
            bezierPoints.cp1[0] += 0;
            bezierPoints.cp1[1] += 45;
            bezierPoints.cp2[0] += 0;
            bezierPoints.cp2[1] += -45;
        }

        bezierPoints.end[0] += 0;
        bezierPoints.end[1] += -20;
        linkData = `M ${bezierPoints.start[0]},${bezierPoints.start[1]} C ${bezierPoints.cp1[0]} ${bezierPoints.cp1[1]} ${bezierPoints.cp2[0]} ${bezierPoints.cp2[1]} ${bezierPoints.end[0]} ${bezierPoints.end[1]}`;
    }else if(link.source.nodeLevel < link.target.nodeLevel){//if source is below target
        bezierPoints.start[0] += 0;
        bezierPoints.start[1] -= 15;
        //If source is to the left of the target
        if(link.source.anchors.from < link.target.anchors.from){
            bezierPoints.cp1[0] += 20;
            bezierPoints.cp1[1] -= 45;
            bezierPoints.cp2[0] += -20;
            bezierPoints.cp2[1] += 45;
        }else{ //source is to the right of the target
            bezierPoints.cp1[0] += -20;
            bezierPoints.cp1[1] += -45;
            bezierPoints.cp2[0] += 20;
            bezierPoints.cp2[1] += 45;
        }
        bezierPoints.end[0] += 0;
        bezierPoints.end[1] += 20;
        linkData = `M ${bezierPoints.start[0]},${bezierPoints.start[1]} C ${bezierPoints.cp1[0]} ${bezierPoints.cp1[1]} ${bezierPoints.cp2[0]} ${bezierPoints.cp2[1]} ${bezierPoints.end[0]} ${bezierPoints.end[1]}`;
    }else{
        bezierPoints.start[0] += 0;
        bezierPoints.start[1] += 15;
        //If source is to the left of the target
        if(link.source.anchors.from < link.target.anchors.from){
            bezierPoints.cp1[0] += 20;
            bezierPoints.cp1[1] += 45;
            bezierPoints.cp2[0] += -20;
            bezierPoints.cp2[1] += 45;
        }else{ //source is to the right of the target
            bezierPoints.cp1[0] += 20;
            bezierPoints.cp1[1] += 45;
            bezierPoints.cp2[0] += -20;
            bezierPoints.cp2[1] += 45;
        }

        bezierPoints.end[0] += 0;
        bezierPoints.end[1] += 15;
        linkData = `M ${bezierPoints.start[0]},${bezierPoints.start[1]} C ${bezierPoints.cp1[0]} ${bezierPoints.cp1[1]} ${bezierPoints.cp2[0]} ${bezierPoints.cp2[1]} ${bezierPoints.end[0]} ${bezierPoints.end[1]}`;
    }

    let midPointX = Math.pow(0.5,3)*bezierPoints.start[0] + 3*Math.pow(0.5,2)*0.5*bezierPoints.cp1[0]+ 3*Math.pow(0.5,3)*bezierPoints.cp2[0] + Math.pow(0.5,3)*bezierPoints.end[0];
    let midPointY = Math.pow(0.5,3)*bezierPoints.start[1] + 3*Math.pow(0.5,2)*0.5*bezierPoints.cp1[1]+ 3*Math.pow(0.5,3)*bezierPoints.cp2[1] + Math.pow(0.5,3)*bezierPoints.end[1];

    let LinkComponent = LinkVerticalCurve;
    return (

        <Group>
          <defs>
              <marker
                  id="marker-arrow"
                  markerWidth="10"
                  markerHeight="10"
                  refX="0"
                  refY="3"
                  orient="auto"
                  markerUnits="strokeWidth"
                  viewBox="0 0 20 20">
                  <path d="M0,0 L0,6 L9,3 z" fill="White" />
              </marker>
          </defs>
          <path
              id={`${link.source.id}${link.target.id}`}//unique id combination
              d={linkData}
              fill="none"
              stroke="White"
              strokeWidth={2}
              markerEnd="url(#marker-arrow)"
          />
            <Text
                fill="Black"
                x={midPointX}
                y={midPointY}
                dx={textDx}
                dy={textDy}
                fontFamily="Arial"
                textAnchor={"start"}
                verticalAnchor={"start"}
                style={{ pointerEvents: "none" }}
            >
                {link.label}
            </Text>
            {/*<text
              x={midPointX}
              y={midPointY}
              dx={textDx}
              dy={textDy}
              fill="Black"
              fontFamily="Arial"
              style={{ pointerEvents: "none" }}
          >
              {link.label}
          </text>*/}
      </Group>
      /*<Group>
          <MarkerArrow id="marker-arrow" fill="White" size={5} />
          <Line
          from={{x:fromX, y:fromY}}
          to={{x:toX, y:toY}}
          stroke="White"
          strokeWidth={2}
          markerEnd="url(#marker-arrow)"
          curve={curveNatural}
          />
      </Group>*/
      /*<Group>
          <MarkerArrow id="marker-arrow" fill="White" size={5} />
          <LinkComponent
              data={link}
              stroke="White"
              strokeWidth={2}
              fill="none"
              markerEnd="url(#marker-arrow)"

          />
      </Group>*/

  );

  //return <LinkHorizontalCurve data={link} link={link} />;
};

function Visualization(props) {
  const [showMiniMap, setShowMiniMap] = useState(true);

  return (
    <React.Fragment>
      <Zoom
        width={props.width}
        height={props.height}
        scaleXMin={1 / 2}
        scaleXMax={4}
        scaleYMin={1 / 2}
        scaleYMax={4}
        transformMatrix={initialTransform}
      >
        {(zoom) => (
          <div className="relative">
            <svg
              width={props.width}
              height={props.height}
              style={{ cursor: zoom.isDragging ? "grabbing" : "grab" }}
            >
                <defs>
                    <marker
                        id="arrow"
                        viewBox="0 -5 15 15"
                        refX="0"
                        refY="0"
                        markerWidth="10"
                        markerHeight="10"
                        orient="auto"
                        fill="#fff"
                    >
                        <path d="M0,-5L10,0L0,5" />
                    </marker>
                </defs>
              <RectClipPath
                id="zoom-clip"
                width={props.width}
                height={props.height}
              />
              <LinearGradient id="links-gradient" from="#fd9b93" to="#fe6e9e" />
              <rect
                width={props.width}
                height={props.height}
                rx={14}
                fill="#3f51b5"
              />
              <g transform={zoom.toString()}>
                <Graph
                  graph={props.graph}
                  linkComponent={Link}
                  nodeComponent={Node}
                />
              </g>
              <rect
                width={props.width}
                height={props.height}
                rx={14}
                fill="transparent"
                onTouchStart={zoom.dragStart}
                onTouchMove={zoom.dragMove}
                onTouchEnd={zoom.dragEnd}
                onMouseDown={zoom.dragStart}
                onMouseMove={zoom.dragMove}
                onMouseUp={zoom.dragEnd}
                onMouseLeave={() => {
                  if (zoom.isDragging) zoom.dragEnd();
                }}
                onDoubleClick={(event) => {
                  const point = localPoint(event) || { x: 0, y: 0 };
                  zoom.scale({ scaleX: 1.1, scaleY: 1.1, point });
                }}
              />
              {showMiniMap && (
                <g
                  clipPath="url(#zoom-clip)"
                  transform={`
                    scale(0.25)
                    translate(${props.width * 4 - props.width - 60}, ${
                    props.height * 4 - props.height - 60
                  })
                  `}
                >
                  <rect
                    width={props.width}
                    height={props.height}
                    fill="#1a1a1a"
                  />
                  <Graph
                    graph={props.graph}
                    linkComponent={Link}
                    nodeComponent={Node}
                  />
                  <rect
                    width={props.width}
                    height={props.height}
                    fill="white"
                    fillOpacity={0.2}
                    stroke="white"
                    strokeWidth={4}
                    transform={zoom.toStringInvert()}
                  />
                </g>
              )}
            </svg>
            <div className="controls">
              <button
                className="btn btn-zoom"
                onClick={() => zoom.scale({ scaleX: 1.2, scaleY: 1.2 })}
              >
                +
              </button>
              <button
                className="btn btn-zoom btn-bottom"
                onClick={() => zoom.scale({ scaleX: 0.8, scaleY: 0.8 })}
              >
                -
              </button>
              <button className="btn btn-lg" onClick={zoom.center}>
                Center
              </button>
              <button className="btn btn-lg" onClick={zoom.reset}>
                Reset
              </button>
              <button className="btn btn-lg" onClick={zoom.clear}>
                Clear
              </button>
            </div>
            <div className="mini-map">
              <button
                className="btn btn-lg"
                onClick={() => setShowMiniMap(!showMiniMap)}
              >
                {showMiniMap ? "Hide" : "Show"} Mini Map
              </button>
            </div>
          </div>
        )}
      </Zoom>
      <style>{`
        .btn {
          margin: 0;
          text-align: center;
          border: none;
          background: #2f2f2f;
          color: #888;
          padding: 0 4px;
          border-top: 1px solid #0a0a0a;
        }
        .btn-lg {
          font-size: 12px;
          line-height: 1;
          padding: 4px;
        }
        .btn-zoom {
          width: 26px;
          font-size: 22px;
        }
        .btn-bottom {
          margin-bottom: 1rem;
        }
        .description {
          font-size: 12px;
          margin-right: 0.25rem;
        }
        .controls {
          position: absolute;
          top: 15px;
          right: 15px;
          display: flex;
          flex-direction: column;
          align-items: flex-end;
        }
        .mini-map {
          position: absolute;
          bottom: 25px;
          right: 15px;
          display: flex;
          flex-direction: column;
          align-items: flex-end;
        }
        .relative {
          position: relative;
        }
      `}</style>
    </React.Fragment>
  );
}

const styles = {
  Paper: {
    padding: 20,
    marginBottom: 10,
    height: "100%",
    overflow: "auto",
  },
  TextareaAutosize: {
    width: "100%",
  },
};

const useStyles = makeStyles((theme) => ({
  backdrop: {
    zIndex: theme.zIndex.drawer + 1,
    color: "#fff",
  },
}));

function DiagramArea(props) {
  const classes = useStyles();

  return (
    <div>
      <Backdrop
        className={classes.backdrop}
        open={props.selectingSubset}
        onClick={props.subsetHandleClose}
      >
        <svg style={{ border: "2px solid gold" }} />
      </Backdrop>
    </div>
  );
}

class VisualizerArea extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { sentence } = this.props;

    return (
      <Paper style={styles.Paper} variant="elevation" elevation={5}>
        <Typography variant="h6" align="center">
          Visualization Area
        </Typography>
        <Divider />
        <Accordion>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-controls="visualization-controls"
            id="visualization-controls-header"
          >
            <Typography>Visualization Control Panel</Typography>
          </AccordionSummary>
          <AccordionDetails>
            <FormControl component="fieldset">
              <FormLabel component="legend">
                Select visualization format
              </FormLabel>
              <RadioGroup
                row
                aria-label="visualization-format"
                name="format"
                defaultValue="format1"
              >
                <FormControlLabel
                  value="format1"
                  control={<Radio color="primary" />}
                  label="Flat/Hierarchical"
                />
                <FormControlLabel
                  value="format2"
                  control={<Radio color="primary" />}
                  label="Tree-like"
                />
              </RadioGroup>
            </FormControl>
          </AccordionDetails>
        </Accordion>
        <Divider />
        <TextareaAutosize
          style={styles.TextareaAutosize}
          rowsMax={3}
          aria-label="graph data"
          placeholder="graph data"
          value={JSON.stringify(sentence)}
        />
        <Divider />
        <DiagramArea
          selectingSubset={this.props.selectingSubset}
          subsetHandleClose={this.props.subsetHandleClose}
        />
        <div>diagram to go here</div>
        <TextareaAutosize
          style={styles.TextareaAutosize}
          rowsMax={3}
          aria-label="graph visualization debug"
          placeholder="graph visualization debug"
          value={
            JSON.stringify(testGraphNodes) +
            "\n" +
            JSON.stringify(testGraphEdges) +
            "\n"
          }
        />

        <ParentSize height="480">
          {({ width, height }) => (
            <Visualization width={width} height={480} graph={testGraphData} />
          )}
        </ParentSize>

        {/*<svg width="800" height="500">
          <rect width="780" height="500" rx={14} fill="#272b4d" />
          <Visualization width="780" height="450" graph={testGraphData} />
        </svg>*/}
      </Paper>
    );
  }
}

export default VisualizerArea;
