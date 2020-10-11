import React, {useContext} from "react";
import {
    Card,
    Grid,
    Paper,
    Typography,
    CardContent,
    CardActions
} from "@material-ui/core";

import clsx from "clsx";
import {makeStyles, useTheme} from "@material-ui/core/styles";
import Drawer from "@material-ui/core/Drawer";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import List from "@material-ui/core/List";
import CssBaseline from "@material-ui/core/CssBaseline";
import Divider from "@material-ui/core/Divider";
import IconButton from "@material-ui/core/IconButton";
import MenuIcon from "@material-ui/icons/Menu";
import ChevronLeftIcon from "@material-ui/icons/ChevronLeft";
import ChevronRightIcon from "@material-ui/icons/ChevronRight";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import {Button} from "@material-ui/core";
import AssignmentIcon from "@material-ui/icons/Assignment";
import MenuOpenIcon from "@material-ui/icons/MenuOpen";
import ReorderIcon from "@material-ui/icons/Reorder";
import ShortTextIcon from "@material-ui/icons/ShortText";

import {AppContext} from "./Store/AppContextProvider.js";
import SentenceList from "./Components/Main/SentenceList.js";
import Header from "./Components/Layouts/Header.js";
import AnalysisAccordion from "./Components/Main/AnalysisAccordion";
import VisualisationControls from "./Components/Main/VisualisationControls";
import GraphVisualisation from "./Components/Main/GraphVisualisation";

import "./Components/Main/network.css";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogActions from "@material-ui/core/DialogActions";
import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';
import BuildIcon from '@material-ui/icons/Build';
import CloudUploadIcon from '@material-ui/icons/CloudUpload';

import {Chip} from "@material-ui/core";
import Tooltip from "@material-ui/core/Tooltip";
import {Link, useHistory} from "react-router-dom";
import Box from "@material-ui/core/Box";
import CardHeader from "@material-ui/core/CardHeader";
import VisualizerArea from "./Components/Main/VisualizerArea";
import FormControl from "@material-ui/core/FormControl";
import FormLabel from "@material-ui/core/FormLabel";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Radio from "@material-ui/core/Radio";
import ToggleButtonGroup from "@material-ui/lab/ToggleButtonGroup";
import ToggleButton from "@material-ui/lab/ToggleButton";

const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
    root: {
        display: "flex"
    },
    appBar: {
        zIndex: theme.zIndex.drawer + 1,
        transition: theme.transitions.create(["width", "margin"], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen
        })
    },
    appBarShift: {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(["width", "margin"], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen
        })
    },
    menuButton: {
        marginRight: 36
    },
    hide: {
        display: "none"
    },
    drawer: {
        width: drawerWidth,
        flexShrink: 0,
        whiteSpace: "nowrap"
    },
    drawerOpen: {
        width: drawerWidth,
        transition: theme.transitions.create("width", {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen
        })
    },
    drawerClose: {
        transition: theme.transitions.create("width", {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen
        }),
        overflowX: "hidden",
        width: theme.spacing(7) + 1,
        [theme.breakpoints.up("sm")]: {
            width: theme.spacing(9) + 1
        }
    },
    toolbar: {
        display: "flex",
        alignItems: "center",
        justifyContent: "flex-end",
        padding: theme.spacing(0, 1),
        // necessary for content to be below app bar
        ...theme.mixins.toolbar
    },
    content: {
        flexGrow: 1,
        padding: theme.spacing(3)
    }
}));

const testingEndpoint = "http://192.168.0.135:8080";

