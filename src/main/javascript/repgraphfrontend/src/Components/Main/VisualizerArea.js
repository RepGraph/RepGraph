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
import { Group } from "@vx/group";
import { Text } from "@vx/text";
import { LinkHorizontalStep } from "@vx/shape";
import { Zoom } from "@vx/zoom";
import { localPoint } from "@vx/event";
import { RectClipPath } from "@vx/clip-path";
import { LinearGradient } from "@vx/gradient";
import { ParentSize } from "@vx/responsive";

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

const testGraphNodes = testGraph.nodes.map((node) => ({
  x: Math.random() * 780,
  y: Math.random() * 500,
  label: node.label,
}));

const testGraphEdges = testGraph.edges.map((edge) => ({
  source: testGraphNodes[edge.source],
  target: testGraphNodes[edge.target],
}));

const numTokens = testGraph.tokens.length;

const testGraphTokens = testGraph.tokens;

//.map((token) => ({index: token.index, form: token.form}));

const testGraphData = {
  nodes: testGraphNodes,
  links: testGraphEdges,
};

const Node = ({ node }) => {
  return (
    <svg width="80" height="80">
      <defs>
        <marker
          id="arrow"
          viewBox="0 -5 10 10"
          refX="8"
          refY="-.3"
          markerWidth="6"
          markerHeight="6"
          orient="auto"
          fill="White"
        >
          <path d="M0,-5L10,0L0,5" />
        </marker>
      </defs>
      <rect
        x="0"
        y="0"
        rx="10"
        ry="10"
        width="80"
        height="30"
        fill="#FFE521"
        stroke-width="0"
        stroke="#000"
      />
      {node.label && (
        <Text
          fill="Black"
          dx="7"
          dy="7"
          scaleToFit={true}
          fontFamily="Arial"
          textAnchor={"start"}
          verticalAnchor={"start"}
          style={{ pointerEvents: "none" }}
        >
          {node.label}
        </Text>
      )}
    </svg>
  );
};

const Link = ({ link }) => {
  return (
    <line
      x1={link.source.x}
      y1={link.source.y}
      x2={link.target.x}
      y2={link.target.y}
      strokeWidth={2}
      stroke="White"
      strokeOpacity={1}
      marker-end="url(#arrow)"
    />
  );

  return <DefaultLink link={link} />;
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
                fill="#272b4d"
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
      <style jsx>{`
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
          defaultValue="graph data"
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
          defaultValue="graph visualization debug"
          value={
            JSON.stringify(testGraphNodes) +
            "\n" +
            JSON.stringify(testGraphEdges)
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
