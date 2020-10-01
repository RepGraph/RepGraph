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
    const [open, setOpen] = React.useState(false);
    const [sentenceOpen, setSentenceOpen] = React.useState(false);

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    const handleSentenceClose = () => {
        setSentenceOpen(false);
    };


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
                    <Typography variant="h6" noWrap>
                        RepGraph
                    </Typography>
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
                            <MenuOpenIcon/>
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
                            <SentenceList/>
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={handleSentenceClose}>
                                Close
                            </Button>
                        </DialogActions>
                    </Dialog>
                    <Grid container spacing={2} direction="column">
                        <Grid item xs={12}>
                            <Card>
                                <CardContent>
                                    <Typography variant="h6">Visualisation Area</Typography>
                                </CardContent>
                                <CardActions>
                                    <VisualisationControls/>
                                </CardActions>
                                <CardContent>
                                    {state.selectedSentence === null ? (
                                        <div>Select a sentence</div>
                                    ) : (
                                        <GraphVisualisation/>
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