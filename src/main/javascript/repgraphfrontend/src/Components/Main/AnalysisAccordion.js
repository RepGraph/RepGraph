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

import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
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
import FormalTestResultsDisplay from "../AnalysisComponents/FormalTestResultsDisplay";

const useStyles = makeStyles((theme) => ({
    root: {
        width: "100%",
        height: "100%",
    },
    rootJustWidth: {
        width: "100%",
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
        fontWeight: theme.typography.fontWeightRegular
    },
    header: {
        display: "flex",
        alignItems: "center",
        height: 50,
        paddingLeft: theme.spacing(4),
        backgroundColor: theme.palette.background.default
    },
    body: {
        height: "100%",
        width: "100%",
    },
    test: {
        height: "100%",
        width: "100%",
        border: "1px solid red"
    }
}));

export default function AnalysisAccordion() {
    const classes = useStyles();
    const {state, dispatch} = useContext(AppContext);
    const [open, setOpen] = React.useState(false);
    const [infoClicked, setInfoClicked] = React.useState("");

    function handleClose() {
        setOpen(false);
    }

    function handleInfoClick(type) {
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
                <DialogContent>
                    <Typography>blah blah blah</Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="primary" autoFocus>
                        close
                    </Button>
                </DialogActions>
            </Dialog>
            <Paper elevation={1}>
                <Accordion>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon/>}
                        aria-controls="panel2a-content"
                        id="display-subset-header"
                    >
                        <Typography className={classes.heading}>Display a subset of a graph</Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                        <Grid
                            className={classes.rootJustWidth}
                            container
                            direction="row"
                            justify="space-between"
                            alignItems="center"
                            spacing={2}
                        >
                            <Grid item xs={6} className={classes.body}>
                                <Card className={classes.body} variant="outlined">
                                    <CardContent className={classes.body}>
                                        <Typography
                                            className={classes.title}
                                            color="textPrimary"
                                            gutterBottom
                                        >
                                            About the tool:
                                            <IconButton aria-label="Display subset information button"
                                                        onClick={() => handleInfoClick("display subset tool")}>
                                                <InfoIcon/>
                                            </IconButton>
                                        </Typography>
                                        <Typography variant="body2" color="textSecondary">
                                            Select a node on the graph displayed in the visualization area to
                                            see the corresponding subset of the graph.
                                        </Typography>
                                    </CardContent>
                                </Card>
                            </Grid>
                            <Grid item xs={6} style={{height: "100%"}}>
                                <Card className={classes.body} variant="outlined">
                                    <CardContent>
                                        <DisplaySubsetTool/>
                                    </CardContent>
                                </Card>
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
                            className={classes.rootJustWidth}
                            container
                            direction="row"
                            justify="space-between"
                            alignItems="center"
                            spacing={2}
                        >
                            <Grid item xs={6} style={{height: "100%"}}>
                                <Card className={classes.body} variant="outlined">
                                    <CardContent>
                                        <Typography
                                            className={classes.title}
                                            color="textPrimary"
                                            gutterBottom
                                        >
                                            About the tool:
                                            <IconButton aria-label="Search for sub-graph pattern information button"
                                                        onClick={() => handleInfoClick("search for sub-graph pattern tool")}>
                                                <InfoIcon/>
                                            </IconButton>
                                        </Typography>
                                        <Typography variant="body2" color="textSecondary">
                                            Search for a sub-graph pattern using the nodes and labels of the current
                                            graph.
                                        </Typography>
                                    </CardContent>
                                </Card>
                            </Grid>
                            <Grid item xs={6} className={classes.body}>
                                <div className={classes.body}>
                                    <SearchSubgraphPatternTool/>
                                </div>
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
                            className={classes.rootJustWidth}
                            container
                            direction="row"
                            justify="space-between"
                            alignItems="center"
                            spacing={2}
                        >
                            <Grid item xs={6} style={{height: "100%"}}>
                                <Card className={classes.body} variant="outlined">
                                    <CardContent>
                                        <Typography
                                            className={classes.title}
                                            color="textPrimary"
                                            gutterBottom
                                        >
                                            About the tool:
                                            <IconButton aria-label="Compare two graphs information button"
                                                        onClick={() => handleInfoClick("compare two graphs tool")}>
                                                <InfoIcon/>
                                            </IconButton>
                                        </Typography>
                                        <Typography variant="body2" color="textSecondary">
                                            Click the button to compare the similarities and differences of any two
                                            graphs.
                                        </Typography>
                                    </CardContent>
                                </Card>
                            </Grid>
                            <Grid item xs={6} className={classes.body}>
                                <Card className={classes.body} variant="outlined">
                                    <CardContent className={classes.body}>
                                        <CompareTwoGraphsTool/>
                                    </CardContent>
                                </Card>
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
                        <Grid
                            className={classes.root}
                            container
                            direction="column"
                            justify="center"
                            alignItems="center"
                            spacing={2}
                        >
                            <Grid container item xs={12}>
                                <Grid
                                    className={classes.root}
                                    container
                                    direction="row"
                                    justify="flex-start"
                                    alignItems="stretch"
                                >
                                    <Grid item xs style={{marginRight: "15px"}}>
                                        <Card className={classes.body} variant="outlined">
                                            <CardContent className={classes.body}>
                                                <Typography
                                                    className={classes.title}
                                                    color="textPrimary"
                                                    gutterBottom
                                                >
                                                    About the tool:
                                                    <IconButton aria-label="Formal tests information button" onClick={() => handleInfoClick("formal tests tool")}>
                                                        <InfoIcon />
                                                    </IconButton>
                                                </Typography>
                                                <Typography variant="body2" color="textSecondary">
                                                    Select a number of graph properties with which to test the
                                                    currently displayed graph.
                                                </Typography>
                                            </CardContent>
                                        </Card>
                                    </Grid>
                                    <Grid item xs>
                                        <Card className={classes.body} variant="outlined">
                                            <CardContent>
                                                <FormalTestsTool/>
                                            </CardContent>
                                        </Card>
                                    </Grid>
                                </Grid>
                            </Grid>
                            {state.testResults !== null &&
                            <Grid container item xs={12}>
                                <Card className={classes.body} variant="outlined">
                                    <CardContent>
                                        <FormalTestResultsDisplay response={state.testResults}/>
                                    </CardContent>
                                </Card>
                            </Grid>}
                        </Grid>
                    </AccordionDetails>
                </Accordion>
            </Paper>
        </Grid>
    );
}
