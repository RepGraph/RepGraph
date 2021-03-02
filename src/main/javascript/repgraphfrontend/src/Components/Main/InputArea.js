import React from 'react';
import Typography from "@material-ui/core/Typography";
import Paper from "@material-ui/core/Paper";
import Divider from '@material-ui/core/Divider';
import List from '@material-ui/core/List';
import Tooltip from "@material-ui/core/Tooltip";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Zoom from "@material-ui/core/Zoom";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";

import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import AccordionDetails from "@material-ui/core/AccordionDetails";
import makeStyles from "@material-ui/core/styles/makeStyles";
import {FormatListNumbered} from "@material-ui/icons";
import FormalTestsTool from "../AnalysisComponents/FormalTestsTool";
import SearchSubgraphPatternTool from "../AnalysisComponents/SearchSubgraphPatternTool";
import DisplaySubsetTool from "../AnalysisComponents/DisplaySubsetTool";
import CompareTwoGraphsTool from "../AnalysisComponents/CompareTwoGraphsTool";
import Grid from "@material-ui/core/Grid";

const styles = {
    Paper: {
        padding: 20,
        marginBottom: 10,
        height: "100%",
        overflow:'auto'
    },
    SentencePaper: {
        height: 300,
        overflow: 'auto'
    }
};

class InputArea extends React.Component
{

    constructor(props) {
        super(props);
    }

    render(){

        const parsedSentences = this.props.sentences;

        const onSelect = this.props.onSelect;

        return (
            <Paper style={styles.Paper} variant="elevation" elevation={5}>
                <Grid container direction="column" justify="space-between" alignItems="center" style={{width:"100%"}}>
                    <Grid item style={{width:"100%"}}>
                        <Typography variant="h6" align="center">Select a Sentence for Visualization:</Typography>
                        <Divider/>
                    </Grid>
                    <Grid item style={{width:"100%"}}>
                        <List component="nav" aria-label="features">
                            <Paper style={styles.SentencePaper}>
                                <List component="ul">
                                    {parsedSentences.map((sentence)=>
                                        <Tooltip TransitionComponent={Zoom} title="Display Graph >" placement="right">
                                            <div>
                                                <ListItem button key={sentence.id} onClick={()=>onSelect(sentence.id)}>
                                                    <ListItemText
                                                        primary={sentence.input}
                                                    >
                                                    </ListItemText>
                                                </ListItem>
                                                <Divider/>
                                            </div>
                                        </Tooltip>)}
                                </List>
                            </Paper>
                        </List>
                        <Divider/>
                    </Grid>
                    <Grid item style={{width:"100%"}}>
                        <Typography variant="h6" align="center">Analysis Tools:</Typography>
                        <Divider/>
                    </Grid>
                    <Grid item style={{width:"100%"}}>
                        <AnalysisAccordion sentence={this.props.sentence} sentences={this.props.sentences} onClickSubset={this.props.subsetHandleToggle}/>
                    </Grid>
                </Grid>
            </Paper>
        );
    }
}

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%'
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
        fontWeight: theme.typography.fontWeightRegular,
    },
}));

function AnalysisAccordion(props) {
    const classes = useStyles();

    return (
        <div className={classes.root}>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    aria-controls="panel1a-content"
                    id="display-subset-header"
                >
                    <Typography className={classes.heading}>Display a subset of a graph</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Typography>Select a node on the graph displayed in the visualization area to see the corresponding subset of the graph:</Typography>
                </AccordionDetails>
                <DisplaySubsetTool onClick={props.onClickSubset}/>
            </Accordion>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    aria-controls="panel2a-content"
                    id="search-sub-graph-header"
                >
                    <Typography className={classes.heading}>Search for a sub-graph pattern</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Typography>Manually select a number of node labels to search against the uploaded data-set:</Typography>
                </AccordionDetails>
                <SearchSubgraphPatternTool sentence={props.sentence} onClick={props.onClickSubset}/>
            </Accordion>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    aria-controls="panel2a-content"
                    id="compare-two-header"
                >
                    <Typography className={classes.heading}>Compare Two Graphs</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Typography>Select a two graphs below:</Typography>
                </AccordionDetails>
                <CompareTwoGraphsTool sentences={props.sentences}/>
            </Accordion>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    aria-controls="panel2a-content"
                    id="run-formal-header"
                >
                    <Typography className={classes.heading}>Run Formal Tests</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Typography>Select a number of graph properties with which to test the currently displayed graph:</Typography>
                </AccordionDetails>
                <FormalTestsTool/>

            </Accordion>

        </div>
    );
}

export default InputArea;