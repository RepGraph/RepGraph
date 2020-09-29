import React, { useContext } from "react";
import makeStyles from "@material-ui/core/styles/makeStyles";
import {
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Grid,
  Paper,
  Typography
} from "@material-ui/core";

import ExpandMoreIcon from "@material-ui/icons/ExpandMore";

import { AppContext } from "../../Store/AppContextProvider.js";

const useStyles = makeStyles((theme) => ({
  root: {
    width: "100%"
  },
  heading: {
    fontSize: theme.typography.pxToRem(15),
    fontWeight: theme.typography.fontWeightRegular
  }
}));

export default function AnalysisAccordion() {
  const classes = useStyles();
  const { state, dispatch } = useContext(AppContext);

  return (
    <Grid item xs={12}>
      <Paper elevation={1}>
        <Accordion>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-controls="panel1a-content"
            id="display-subset-header"
          >
            <Typography className={classes.heading}>
              Display a subset of a graph
            </Typography>
          </AccordionSummary>
          <AccordionDetails>
            <Typography>
              Select a node on the graph displayed in the visualization area to
              see the corresponding subset of the graph:
            </Typography>
          </AccordionDetails>
        </Accordion>
        <Accordion>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-controls="panel2a-content"
            id="search-sub-graph-header"
          >
            <Typography className={classes.heading}>
              Search for a sub-graph pattern
            </Typography>
          </AccordionSummary>
          <AccordionDetails>
            <Typography>
              Manually select a number of node labels to search against the
              uploaded data-set:
            </Typography>
          </AccordionDetails>
        </Accordion>
        <Accordion>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-controls="panel2a-content"
            id="compare-two-header"
          >
            <Typography className={classes.heading}>
              Compare Two Graphs
            </Typography>
          </AccordionSummary>
          <AccordionDetails>
            <Typography>Select a two graphs below:</Typography>
          </AccordionDetails>
        </Accordion>
        <Accordion>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-controls="panel2a-content"
            id="run-formal-header"
          >
            <Typography className={classes.heading}>
              Run Formal Tests
            </Typography>
          </AccordionSummary>
          <AccordionDetails>
            <Typography>
              Select a number of graph properties with which to test the
              currently displayed graph:
            </Typography>
          </AccordionDetails>
        </Accordion>
      </Paper>
    </Grid>
  );
}