export default function Main() {
    const {state, dispatch} = useContext(AppContext);
    const classes = useStyles();
    const theme = useTheme();
    const history = useHistory();
    const [open, setOpen] = React.useState(false);
    const [sentenceOpen, setSentenceOpen] = React.useState(false);
    const [visFormat, setVisFormat] = React.useState("1");

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    const handleSentenceClose = () => {
        setSentenceOpen(false);
    };

    const handleChangeVisualisationFormat = (format) => {
        setVisFormat(format);
        dispatch({type: "SET_VISUALISATION_FORMAT", payload: {visualisationFormat: format}});
        console.log(format);
        //Update the currently displayed graph as well!

        let requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        dispatch({type: "SET_VISUALISATION_FORMAT", payload: {isLoading: true}});

        fetch(state.APIendpoint + "/Visualise?graphID=" + state.selectedSentenceID + "&format="+format, requestOptions)
            .then((response) => response.text())
            .then((result) => {

                const jsonResult = JSON.parse(result);
                console.log(jsonResult);
                //console.log(jsonResult);
                //console.log(jsonResult.response);
                //const formattedGraph = layoutGraph(jsonResult);
                dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: jsonResult}});
                dispatch({type: "SET_LOADING", payload: {isLoading: false}});
            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}});
                console.log("error", error);
                history.push("/404"); //for debugging
            });
    }


    return (
        <div className={classes.root}>
            <CssBaseline/>
            <AppBar
                color="primary"
                position="fixed"
                className={clsx(classes.appBar, {
                    [classes.appBarShift]: open
                })}
            >
                <Toolbar>
                    <IconButton
                        color="inherit"
                        aria-label="open drawer"
                        onClick={handleDrawerOpen}
                        edge="start"
                        className={clsx(classes.menuButton, {
                            [classes.hide]: open
                        })}
                    >
                        <MenuIcon/>
                    </IconButton>
                    <Typography variant="h6">
                        RepGraph
                    </Typography>
                    <Grid container justify="flex-end" spacing={2}>
                        <Grid item color="inherit">
                            <Tooltip arrow
                                     title={"Select visualisation format"}>
                            <Paper>
                                <ToggleButtonGroup
                                    value={visFormat}
                                    exclusive
                                    onChange={(event, value)=>{handleChangeVisualisationFormat(value)}}
                                    aria-label="Visualisation formats"
                                    color="inherit"
                                >
                                    <ToggleButton color="primary" value="1" aria-label="Hierarchical">
                                        <Typography color="primary">Hierarchical</Typography>
                                    </ToggleButton>
                                    <ToggleButton color="primary" value="2" aria-label="Tree-like">
                                        <Typography color="primary">Tree-like</Typography>
                                    </ToggleButton>
                                    <ToggleButton color="primary" value="3" aria-label="Flat">
                                        <Typography color="primary">Flat</Typography>
                                    </ToggleButton>
                                </ToggleButtonGroup>
                            </Paper>
                            </Tooltip>
                        </Grid>
                        <Grid item>
                            <Tooltip arrow
                                     title={state.selectedSentenceID === null ? "Select Sentence" : "Change Sentence"}>
                                <Chip onClick={() => {
                                    handleDrawerOpen();
                                }} color="inherit"
                                      label={state.selectedSentenceID === null ? "No Sentence Selected" : state.selectedSentenceID}
                                      icon={state.selectedSentenceID === null ? <AddCircleOutlineIcon/> :
                                          <BuildIcon/>}/>
                            </Tooltip>
                        </Grid>
                        <Grid item>
                            <Tooltip arrow title={state.dataSet === null ? "Upload data-set" : "Upload new data-set"}>
                                <Chip onClick={() => {
                                    history.push("/");
                                }} color="inherit"
                                      label={state.dataSet === null ? "No Data-set Uploaded" : state.dataSetFileName}
                                      icon={state.dataSet === null ? <CloudUploadIcon/> : <BuildIcon/>}/>
                            </Tooltip>
                        </Grid>
                    </Grid>

                </Toolbar>

            </AppBar>
            <Drawer
                variant="permanent"
                className={clsx(classes.drawer, {
                    [classes.drawerOpen]: open,
                    [classes.drawerClose]: !open
                })}
                classes={{
                    paper: clsx({
                        [classes.drawerOpen]: open,
                        [classes.drawerClose]: !open
                    })
                }}
            >
                <div className={classes.toolbar}>
                    <IconButton onClick={handleDrawerClose}>
                        {theme.direction === "rtl" ? (
                            <ChevronRightIcon/>
                        ) : (
                            <ChevronLeftIcon/>
                        )}
                    </IconButton>
                </div>
                <Divider/>
                <List>
                    <ListItem button onClick={() => setSentenceOpen(true)}>
                        <ListItemIcon>
                            {state.selectedSentenceID === null ? <AddCircleOutlineIcon/> : <BuildIcon/>}
                        </ListItemIcon>
                        <ListItemText primary="Select Sentence"/>
                    </ListItem>
                </List>
                <Divider/>
            </Drawer>
            <main className={classes.content}>
                <div className={classes.toolbar}/>
                <React.Fragment>
                    <Dialog
                        fullWidth
                        maxWidth="md"
                        open={sentenceOpen}
                        onClose={handleSentenceClose}
                        aria-labelledby="longest-path-visualisation-title"
                    >
                        <DialogTitle id="longest-path-visualisation-title">
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
                    <Grid container spacing={2} direction="column">
                        <Grid item>
                            <Card>
                                <CardContent>
                                    <Typography variant="h5">Visualisation Area</Typography>
                                    <VisualisationControls/>
                                </CardContent>
                                <CardContent style={{height: "80vh", width: "100%"}}>
                                    {state.selectedSentenceID === null ? (
                                        <Typography variant="subtitle1">Please select a sentence</Typography>
                                    ) : (
                                               <GraphVisualisation/>
                                    )}
                                </CardContent>
                            </Card>
                        </Grid>
                        <Grid item>
                            <Card>
                                <CardContent>
                                    <Typography variant="h6">Analysis Features</Typography>
                                </CardContent>
                                <CardActions>
                                    <AnalysisAccordion/>
                                </CardActions>
                            </Card>
                        </Grid>
                    </Grid>
                </React.Fragment>
            </main>
        </div>
    );
}