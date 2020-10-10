import React, {useContext} from "react";
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

import {AppContext} from "../../Store/AppContextProvider.js";
import DisplaySubsetTool from "../AnalysisComponents/DisplaySubsetTool";
import FormalTestsTool from "../AnalysisComponents/FormalTestsTool";
import {Grade} from "@material-ui/icons";
import SearchSubgraphPatternTool from "../AnalysisComponents/SearchSubgraphPatternTool";
import IconButton from "@material-ui/core/IconButton";
import InfoIcon from '@material-ui/icons/Info';
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import SubsetVisualisation from "./SubsetVisualisation";
import DialogActions from "@material-ui/core/DialogActions";
import Button from "@material-ui/core/Button";
import FormControl from "@material-ui/core/FormControl";
import CompareTwoGraphsTool from "../AnalysisComponents/CompareTwoGraphsTool";

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
    const {state, dispatch} = useContext(AppContext);
    const [open, setOpen] = React.useState(false);
    const [infoClicked, setInfoClicked] = React.useState("");

    function handleClose(){
      setOpen(false);
    }

   function handleInfoClick(type){
        setInfoClicked(type);
        setOpen(true);
    }

    return (
        <Grid item xs={12}>
            <Dialog
                open={open}
                fullWidth={true}
                maxWidth="sm"
                onClose={handleClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">{`Some information about the ${infoClicked}:`}</DialogTitle>
                <DialogContent >
                    <Typography>blah blah blah</Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="primary" autoFocus >
                        close
                    </Button>
                </DialogActions>
            </Dialog>
            <Paper elevation={1}>
                <Accordion>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon/>}
                        aria-controls="panel1a-content"
                        id="display-subset-header"
                    >
                        <Typography className={classes.heading}>
                            Display a subset of a graph
                        </Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                        <Grid
                            container
                            direction="row"
                            justify="space-between"
                            alignItems="flex-start"
                            spacing={4}
                        >
                            <Grid item>
                                <Typography>
                                    Select a node on the graph displayed in the visualization area to
                                    see the corresponding subset of the graph:
                                    <IconButton aria-label="Display subset information button" onClick={() => handleInfoClick("display subset tool")}>
                                        <InfoIcon />
                                    </IconButton>
                                </Typography>
                            </Grid>
                            <Grid item>
                                <DisplaySubsetTool/>
                            </Grid>
                        </Grid>
                    </AccordionDetails>
                </Accordion>
                <Accordion>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon/>}
                        aria-controls="panel2a-content"
                        id="search-sub-graph-header"
                    >
                        <Typography className={classes.heading}>
                            Search for a sub-graph pattern
                        </Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                        <Grid
                            container
                            direction="row"
                            justify="space-between"
                            alignItems="flex-start"
                            spacing={4}
                        >
                            <Grid item>
                                <Typography>
                                    Search for a sub-graph pattern using the nodes and labels of the current graph:
                                    <IconButton aria-label="Search for sub-graph pattern information button" onClick={() => handleInfoClick("search for sub-graph pattern tool")}>
                                        <InfoIcon />
                                    </IconButton>
                                </Typography>
                            </Grid>
                            <Grid item>
                                <SearchSubgraphPatternTool />
                            </Grid>
                        </Grid>
                    </AccordionDetails>
                </Accordion>
                <Accordion>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon/>}
                        aria-controls="panel2a-content"
                        id="compare-two-header"
                    >
                        <Typography className={classes.heading}>
                            Compare Two Graphs
                        </Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                        <Grid
                            container
                            direction="row"
                            justify="space-between"
                            alignItems="flex-start"
                            spacing={4}
                        >
                            <Grid item>
                                <Typography>Select a two graphs below:
                                    <IconButton aria-label="Compare two graphs information button" onClick={() => handleInfoClick("compare two graphs tool")}>
                                        <InfoIcon />
                                    </IconButton>
                                </Typography>
                            </Grid>
                            <Grid item>
                                <CompareTwoGraphsTool/>
                            </Grid>
                        </Grid>

                    </AccordionDetails>
                </Accordion>
                <Accordion>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon/>}
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
                            <IconButton aria-label="Formal tests information button" onClick={() => handleInfoClick("formal tests tool")}>
                                <InfoIcon />
                            </IconButton>
                        </Typography>
                        <FormalTestsTool/>
                    </AccordionDetails>
                </Accordion>
            </Paper>
        </Grid>
    );
}
