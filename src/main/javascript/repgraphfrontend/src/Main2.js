import React, {useState, useContext} from 'react';
import clsx from 'clsx';
import {makeStyles, useTheme} from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import List from '@material-ui/core/List';
import CssBaseline from '@material-ui/core/CssBaseline';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import SearchIcon from '@material-ui/icons/Search';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import CompareArrowsIcon from '@material-ui/icons/CompareArrows';
import SettingsIcon from '@material-ui/icons/Settings';
import AssessmentIcon from '@material-ui/icons/Assessment';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import InboxIcon from '@material-ui/icons/MoveToInbox';
import MailIcon from '@material-ui/icons/Mail';
import SelectAllIcon from '@material-ui/icons/SelectAll';
import {AppContext} from "./Store/AppContextProvider";
import {Button, Card, CardContent, Chip, Grid} from "@material-ui/core";
import Fab from "@material-ui/core/Fab";
import Popover from "@material-ui/core/Popover";
import Tooltip from "@material-ui/core/Tooltip";
import ToggleButtonGroup from "@material-ui/lab/ToggleButtonGroup";
import ToggleButton from "@material-ui/lab/ToggleButton";
import AddCircleOutlineIcon from "@material-ui/icons/AddCircleOutline";
import BuildIcon from "@material-ui/icons/Build";
import EjectIcon from '@material-ui/icons/Eject';
import EditIcon from '@material-ui/icons/Edit';
import {determineAdjacentLinks, layoutHierarchy} from "./LayoutAlgorithms/layoutHierarchy";
import {layoutTree} from "./LayoutAlgorithms/layoutTree";
import {layoutFlat} from "./LayoutAlgorithms/layoutFlat";
import {useHistory} from "react-router-dom";
import {Graph} from "./Components/Graph/Graph";
import CloudUploadIcon from "@material-ui/icons/CloudUpload";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import SentenceList from "./Components/Main/SentenceList";
import DialogActions from "@material-ui/core/DialogActions";
import Dialog from "@material-ui/core/Dialog";

import {ParentSize} from '@visx/responsive';
import InfoIcon from "@material-ui/icons/Info";
import SettingsTool from "./Components/AnalysisComponents/SettingsTool";
import DisplaySubsetTool from "./Components/AnalysisComponents/DisplaySubsetTool";
import SearchSubgraphPatternTool from "./Components/AnalysisComponents/SearchSubgraphPatternTool";
import CompareTwoGraphsTool from "./Components/AnalysisComponents/CompareTwoGraphsTool";
import FormalTestsTool from "./Components/AnalysisComponents/FormalTestsTool";
import FormalTestResultsDisplay from "./Components/AnalysisComponents/FormalTestResultsDisplay";

import useMediaQuery from '@material-ui/core/useMediaQuery';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import PopupState, {bindTrigger, bindPopover} from 'material-ui-popup-state';
import Box from "@material-ui/core/Box";
import {mdiArrowExpandHorizontal, mdiDatabaseCog} from '@mdi/js';
import Icon from "@mdi/react";

import MinimalFeedback from 'minimal-feedback'
import 'minimal-feedback/dist/index.css' // don't forget to import css
import AssignmentIcon from '@material-ui/icons/Assignment';

import {Octokit} from "@octokit/core";
import DatasetAnalysisFormDisplay from "./Components/AnalysisComponents/DatasetAnalysisDisplay";
import DatasetAnalysisTool from "./Components/AnalysisComponents/DatasetAnalysisTool";

const drawerWidth = 300;

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
    },
    appBar: {
        zIndex: theme.zIndex.drawer + 1,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
    },
    appBarShift: {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    menuButton: {
        marginRight: "1rem"
    },
    hide: {
        display: 'none',
    },
    drawer: {
        width: drawerWidth,
        flexShrink: 0,
        whiteSpace: 'nowrap',
    },
    drawerOpen: {
        width: drawerWidth,
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    drawerClose: {
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        overflowX: 'hidden',
        width: theme.spacing(7) + 1,
        [theme.breakpoints.up('sm')]: {
            width: theme.spacing(9) + 1,
        },
    },
    toolbar: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
        padding: theme.spacing(0, 1),
        // necessary for content to be below app bar
        ...theme.mixins.toolbar,
    },
    content: {
        padding: theme.spacing(1),
        width: "100%",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        //border: "1px solid blue",
        // height: `calc(100vh - ${theme.mixins.toolbar.minHeight}px)`
        height: "100vh"
    },
    graphDiv: {
        height: `calc(100% - ${theme.mixins.toolbar.minHeight}px)`,
        //border: "1px solid red",
        flex: "1",
        width: "100%"
    }
}));

function useWidth() {
    const theme = useTheme();
    const keys = [...theme.breakpoints.keys].reverse();
    return (
        keys.reduce((output, key) => {
            // eslint-disable-next-line react-hooks/rules-of-hooks
            const matches = useMediaQuery(theme.breakpoints.up(key));
            return !output && matches ? key : output;
        }, null) || 'xs'
    );
}

