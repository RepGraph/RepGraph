// import React, {useState}from 'react';
// import PropTypes from 'prop-types';
// import {withStyles} from '@material-ui/core/styles';
// import List from '@material-ui/core/List';
// import ListItem from '@material-ui/core/ListItem';
// import ListItemIcon from '@material-ui/core/ListItemIcon';
// import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
// import ListItemText from '@material-ui/core/ListItemText';
// import ListSubheader from '@material-ui/core/ListSubheader';
// import Switch from '@material-ui/core/Switch';
// import WifiIcon from '@material-ui/icons/Wifi';
// import BluetoothIcon from '@material-ui/icons/Bluetooth';
// import PaletteIcon from '@material-ui/icons/Palette';
// import {PresentToAll} from "@material-ui/icons";
// import {Slider, TextField} from "@material-ui/core";
// import Grid from "@material-ui/core/Grid";
// import LinearScaleIcon from '@material-ui/icons/LinearScale';
// import FormatLineSpacingIcon from '@material-ui/icons/FormatLineSpacing';
//
// import { HexColorPicker } from "react-colorful";
//
// function SettingsTool(props) {
//
//     const [color, setColor] = useState("#aabbcc");
//
//     return (<List subheader={<ListSubheader>Settings</ListSubheader>}>
//             <ListItem>
//                 <ListItemIcon>
//                     <PaletteIcon/>
//                 </ListItemIcon>
//                 <ListItemText primary="Abstract Node Colour"/>
//                 <ListItemSecondaryAction>
//                     <HexColorPicker color={color} onChange={setColor} />
//                 </ListItemSecondaryAction>
//             </ListItem>
//             <ListItem>
//                 <ListItemIcon>
//                     <PaletteIcon/>
//                 </ListItemIcon>
//                 <ListItemText primary="Surface Node Colour"/>
//                 <ListItemSecondaryAction>
//
//                 </ListItemSecondaryAction>
//             </ListItem>
//             <ListItem>
//                 <ListItemIcon>
//                     <LinearScaleIcon/>
//                 </ListItemIcon>
//                 <ListItemText primary="Intralevel Node Spacing"/>
//                 <ListItemSecondaryAction>
//                     <TextField />
//                 </ListItemSecondaryAction>
//             </ListItem>
//             <ListItem>
//                 <ListItemIcon>
//                     <FormatLineSpacingIcon/>
//                 </ListItemIcon>
//                 <ListItemText primary="Interlevel Node Spacing"/>
//                 <ListItemSecondaryAction>
//                     <TextField />
//                 </ListItemSecondaryAction>
//             </ListItem>
//
//         </List>
//     );
// }
//
// export default SettingsTool;

import React, {useContext, useState} from "react";
import {makeStyles} from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import Divider from "@material-ui/core/Divider";
import InboxIcon from "@material-ui/icons/Inbox";
import DraftsIcon from "@material-ui/icons/Drafts";
import TextField from "@material-ui/core/TextField";
import InputAdornment from "@material-ui/core/InputAdornment";
import Grid from "@material-ui/core/Grid";

import MaskedInput from "react-text-mask";
import PropTypes from "prop-types";
import Input from "@material-ui/core/Input";

import {RgbaStringColorPicker} from "react-colorful";
import {Typography} from "@material-ui/core";

import ColorizeIcon from "@material-ui/icons/Colorize";

import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";


import NumberFormat from "react-number-format";

import LinearScaleIcon from "@material-ui/icons/LinearScale";
import FormatLineSpacingIcon from "@material-ui/icons/FormatLineSpacing";
import VerticalAlignBottomIcon from "@material-ui/icons/VerticalAlignBottom";
import {mdiArrowExpandHorizontal, mdiArrowExpandVertical} from "@mdi/js";
import Icon from "@mdi/react";
import {AppContext, defaultGraphStyles,defaultGraphLayoutSpacing} from "../../Store/AppContextProvider";
import {useHistory} from "react-router-dom";
import Button from "@material-ui/core/Button";
import {layoutHierarchy} from "../../LayoutAlgorithms/layoutHierarchy";
import {layoutTree} from "../../LayoutAlgorithms/layoutTree";
import {layoutFlat} from "../../LayoutAlgorithms/layoutFlat";

const useStyles = makeStyles((theme) => ({
    root: {
        width: "100%",
        backgroundColor: theme.palette.background.paper
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
        fontWeight: theme.typography.fontWeightRegular
    }
}));

const hexRegEx = /[a-fA-F0-9]/;

///^rgba\((\d{1,3}%?),\s*(\d{1,3}%?),\s*(\d{1,3}%?),\s*(\d*(?:\.\d+)?)\)$/

const rgbaRegEx = /\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\b/g;

function HexTextMask(props) {
    const {inputRef, ...other} = props;

    return (
        <MaskedInput
            {...other}
            ref={(ref) => {
                inputRef(ref ? ref.inputElement : null);
            }}
            mask={[hexRegEx, hexRegEx, hexRegEx, hexRegEx, hexRegEx]}
        />
    );
}

//[hexRegEx, hexRegEx, hexRegEx, hexRegEx, hexRegEx]

// <TextField inputProps={{ pattern: "[a-z]" }} />

// [
//   "rgba(",
//   rgbaRegEx,
//   ",",
//   rgbaRegEx,
//   ",",
//   rgbaRegEx,
//   ",",
//   /^(?:0*(?:\.\d+)?|1(\.0*)?)$/gm,
//   ")"
// ];

//rgba(0,172,237,1)

HexTextMask.propTypes = {
    inputRef: PropTypes.func.isRequired
};

function NumberFormatSpacingInput(props) {
    const {inputRef, onChange, ...other} = props;

    return (
        <NumberFormat
            {...other}
            getInputRef={inputRef}
            onValueChange={(values) => {
                onChange({
                    target: {
                        name: props.name,
                        value: values.value
                    }
                });
            }}
            allowNegative={false}
        />
    );
}

NumberFormatSpacingInput.propTypes = {
    inputRef: PropTypes.func.isRequired,
    name: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired
};

function NumberFormatDecimalInput(props) {
    const {inputRef, onChange, ...other} = props;

    return (
        <NumberFormat
            {...other}
            getInputRef={inputRef}
            onValueChange={(values) => {
                onChange({
                    target: {
                        name: props.name,
                        value: values.value
                    }
                });
            }}
            allowNegative={false}
            decimalScale={2}
        />
    );
}

NumberFormatSpacingInput.propTypes = {
    inputRef: PropTypes.func.isRequired,
    name: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired
};

const NumberInput = (props) => {
    const {name, values, onChange} = props;

    return (
        <TextField
            {...props}
            value={values[name]}
            onChange={onChange}
            name={name}
            type="number"
            InputProps={{
                inputComponent: NumberFormatSpacingInput
            }}
        />
    );
};

const RgbaInput = (props) => {
    const {name, values, onChange} = props;

    return (
        <Grid
            container
            direction="row"
            justify="flex-start"
            alignItems="center"
            spacing={2}
        >
            <Grid item>
                <NumberInput label={"R"} {...props} />
            </Grid>
            <Grid item>
                <NumberInput label={"G"} {...props} />
            </Grid>
            <Grid item>
                <NumberInput label={"B"} {...props} />
            </Grid>
        </Grid>
    );
};

const MaskedColourPicker = (props) => {
    const {name, values, onChange} = props;

    //console.log(props);

    const handleChange = (value) => {
        onChange({target: {name, value}});
    };

    return (
        <Grid
            container
            direction="column"
            justify="center"
            alignItems="center"
            spacing={2}
        >
            <Grid item>
                <RgbaStringColorPicker
                    color={values[name]}
                    onChange={handleChange}
                />
            </Grid>
            {/* <Grid item>
        <Input
          name={name}
          value={values[name]}
          onChange={onChange}
          inputComponent={HexTextMask}
        />
      </Grid> */}
        </Grid>
    );
};

const graphSpacingIcons = {
    nodeHeight: <Icon path={mdiArrowExpandVertical} size={1}/>,
    nodeWidth: <Icon path={mdiArrowExpandHorizontal} size={1}/>,
    interLevelSpacing: <FormatLineSpacingIcon/>,
    intraLevelSpacing: <LinearScaleIcon/>,
    tokenLevelSpacing: <VerticalAlignBottomIcon/>
};