export default function MiniDrawer() {
    const {state, dispatch} = useContext(AppContext); //Provide access to global state
    const classes = useStyles();
    const theme = useTheme();
    const [open, setOpen] = useState(false);
    const [anchorEl, setAnchorEl] = useState(null); //Anchor state for popover graph legend

    const history = useHistory(); //Provide access to router history
    const [sentenceOpen, setSentenceOpen] = useState(false); //Local state of select sentence dialog
    const [dataSetResponseOpen, setDataSetResponseOpen] = useState(true); //Local state for upload dataset alert

    const [subsetDialogOpen, setSubsetDialogOpen] = useState(false); //Local state of subset dialog
    const [subgraphDialogOpen, setSubgraphDialogOpen] = useState(false); //Local state of subgraph dialog
    const [compareDialogOpen, setCompareDialogOpen] = useState(false); //Local state of compare dialog
    const [testsDialogOpen, setTestsDialogOpen] = useState(false); //Local state of tests dialog
    const [datasetAnalysisDialogOpen, setDatasetAnalysisDialogOpen] = useState(false); //Local state of tests dialog
    const [settingsDialogOpen, setSettingsDialogOpen] = useState(false); //Local state of settings dialog
    const [showSettings, setShowSettings] = useState(false); //Local state of settings visibility

    const [feedbackText, setFeedbackText] = useState({feedback: ''})

    //const matches = useMediaQuery('(min-width:1000px)');
    // const matches = useMediaQuery(theme.breakpoints.up('sm'));

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
        setShowSettings(false);
    };

    const legendOpen = Boolean(anchorEl); //State of graph legend
    //Function to handle click on graph legend button
    const handleClickGraphLegend = (event) => {
        setAnchorEl(event.currentTarget);
    };

    //Functions to handle close of graph legend
    const handleCloseGraphLegend = () => {
        setAnchorEl(null);
    };

    //Handle change visualisation format setting in application app bar
    const handleChangeVisualisationFormat = (event, newFormat) => {
        //Enforce one format being selected at all times
        if (newFormat !== null) {
            dispatch({type: "SET_VISUALISATION_FORMAT", payload: {visualisationFormat: newFormat}}); //Set global state for visualisation format

            //Update the currently displayed graph as well
            if (state.selectedSentenceID !== null) {
                let myHeaders = new Headers();
                myHeaders.append("X-USER", state.userID);
                let requestOptions = {
                    method: 'GET',
                    headers: myHeaders,
                    redirect: 'follow'
                };

                dispatch({type: "SET_LOADING", payload: {isLoading: true}}); //Show loading animation

                let graphData = null;
                switch (newFormat) {
                    case "1":
                        graphData = layoutHierarchy(state.selectedSentenceGraphData, state.graphLayoutSpacing);
                        break;
                    case "2":
                        graphData = layoutTree(state.selectedSentenceGraphData, state.graphLayoutSpacing);
                        break;
                    case "3":
                        graphData = layoutFlat(state.selectedSentenceGraphData, false, state.graphLayoutSpacing);
                        break;
                    default:
                        graphData = layoutHierarchy(state.selectedSentenceGraphData, state.graphLayoutSpacing);
                        break;
                }



                dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: graphData}});
                dispatch({type: "SET_LOADING", payload: {isLoading: false}});
            }

        }
    }

    //Handle click close select sentence dialog
    const handleSentenceClose = () => {
        setSentenceOpen(false);
    };

    //Handle click subset tool menu button
    const handleSubsetToolClick = () => {
        setSubsetDialogOpen(true);
    }

    const handleDatasetAnalysisToolClick = () => {
        setDatasetAnalysisDialogOpen(true);
    }
    const handleDatasetAnalysisDialogClose = () => {
        setDatasetAnalysisDialogOpen(false);
    }

    //Handle click close subset sentence dialog
    const handleSubsetDialogClose = () => {
        setSubsetDialogOpen(false);
    };

    //Handle click subgraph tool menu button
    const handleSubgraphToolClick = () => {
        setSubgraphDialogOpen(true);
    }

    //Handle click close subgraph sentence dialog
    const handleSubgraphDialogClose = () => {
        setSubgraphDialogOpen(false);
    };

    //Handle click compare tool menu button
    const handleCompareToolClick = () => {
        setCompareDialogOpen(true);
    }

    //Handle click close compare tool dialog
    const handleCompareToolDialogClose = () => {
        setCompareDialogOpen(false);
    };

    //Handle click compare tool menu button
    const handleTestsToolClick = () => {
        setTestsDialogOpen(true);
    }

    //Handle click close compare tool dialog
    const handleTestsToolDialogClose = () => {
        setTestsDialogOpen(false);
    };

    const handleSettingsClick = () => {
        //setSettingsDialogOpen(true)
        setShowSettings(!showSettings);
        setOpen(true);
    }

    const handleSettingsDialogClose = () => {
        setSettingsDialogOpen(false)
    }

    //Determine graphFormatCode
    let graphFormatCode = null;
    switch (state.visualisationFormat) {
        case "1":
            graphFormatCode = "hierarchical";
            break;
        case "2":
            graphFormatCode = "tree";
            break;
        case "3":
            graphFormatCode = "flat";
            break;
        default:
            graphFormatCode = "hierarchical";
            break;
    }

    const currentScreenWidth = useWidth();

    let toolBarComponent = null;

    switch (currentScreenWidth) {
        case 'xs':
        case 'sm':
            toolBarComponent = <Toolbar>
                <IconButton
                    color="inherit"
                    aria-label="open drawer"
                    onClick={handleDrawerOpen}
                    edge="start"
                    className={clsx(classes.menuButton, {
                        [classes.hide]: open,
                    })}
                >
                    <MenuIcon/>
                </IconButton>
                <div style={{marginRight: "1rem"}}>
                    <Typography variant="h6" noWrap>
                        RepGraph
                    </Typography>
                </div>

                <Grid
                    container
                    direction="row"
                    justify="space-evenly"
                    alignItems="center"
                >
                    <Grid item className={classes.menuButton}>
                        <PopupState variant="popover" popupId="demo-popup-popover">
                            {(popupState) => (
                                <div>
                                    <Button variant="contained" color="primary" disableElevation
                                            startIcon={<Icon path={mdiDatabaseCog} size={1}/>}
                                            endIcon={<ExpandMoreIcon/>}{...bindTrigger(popupState)}>
                                        Data Settings
                                    </Button>
                                    <Popover
                                        {...bindPopover(popupState)}
                                        anchorOrigin={{
                                            vertical: 'bottom',
                                            horizontal: 'center',
                                        }}
                                        transformOrigin={{
                                            vertical: 'top',
                                            horizontal: 'center',
                                        }}
                                    >
                                        <Card variant="outlined" style={{}}>
                                            <CardContent style={{}}>
                                                <Grid
                                                    container
                                                    direction="column"
                                                    justify="center"
                                                    alignItems="center"
                                                    style={{}}
                                                    spacing={2}
                                                >
                                                    <Grid item>
                                                        <div>
                                                            {/*<Fab color="primary" aria-label="add" variant="extended"*/}
                                                            {/*     className={classes.fabButton} onClick={handleClickGraphLegend}>*/}
                                                            {/*    Show Graph Legend*/}
                                                            {/*</Fab>*/}
                                                            <Button color="primary" variant="contained" disableElevation
                                                                    onClick={handleClickGraphLegend}>Show Graph
                                                                Legend</Button>
                                                            <Popover
                                                                open={legendOpen}
                                                                anchorEl={anchorEl}
                                                                onClose={handleCloseGraphLegend}
                                                                anchorOrigin={{
                                                                    vertical: 'bottom',
                                                                    horizontal: 'center',
                                                                }}
                                                                transformOrigin={{
                                                                    vertical: 'top',
                                                                    horizontal: 'center',
                                                                }}
                                                            >
                                                                <Card>
                                                                    <CardContent>
                                                                        <Grid container spacing={1}>
                                                                            <div>This needs to be fixed still</div>
                                                                            <Grid item>
                                                                                <Chip label="AbstractNode" style={{
                                                                                    color: "white",
                                                                                    fontWeight: "bold",
                                                                                    backgroundColor: state.graphStyles.nodeStyles.abstractNodeColour
                                                                                }}/>
                                                                            </Grid>
                                                                            <Grid item>
                                                                                <Chip label="SurfaceNode" style={{
                                                                                    color: "white",
                                                                                    fontWeight: "bold",
                                                                                    backgroundColor: state.graphStyles.nodeStyles.surfaceNodeColour
                                                                                }}/>
                                                                            </Grid>
                                                                            <Grid item>
                                                                                <Chip label="Token" style={{
                                                                                    color: "white",
                                                                                    fontWeight: "bold",
                                                                                    backgroundColor: state.graphStyles.tokenStyles.tokenColour
                                                                                }}/>
                                                                            </Grid>
                                                                        </Grid>
                                                                    </CardContent>
                                                                </Card>
                                                            </Popover>
                                                        </div>
                                                    </Grid>
                                                    <Grid item><Tooltip arrow
                                                                        title={state.dataSet === null ? "Upload data-set" : "Upload new data-set"}>
                                                        {/*<Fab color="primary" aria-label="add" variant="extended"*/}
                                                        {/*     className={classes.fabButton} onClick={() => {*/}
                                                        {/*    history.push("/");*/}
                                                        {/*}}>*/}
                                                        {/*    {state.dataSet === null ? "No Data-set Uploaded" : state.dataSetFileName} {state.dataSet === null ?*/}
                                                        {/*    <CloudUploadIcon/> : <BuildIcon/>}*/}
                                                        {/*</Fab>*/}
                                                        <Button color="primary" variant="contained" disableElevation
                                                                onClick={() => {
                                                                    history.push("/");
                                                                }}>{state.dataSet === null ? "No Data-set Uploaded" : state.dataSetFileName} {state.dataSet === null ?
                                                            <CloudUploadIcon/> : <EjectIcon/>}</Button>
                                                    </Tooltip></Grid>
                                                    <Grid item><Tooltip arrow
                                                                        title={state.selectedSentenceID === null ? "Select Sentence" : "Change Sentence"}>
                                                        {/*<Fab color="primary" aria-label="add" variant="extended"*/}
                                                        {/*     className={classes.fabButton} onClick={() => {*/}
                                                        {/*    setSentenceOpen(true);*/}
                                                        {/*}} disabled={state.dataSet === null}>*/}
                                                        {/*    {state.selectedSentenceID === null ? "No Sentence Selected" : state.selectedSentenceID} {state.selectedSentenceID === null ?*/}
                                                        {/*    <AddCircleOutlineIcon/> :*/}
                                                        {/*    <BuildIcon/>}*/}
                                                        {/*</Fab>*/}

                                                        <Button color="primary" variant="contained" disableElevation
                                                                onClick={() => {
                                                                    setSentenceOpen(true);
                                                                }}
                                                                disabled={state.dataSet === null}>{state.selectedSentenceID === null ? "No Sentence Selected" : state.selectedSentenceID} {state.selectedSentenceID === null ?
                                                            <AddCircleOutlineIcon/> :
                                                            <EditIcon/>}</Button>
                                                    </Tooltip></Grid>
                                                    <Grid item><Tooltip arrow
                                                                        title={"Select visualisation format"}>
                                                        <ToggleButtonGroup
                                                            value={state.visualisationFormat}
                                                            exclusive
                                                            onChange={handleChangeVisualisationFormat}
                                                            aria-label="Visualisation formats"
                                                            color="primary"
                                                        >
                                                            <ToggleButton value="1" aria-label="Hierarchical">
                                                                <Typography
                                                                    color={"textPrimary"}>Hierarchical</Typography>
                                                            </ToggleButton>
                                                            <ToggleButton value="2" aria-label="Tree-like">
                                                                <Typography color={"textPrimary"}>Tree-like</Typography>
                                                            </ToggleButton>
                                                            <ToggleButton value="3" aria-label="Flat">
                                                                <Typography color={"textPrimary"}>Flat</Typography>
                                                            </ToggleButton>
                                                        </ToggleButtonGroup>

                                                    </Tooltip></Grid>
                                                </Grid>
                                            </CardContent>

                                        </Card>

                                    </Popover>
                                </div>
                            )}
                        </PopupState>
                    </Grid>
                </Grid>
            </Toolbar>;
            break;
        case 'md':
            toolBarComponent = <Toolbar>
                <IconButton
                    color="inherit"
                    aria-label="open drawer"
                    onClick={handleDrawerOpen}
                    edge="start"
                    className={clsx(classes.menuButton, {
                        [classes.hide]: open,
                    })}
                >
                    <MenuIcon/>
                </IconButton>
                <div style={{marginRight: "1rem"}}>
                    <Typography variant="h6" noWrap>
                        RepGraph
                    </Typography>
                </div>

                <Grid
                    container
                    direction="row"
                    justify="space-evenly"
                    alignItems="center"
                >
                    <Grid item className={classes.menuButton}><Tooltip arrow
                                                                       title={state.selectedSentenceID === null ? "Select Sentence" : "Change Sentence"}>
                        {/*<Fab color="primary" aria-label="add" variant="extended"*/}
                        {/*     className={classes.fabButton} onClick={() => {*/}
                        {/*    setSentenceOpen(true);*/}
                        {/*}} disabled={state.dataSet === null}>*/}
                        {/*    {state.selectedSentenceID === null ? "No Sentence Selected" : state.selectedSentenceID} {state.selectedSentenceID === null ?*/}
                        {/*    <AddCircleOutlineIcon/> :*/}
                        {/*    <BuildIcon/>}*/}
                        {/*</Fab>*/}

                        <Button color="primary" variant="contained" disableElevation onClick={() => {
                            setSentenceOpen(true);
                        }}
                                disabled={state.dataSet === null}>{state.selectedSentenceID === null ? "No Sentence Selected" : state.selectedSentenceID} {state.selectedSentenceID === null ?
                            <AddCircleOutlineIcon/> :
                            <EditIcon/>}</Button>
                    </Tooltip></Grid>
                    <Grid item className={classes.menuButton}><Tooltip arrow
                                                                       title={"Select visualisation format"}>
                        <ToggleButtonGroup
                            value={state.visualisationFormat}
                            exclusive
                            onChange={handleChangeVisualisationFormat}
                            aria-label="Visualisation formats"
                            color="primary"
                        >
                            <ToggleButton value="1" aria-label="Hierarchical">
                                <Typography color={"textPrimary"}>Hierarchical</Typography>
                            </ToggleButton>
                            <ToggleButton value="2" aria-label="Tree-like">
                                <Typography color={"textPrimary"}>Tree-like</Typography>
                            </ToggleButton>
                            <ToggleButton value="3" aria-label="Flat">
                                <Typography color={"textPrimary"}>Flat</Typography>
                            </ToggleButton>
                        </ToggleButtonGroup>

                    </Tooltip></Grid>
                    <Grid item className={classes.menuButton}>
                        <PopupState variant="popover" popupId="demo-popup-popover">
                            {(popupState) => (
                                <div>
                                    <Button variant="contained" color="primary" disableElevation
                                            startIcon={<Icon path={mdiDatabaseCog} size={1}/>}
                                            endIcon={<ExpandMoreIcon/>}{...bindTrigger(popupState)}>
                                        Data Settings
                                    </Button>
                                    <Popover
                                        {...bindPopover(popupState)}
                                        anchorOrigin={{
                                            vertical: 'bottom',
                                            horizontal: 'center',
                                        }}
                                        transformOrigin={{
                                            vertical: 'top',
                                            horizontal: 'center',
                                        }}
                                    >
                                        <Card variant="outlined" style={{}}>
                                            <CardContent style={{}}>
                                                <Grid
                                                    container
                                                    direction="column"
                                                    justify="center"
                                                    alignItems="center"
                                                    style={{}}
                                                    spacing={2}
                                                >
                                                    <Grid item>
                                                        <div>
                                                            {/*<Fab color="primary" aria-label="add" variant="extended"*/}
                                                            {/*     className={classes.fabButton} onClick={handleClickGraphLegend}>*/}
                                                            {/*    Show Graph Legend*/}
                                                            {/*</Fab>*/}
                                                            <Button color="primary" variant="contained" disableElevation
                                                                    onClick={handleClickGraphLegend}>Show Graph
                                                                Legend</Button>
                                                            <Popover
                                                                open={legendOpen}
                                                                anchorEl={anchorEl}
                                                                onClose={handleCloseGraphLegend}
                                                                anchorOrigin={{
                                                                    vertical: 'bottom',
                                                                    horizontal: 'center',
                                                                }}
                                                                transformOrigin={{
                                                                    vertical: 'top',
                                                                    horizontal: 'center',
                                                                }}
                                                            >
                                                                <Card>
                                                                    <CardContent>
                                                                        <Grid container spacing={1}>
                                                                            <div>This needs to be fixed still</div>
                                                                            <Grid item>
                                                                                <Chip label="AbstractNode" style={{
                                                                                    color: "white",
                                                                                    fontWeight: "bold",
                                                                                    backgroundColor: state.graphStyles.nodeStyles.abstractNodeColour
                                                                                }}/>
                                                                            </Grid>
                                                                            <Grid item>
                                                                                <Chip label="SurfaceNode" style={{
                                                                                    color: "white",
                                                                                    fontWeight: "bold",
                                                                                    backgroundColor: state.graphStyles.nodeStyles.surfaceNodeColour
                                                                                }}/>
                                                                            </Grid>
                                                                            <Grid item>
                                                                                <Chip label="Token" style={{
                                                                                    color: "white",
                                                                                    fontWeight: "bold",
                                                                                    backgroundColor: state.graphStyles.tokenStyles.tokenColour
                                                                                }}/>
                                                                            </Grid>
                                                                        </Grid>
                                                                    </CardContent>
                                                                </Card>
                                                            </Popover>
                                                        </div>
                                                    </Grid>
                                                    <Grid item><Tooltip arrow
                                                                        title={state.dataSet === null ? "Upload data-set" : "Upload new data-set"}>
                                                        {/*<Fab color="primary" aria-label="add" variant="extended"*/}
                                                        {/*     className={classes.fabButton} onClick={() => {*/}
                                                        {/*    history.push("/");*/}
                                                        {/*}}>*/}
                                                        {/*    {state.dataSet === null ? "No Data-set Uploaded" : state.dataSetFileName} {state.dataSet === null ?*/}
                                                        {/*    <CloudUploadIcon/> : <BuildIcon/>}*/}
                                                        {/*</Fab>*/}
                                                        <Button color="primary" variant="contained" disableElevation
                                                                onClick={() => {
                                                                    history.push("/");
                                                                }}>{state.dataSet === null ? "No Data-set Uploaded" : state.dataSetFileName} {state.dataSet === null ?
                                                            <CloudUploadIcon/> : <EjectIcon/>}</Button>
                                                    </Tooltip></Grid>
                                                </Grid>
                                            </CardContent>

                                        </Card>

                                    </Popover>
                                </div>
                            )}
                        </PopupState>
                    </Grid>
                </Grid>
            </Toolbar>;
            break;
        case 'lg':
        case 'xl':
        default:
            toolBarComponent = <Toolbar>
                <IconButton
                    color="inherit"
                    aria-label="open drawer"
                    onClick={handleDrawerOpen}
                    edge="start"
                    className={clsx(classes.menuButton, {
                        [classes.hide]: open,
                    })}
                >
                    <MenuIcon/>
                </IconButton>
                <div style={{marginRight: "1rem"}}>
                    <Typography variant="h6" noWrap>
                        RepGraph
                    </Typography>
                </div>

                <Grid
                    container
                    direction="row"
                    justify="space-evenly"
                    alignItems="center"
                >
                    <Grid item className={classes.menuButton}>
                        <div>
                            {/*<Fab color="primary" aria-label="add" variant="extended"*/}
                            {/*     className={classes.fabButton} onClick={handleClickGraphLegend}>*/}
                            {/*    Show Graph Legend*/}
                            {/*</Fab>*/}
                            <Button color="primary" variant="contained" disableElevation
                                    onClick={handleClickGraphLegend}>Show Graph Legend</Button>
                            <Popover
                                open={legendOpen}
                                anchorEl={anchorEl}
                                onClose={handleCloseGraphLegend}
                                anchorOrigin={{
                                    vertical: 'bottom',
                                    horizontal: 'center',
                                }}
                                transformOrigin={{
                                    vertical: 'top',
                                    horizontal: 'center',
                                }}
                            >
                                <Card>
                                    <CardContent>
                                        <Grid container spacing={1}>
                                            <div>This needs to be fixed still</div>
                                            <Grid item>
                                                <Chip label="AbstractNode" style={{
                                                    color: "white",
                                                    fontWeight: "bold",
                                                    backgroundColor: state.graphStyles.nodeStyles.abstractNodeColour
                                                }}/>
                                            </Grid>
                                            <Grid item>
                                                <Chip label="SurfaceNode" style={{
                                                    color: "white",
                                                    fontWeight: "bold",
                                                    backgroundColor: state.graphStyles.nodeStyles.surfaceNodeColour
                                                }}/>
                                            </Grid>
                                            <Grid item>
                                                <Chip label="Token" style={{
                                                    color: "white",
                                                    fontWeight: "bold",
                                                    backgroundColor: state.graphStyles.tokenStyles.tokenColour
                                                }}/>
                                            </Grid>
                                        </Grid>
                                    </CardContent>
                                </Card>
                            </Popover>
                        </div>
                    </Grid>
                    <Grid item className={classes.menuButton}>
                        <Tooltip arrow
                                 title={"Select visualisation format"}>
                            <ToggleButtonGroup
                                value={state.visualisationFormat}
                                exclusive
                                onChange={handleChangeVisualisationFormat}
                                aria-label="Visualisation formats"
                                color="primary"
                            >
                                <ToggleButton value="1" aria-label="Hierarchical">
                                    <Typography color={"textPrimary"}>Hierarchical</Typography>
                                </ToggleButton>
                                <ToggleButton value="2" aria-label="Tree-like">
                                    <Typography color={"textPrimary"}>Tree-like</Typography>
                                </ToggleButton>
                                <ToggleButton value="3" aria-label="Flat">
                                    <Typography color={"textPrimary"}>Flat</Typography>
                                </ToggleButton>
                            </ToggleButtonGroup>

                        </Tooltip>
                    </Grid>
                    <Grid item className={classes.menuButton}>
                        <Tooltip arrow
                                 title={state.selectedSentenceID === null ? "Select Sentence" : "Change Sentence"}>
                            {/*<Fab color="primary" aria-label="add" variant="extended"*/}
                            {/*     className={classes.fabButton} onClick={() => {*/}
                            {/*    setSentenceOpen(true);*/}
                            {/*}} disabled={state.dataSet === null}>*/}
                            {/*    {state.selectedSentenceID === null ? "No Sentence Selected" : state.selectedSentenceID} {state.selectedSentenceID === null ?*/}
                            {/*    <AddCircleOutlineIcon/> :*/}
                            {/*    <BuildIcon/>}*/}
                            {/*</Fab>*/}

                            <Button color="primary" variant="contained" disableElevation onClick={() => {
                                setSentenceOpen(true);
                            }}
                                    disabled={state.dataSet === null}>{state.selectedSentenceID === null ? "No Sentence Selected" : state.selectedSentenceID} {state.selectedSentenceID === null ?
                                <AddCircleOutlineIcon/> :
                                <EditIcon/>}</Button>
                        </Tooltip>
                    </Grid>
                    <Grid item className={classes.menuButton}>
                        <Tooltip arrow
                                 title={state.dataSet === null ? "Upload data-set" : "Upload new data-set"}>
                            {/*<Fab color="primary" aria-label="add" variant="extended"*/}
                            {/*     className={classes.fabButton} onClick={() => {*/}
                            {/*    history.push("/");*/}
                            {/*}}>*/}
                            {/*    {state.dataSet === null ? "No Data-set Uploaded" : state.dataSetFileName} {state.dataSet === null ?*/}
                            {/*    <CloudUploadIcon/> : <BuildIcon/>}*/}
                            {/*</Fab>*/}
                            <Button color="primary" variant="contained" disableElevation onClick={() => {
                                history.push("/");
                            }}>{state.dataSet === null ? "No Data-set Uploaded" : state.dataSetFileName} {state.dataSet === null ?
                                <CloudUploadIcon/> : <EjectIcon/>}</Button>
                        </Tooltip>
                    </Grid>
                </Grid>
            </Toolbar>;
    }

    // const toolbarList = <List>
    //     <ListItem>
    //         <ListItemIcon>{<SelectAllIcon/>}</ListItemIcon>
    //         <Button color="primary" variant="contained" disableElevation noWrap
    //                 onClick={handleClickGraphLegend}>Show Graph Legend</Button>
    //             <Popover
    //                 open={legendOpen}
    //                 anchorEl={anchorEl}
    //                 onClose={handleCloseGraphLegend}
    //                 anchorOrigin={{
    //                     vertical: 'bottom',
    //                     horizontal: 'center',
    //                 }}
    //                 transformOrigin={{
    //                     vertical: 'top',
    //                     horizontal: 'center',
    //                 }}
    //             >
    //                 <Card>
    //                     <CardContent>
    //                         <Grid container spacing={1}>
    //                             <Grid item>
    //                                 <Chip label="AbstractNode" style={{
    //                                     color: "white",
    //                                     fontWeight: "bold",
    //                                     backgroundColor: state.visualisationOptions.groups.node.color
    //                                 }}/>
    //                             </Grid>
    //                             <Grid item>
    //                                 <Chip label="SurfaceNode" style={{
    //                                     color: "white",
    //                                     fontWeight: "bold",
    //                                     backgroundColor: state.visualisationOptions.groups.surfaceNode.color
    //                                 }}/>
    //                             </Grid>
    //                             <Grid item>
    //                                 <Chip label="Token" style={{
    //                                     color: "white",
    //                                     fontWeight: "bold",
    //                                     backgroundColor: state.visualisationOptions.groups.token.color
    //                                 }}/>
    //                             </Grid>
    //                         </Grid>
    //                     </CardContent>
    //                 </Card>
    //             </Popover>
    //     </ListItem>
    // </List>;

    async function handleSaveFeedback() {

        let labels = null;

        switch (feedbackText.type) {
            case "issue":
                labels = ['bug'];
                break;
            case "idea":
                labels = ['enhancement'];
                break;
            case "anything":
                labels = ['question'];
                break;
            default:
                labels = [];
        }

        const octokit = new Octokit({auth: process.env.REACT_APP_GITHUB_FEEDBACK_APIKEY});

        try {
            const response = await octokit.request('POST /repos/{owner}/{repo}/issues', {
                owner: 'RepGraph',
                repo: 'RepGraph',
                title: `Feedback: ${feedbackText.type}`,
                body: feedbackText.feedback,
                "labels": labels
            });
        } catch (e) {
            console.log(e);
            history.push("/404");
        }
    }


    return (
        <div className={classes.root}>
            <CssBaseline/>
            <Box zIndex="modal">
                <MinimalFeedback
                    save={handleSaveFeedback}
                    value={feedbackText}
                    onChange={(e) => {
                        setFeedbackText(e)
                    }}
                />
            </Box>
            <AppBar
                position="fixed"
                className={clsx(classes.appBar, {
                    [classes.appBarShift]: open,
                })}

                color="secondary"
            >
                {toolBarComponent}
            </AppBar>
            <Drawer
                variant="permanent"
                className={clsx(classes.drawer, {
                    [classes.drawerOpen]: open,
                    [classes.drawerClose]: !open,
                })}
                classes={{
                    paper: clsx({
                        [classes.drawerOpen]: open,
                        [classes.drawerClose]: !open,
                    }),
                }}
            >
                <div className={classes.toolbar}>
                    <IconButton onClick={handleDrawerClose}>
                        {theme.direction === 'rtl' ? <ChevronRightIcon/> : <ChevronLeftIcon/>}
                    </IconButton>
                </div>
                <List>
                    <ListItem button onClick={handleSubsetToolClick}>
                        <ListItemIcon>{<SelectAllIcon/>}</ListItemIcon>
                        <ListItemText primary={"Subset Tool"}/>
                    </ListItem>
                    <ListItem button onClick={handleSubgraphToolClick}>
                        <ListItemIcon>{<SearchIcon/>}</ListItemIcon>
                        <ListItemText primary={"Subgraph Tool"}/>
                    </ListItem>
                    <ListItem button onClick={handleCompareToolClick}>
                        <ListItemIcon>{<CompareArrowsIcon/>}</ListItemIcon>
                        <ListItemText primary={"Compare Tool"}/>
                    </ListItem>
                    <ListItem button onClick={handleTestsToolClick}>
                        <ListItemIcon>{<AssessmentIcon/>}</ListItemIcon>
                        <ListItemText primary={"Tests Tool"}/>
                    </ListItem>
                    <ListItem button onClick={handleDatasetAnalysisToolClick}>
                        <ListItemIcon>{<AssignmentIcon/>}</ListItemIcon>
                        <ListItemText primary={"Dataset Analysis"}/>
                    </ListItem>

                </List>
                <Divider/>
                <List>
                    {['Settings',].map((text, index) => (
                        <ListItem button onClick={handleSettingsClick} key={text}>
                            <ListItemIcon>{<SettingsIcon/>}</ListItemIcon>
                            <ListItemText primary={text}/>
                        </ListItem>
                    ))}
                </List>
                {showSettings && <SettingsTool/>}
            </Drawer>
            <Dialog
                fullWidth
                maxWidth="md"
                open={sentenceOpen}
                onClose={handleSentenceClose}
            >
                <DialogTitle>
                    Select a sentence
                </DialogTitle>
                <DialogContent>
                    <SentenceList closeSelectSentence={handleSentenceClose}/>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleSentenceClose}>
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
            <Dialog
                fullWidth
                maxWidth="md"
                open={subsetDialogOpen}
                onClose={handleSubsetDialogClose}
            >
                <DialogTitle>
                    Subset Tool
                </DialogTitle>
                <DialogContent>
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
                                        {/*<IconButton aria-label="Display subset information button" color={"secondary"}*/}
                                        {/*            onClick={() => handleInfoClick("display subset tool")}>*/}
                                        {/*    <InfoIcon/>*/}
                                        {/*</IconButton>*/}
                                    </Typography>
                                    <Typography variant="body2" color="textPrimary">
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
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleSubsetDialogClose} variant="contained" color="primary" disableElevation>
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
            <Dialog
                fullWidth
                maxWidth="md"
                open={subgraphDialogOpen}
                onClose={handleSubgraphDialogClose}
            >
                <DialogTitle>
                    Subgraph Tool
                </DialogTitle>
                <DialogContent>
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
                                        gutterBottom
                                    >
                                        About the tool:
                                        {/*<IconButton aria-label="Display subset information button" color={"secondary"}*/}
                                        {/*            onClick={() => handleInfoClick("display subset tool")}>*/}
                                        {/*    <InfoIcon/>*/}
                                        {/*</IconButton>*/}
                                    </Typography>
                                    <Typography variant="body2">
                                        Search for a sub-graph pattern using the nodes and labels of the current
                                        graph.
                                    </Typography>
                                </CardContent>
                            </Card>
                        </Grid>
                        <Grid item xs={6} style={{height: "100%"}}>
                            <Card className={classes.body} variant="outlined">
                                <CardContent>
                                    <SearchSubgraphPatternTool/>
                                </CardContent>
                            </Card>
                        </Grid>
                    </Grid>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleSubgraphDialogClose} variant="contained" color="primary" disableElevation>
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
            <Dialog
                fullWidth
                maxWidth="md"
                open={compareDialogOpen}
                onClose={handleCompareToolDialogClose}
            >
                <DialogTitle>
                    Compare Tool
                </DialogTitle>
                <DialogContent>
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
                                        {/*<IconButton aria-label="Display subset information button" color={"secondary"}*/}
                                        {/*            onClick={() => handleInfoClick("display subset tool")}>*/}
                                        {/*    <InfoIcon/>*/}
                                        {/*</IconButton>*/}
                                    </Typography>
                                    <Typography variant="body2" color="textPrimary">
                                        Click the button to compare the similarities and differences of any two
                                        graphs.
                                    </Typography>
                                </CardContent>
                            </Card>
                        </Grid>
                        <Grid item xs={6} style={{height: "100%"}}>
                            <Card className={classes.body} variant="outlined">
                                <CardContent>
                                    <CompareTwoGraphsTool/>
                                </CardContent>
                            </Card>
                        </Grid>
                    </Grid>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCompareToolDialogClose} variant="contained" color="primary" disableElevation>
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
            <Dialog
                fullWidth
                maxWidth="md"
                open={testsDialogOpen}
                onClose={handleTestsToolDialogClose}
            >
                <DialogTitle>
                    Tests Tool
                </DialogTitle>
                <DialogContent>
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
                                        {/*<IconButton aria-label="Display subset information button" color={"secondary"}*/}
                                        {/*            onClick={() => handleInfoClick("display subset tool")}>*/}
                                        {/*    <InfoIcon/>*/}
                                        {/*</IconButton>*/}
                                    </Typography>
                                    <Typography variant="body2" color="textPrimary">
                                        Select a number of graph properties with which to test the
                                        currently displayed graph.
                                    </Typography>
                                </CardContent>
                            </Card>
                        </Grid>
                        <Grid item xs={6} style={{height: "100%"}}>
                            <Card className={classes.body} variant="outlined">
                                <CardContent>
                                    <FormalTestsTool/>
                                </CardContent>
                            </Card>
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
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleTestsToolDialogClose} variant="contained" color="primary" disableElevation>
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
            <Dialog
                fullWidth
                maxWidth="md"
                open={datasetAnalysisDialogOpen}
                onClose={handleDatasetAnalysisDialogClose}
            >
                <DialogTitle>
                    Dataset Analysis
                </DialogTitle>
                <DialogContent>
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
                                        {/*<IconButton aria-label="Display subset information button" color={"secondary"}*/}
                                        {/*            onClick={() => handleInfoClick("display subset tool")}>*/}
                                        {/*    <InfoIcon/>*/}
                                        {/*</IconButton>*/}
                                    </Typography>
                                    <Typography variant="body2" color="textPrimary">
                                        View a number of statistics performed on the dataset uploaded:
                                    </Typography>
                                </CardContent>
                            </Card>
                        </Grid>
                        <Grid item xs={6} style={{height: "100%"}}>
                            <Card className={classes.body} variant="outlined">
                                <CardContent>
                                    <DatasetAnalysisTool/>
                                </CardContent>
                            </Card>
                        </Grid>
                        {state.datasetAnalysis !== null &&
                        <Grid justify={"center"} container item xs={12}>
                            <Card className={classes.body} variant="outlined">
                                <CardContent>
                                    <DatasetAnalysisFormDisplay response={state.datasetAnalysis}/>
                                </CardContent>
                            </Card>
                        </Grid>}
                    </Grid>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDatasetAnalysisDialogClose} variant="contained" color="primary" disableElevation>
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
            <Dialog
                fullWidth
                maxWidth="md"
                open={settingsDialogOpen}
                onClose={handleSettingsDialogClose}
            >
                <DialogTitle>
                    Settings
                </DialogTitle>
                <DialogContent>


                    <SettingsTool/>


                </DialogContent>
                <DialogActions>
                    <Button onClick={handleSettingsDialogClose} variant="contained" color="primary" disableElevation>
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
            <main className={classes.content}>
                <div className={classes.toolbar}/>
                {state.selectedSentenceID === null ? (
                    state.dataSet === null ?
                        <div className={classes.graphDiv}>
                            <Card variant="outlined" style={{width: "100%", height: "100%"}}>
                                <CardContent style={{height: "100%"}}>
                                    <div style={{
                                        display: "flex", justifyContent: "center",
                                        alignItems: "center", height: "100%"
                                    }}>
                                        <Typography variant="h6">
                                            Please
                                            upload a dataset
                                        </Typography>
                                    </div>

                                </CardContent>

                            </Card>
                        </div> :
                        <div className={classes.graphDiv}>
                            <Card variant="outlined" style={{width: "100%", height: "100%"}}>
                                <CardContent style={{height: "100%"}}>
                                    <div style={{
                                        display: "flex", justifyContent: "center",
                                        alignItems: "center", height: "100%"
                                    }}>
                                        <Typography variant="h6">
                                            Please
                                            select a sentence
                                        </Typography>
                                    </div>

                                </CardContent>

                            </Card></div>
                ) : (
                    <div className={classes.graphDiv}>
                        <Card variant="outlined" style={{width: "100%", height: "100%"}}>
                            <ParentSize>
                                {parent => (
                                    <Graph
                                        width={parent.width}
                                        height={parent.height}
                                        graph={state.selectedSentenceVisualisation}
                                        adjacentLinks={determineAdjacentLinks(state.selectedSentenceVisualisation)}
                                        graphFormatCode={graphFormatCode}
                                    />
                                )}
                            </ParentSize>
                        </Card>

                    </div>
                )}
            </main>
        </div>
    );
}