export default function SettingsTool() {
    const classes = useStyles();
    const {state, dispatch} = useContext(AppContext); //Provide access to global state
    const history = useHistory(); //Use routing history

    const [valuesNodes, setValuesNodes] = useState(state.graphStyles.hierarchicalStyles.nodeStyles);

    const [valuesLinks, setValuesLinks] = useState(state.graphStyles.hierarchicalStyles.linkStyles);

    const [valuesTokens, setValuesTokens] = useState(state.graphStyles.hierarchicalStyles.tokenStyles);

    const [valuesGraphSpacing, setValuesGraphSpacing] = useState(state.graphLayoutSpacing);

    const handleChangeNodes = (event) => {
        setValuesNodes({
            ...valuesNodes,
            [event.target.name]: event.target.value
        });

        handleUpdateStyles();
    };

    const handleChangeTokens = (event) => {
        setValuesTokens({
            ...valuesTokens,
            [event.target.name]: event.target.value
        });

        handleUpdateStyles();
    };

    const handleChangeLinks = (event) => {
        setValuesLinks({
            ...valuesLinks,
            [event.target.name]: event.target.value
        });

        handleUpdateStyles();
    };

    const handleChangeGraphSpacing = (event) => {
        setValuesGraphSpacing({
            ...valuesGraphSpacing,
            [event.target.name]: parseInt(event.target.value)
        });

        handleUpdateStyles();
    };

    const handleUpdateStyles = () => {
        dispatch({type: "SET_GRAPH_STYLES",
            payload: {
                graphStyles: {
                    ...state.graphStyles,
                    hierarchicalStyles: {...state.graphStyles.hierarchicalStyles, nodeStyles: valuesNodes, linkStyles: valuesLinks, tokenStyles:valuesTokens},
                    treeStyles: {...state.graphStyles.treeStyles, nodeStyles: valuesNodes, linkStyles: valuesLinks, tokenStyles:valuesTokens},
                    flatStyles: {...state.graphStyles.flatStyles, nodeStyles: valuesNodes, linkStyles: valuesLinks, tokenStyles:valuesTokens}
                }
            }
        });

        dispatch({type: "SET_GRAPH_LAYOUT_SPACING",
            payload: {
                graphLayoutSpacing: valuesGraphSpacing
            }
        });

        let graphData = null;

        switch (state.visualisationFormat) {
            case "1":
                graphData = layoutHierarchy(state.selectedSentenceGraphData, valuesGraphSpacing);
                break;
            case "2":
                graphData = layoutTree(state.selectedSentenceGraphData);
                break;
            case "3":
                graphData = layoutFlat(state.selectedSentenceGraphData);
                break;
            default:
                graphData = layoutHierarchy(state.selectedSentenceGraphData, valuesGraphSpacing);
                break;
        }

        dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: graphData}});
    }

    // dispatch({type: "SET_GRAPH_STYLES",
    //     payload: {
    //         graphStyles: {
    //             ...state.graphStyles,
    //             hierarchicalStyles: {nodeStyles: valuesNodes, ...state.graphStyles.hierarchicalStyles},
    //             treeStyles: {nodeStyles: valuesNodes, ...state.graphStyles.treeStyles},
    //             flatStyles: {nodeStyles: valuesNodes, ...state.graphStyles.flatStyles}
    //         }
    //     }
    // }); //Update the node styles

    //console.log(valuesNodes,valuesLinks,valuesTokens,valuesGraphSpacing);

    const handleResetDefaultStyles = () => {
        setValuesNodes(defaultGraphStyles.hierarchicalStyles.nodeStyles);
        setValuesLinks(defaultGraphStyles.hierarchicalStyles.linkStyles);
        setValuesTokens(defaultGraphStyles.hierarchicalStyles.tokenStyles);
        setValuesGraphSpacing(defaultGraphLayoutSpacing);

        dispatch({type: "SET_GRAPH_STYLES",
            payload: {
                graphStyles: defaultGraphStyles
            }
        });

        dispatch({type: "SET_GRAPH_LAYOUT_SPACING",
            payload: {
                graphLayoutSpacing: defaultGraphLayoutSpacing
            }
        });

        let graphData = null;

        switch (state.visualisationFormat) {
            case "1":
                graphData = layoutHierarchy(state.selectedSentenceGraphData, defaultGraphLayoutSpacing);
                break;
            case "2":
                graphData = layoutTree(state.selectedSentenceGraphData,defaultGraphLayoutSpacing);
                break;
            case "3":
                graphData = layoutFlat(state.selectedSentenceGraphData,false,defaultGraphLayoutSpacing);
                break;
            default:
                graphData = layoutHierarchy(state.selectedSentenceGraphData, defaultGraphLayoutSpacing);
                break;
        }

        dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: graphData}});
    }

    return (
        <div className={classes.root}>
            <Grid
                container
                direction="column"
                justify="center"
                alignItems="center"
                style={{width:"100%"}}
            >

                <Grid item style={{width:"100%"}}>
                    <Accordion>
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon/>}
                        >
                            <Typography className={classes.heading}>Node Styles</Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <List
                                component="nav"
                                style={{width: "100%"}}
                            >
                                {Object.keys(valuesNodes).map((key, index) => (
                                    <ListItem key={`${key}${index}`}>
                                        <Grid
                                            container
                                            direction="column"
                                            justify="center"
                                            alignItems="center"
                                            spacing={2}
                                        >
                                            <Grid item>
                                                <ListItemIcon>
                                                    <ColorizeIcon/>
                                                </ListItemIcon>
                                            </Grid>
                                            <Grid item>
                                                <Typography>{key}</Typography>
                                            </Grid>
                                            <Grid item>
                                                <MaskedColourPicker
                                                    name={key}
                                                    values={valuesNodes}
                                                    onChange={handleChangeNodes}
                                                />
                                            </Grid>
                                        </Grid>
                                    </ListItem>
                                ))}
                            </List>
                        </AccordionDetails>
                    </Accordion>
                    <Accordion>
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon/>}
                        >
                            <Typography className={classes.heading}>Token Styles</Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <List
                                component="nav"
                                style={{width: "100%"}}
                            >
                                {Object.keys(valuesTokens).map((key, index) => (
                                    <ListItem key={`${key}${index}`}>
                                        <Grid
                                            container
                                            direction="column"
                                            justify="center"
                                            alignItems="center"
                                            spacing={2}
                                        >
                                            <Grid item>
                                                <ListItemIcon>
                                                    <ColorizeIcon/>
                                                </ListItemIcon>
                                            </Grid>
                                            <Grid item>
                                                <Typography>{key}</Typography>
                                            </Grid>
                                            <Grid item>
                                                <MaskedColourPicker
                                                    name={key}
                                                    values={valuesTokens}
                                                    onChange={handleChangeTokens}
                                                />
                                            </Grid>
                                        </Grid>
                                    </ListItem>
                                ))}
                            </List>
                        </AccordionDetails>
                    </Accordion>
                    <Accordion>
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon/>}
                        >
                            <Typography className={classes.heading}>Link Styles</Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <List
                                component="nav"
                                style={{width: "100%"}}
                            >
                                {Object.keys(valuesLinks).map((key, index) => (
                                    <ListItem key={`${key}${index}`}>
                                        <Grid
                                            container
                                            direction="column"
                                            justify="center"
                                            alignItems="center"
                                            spacing={2}
                                        >
                                            <Grid item>
                                                <ListItemIcon>
                                                    <ColorizeIcon/>
                                                </ListItemIcon>
                                            </Grid>
                                            <Grid item>
                                                <Typography>{key}</Typography>
                                            </Grid>
                                            <Grid item>
                                                <MaskedColourPicker
                                                    name={key}
                                                    values={valuesLinks}
                                                    onChange={handleChangeLinks}
                                                />
                                            </Grid>
                                        </Grid>
                                    </ListItem>
                                ))}
                            </List>
                        </AccordionDetails>
                    </Accordion>
                    <Accordion>
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon/>}
                        >
                            <Typography className={classes.heading}>
                                Graph Layout Spacing
                            </Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <List
                                component="nav"
                                style={{width: "100%"}}
                            >
                                {Object.keys(valuesGraphSpacing).map((key, index) => (
                                    <ListItem key={`${key}${index}`}>
                                        <Grid
                                            container
                                            direction="column"
                                            justify="center"
                                            alignItems="center"
                                            spacing={1}
                                        >
                                            <Grid
                                                item
                                                container
                                                direction="row"
                                                justify="center"
                                                alignItems="center"
                                            >
                                                <Grid item>
                                                    <ListItemIcon>{graphSpacingIcons[key]}</ListItemIcon>
                                                </Grid>
                                                <Grid item>
                                                    <Typography>{key}</Typography>
                                                </Grid>
                                            </Grid>
                                            <Grid item>
                                                <NumberInput
                                                    name={key}
                                                    values={valuesGraphSpacing}
                                                    onChange={handleChangeGraphSpacing}
                                                />
                                            </Grid>
                                        </Grid>
                                    </ListItem>
                                ))}
                            </List>
                        </AccordionDetails>
                    </Accordion>
                </Grid>
                <Grid item>
                    <Button variant="outlined" color="primary" onClick={handleResetDefaultStyles}>
                        Reset Default Styles
                    </Button>
                </Grid>
            </Grid>
        </div>
    );
}


