import React, {useContext, useState, useRef, useEffect} from "react";
import Switch from "@material-ui/core/Switch";
import Paper from "@material-ui/core/Paper";
import Collapse from "@material-ui/core/Collapse";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Box from "@material-ui/core/Box";
import {Grid, Chip, Grow, Slide} from "@material-ui/core";
import {AppContext} from "../../Store/AppContextProvider";

const GraphLegend = (props) => {
    const {graphFormatCode} = props;

    const {state, dispatch} = useContext(AppContext);
    const [checked, setChecked] = React.useState(false);

    const handleChange = () => {
        setChecked((prev) => !prev);
    };

    // switch (graphFormatCode) {
    //     case "hierarchical":
    //         break;
    //     case "tree":
    //         break;
    //     case "flat":
    //         break;
    //     default:
    //         break;
    // }

    return (<Box
        p={2}
        position="absolute"
        bottom={0}
        left={0}
        zIndex="modal"
    >
        <Grow
            in={checked}
            {...(checked
                ? {timeout: {appear: 500, enter: 500, exit: 500}}
                : {})}
        >
            <Paper elevation={0} style={{backgroundColor: "transparent"}}>
                <Grid
                    container
                    spacing={1}
                    direction="column"
                    justify="center"
                    // alignItems="center"
                >
                    {!(graphFormatCode === "hierarchicalCompare" || graphFormatCode === "treeCompare" || graphFormatCode === "flatCompare") &&
                    <>
                        <Grid item>
                            <Chip
                                label="Abstract Node"
                                style={{
                                    color: "white",
                                    fontWeight: "bold",
                                    backgroundColor:
                                    state.graphStyles.nodeStyles.abstractNodeColour
                                }}
                            />
                        </Grid>
                        <Grid item>
                            <Chip
                                label="Surface Node"
                                style={{
                                    color: "white",
                                    fontWeight: "bold",
                                    backgroundColor:
                                    state.graphStyles.nodeStyles.surfaceNodeColour
                                }}
                            />
                        </Grid>
                        <Grid item>
                            {/*<svg>*/}
                            {/*    <g>*/}
                            {/*        <rect*/}
                            {/*            x="5"*/}
                            {/*            y="4"*/}
                            {/*            rx="15"*/}
                            {/*            ry="15"*/}
                            {/*            width="100"*/}
                            {/*            height="28"*/}
                            {/*            style={{*/}
                            {/*                fill: state.graphStyles.nodeStyles.surfaceNodeColour,*/}
                            {/*                stroke: state.graphStyles.nodeStyles.topNodeColour,*/}
                            {/*                strokeWidth: "6px"*/}
                            {/*            }}*/}
                            {/*        ></rect>*/}
                            {/*        <text x="11" y="22" fontWeight="bold" fontSize="13px" fill="white">*/}
                            {/*            Dummy Node*/}
                            {/*        </text>*/}
                            {/*    </g>*/}
                            {/*</svg>*/}
                            <Chip
                                size="large"
                                style={{
                                    backgroundColor:
                                    state.graphStyles.nodeStyles.topNodeColour,
                                }}
                                label={<Chip
                                    size="small"
                                    label="Dummy Node"
                                    style={{
                                        color: "white",
                                        fontWeight: "bold",
                                        backgroundColor:
                                        state.graphStyles.nodeStyles.surfaceNodeColour,
                                    }}
                                />}
                            />


                        </Grid>
                    </>}
                    <Grid item>
                        <Chip
                            label="Top Node"
                            style={{
                                color: "white",
                                fontWeight: "bold",
                                backgroundColor:
                                state.graphStyles.nodeStyles.topNodeColour
                            }}
                        />
                    </Grid>
                    <Grid item>
                        <Chip
                            label="Token"
                            style={{
                                color: "white",
                                fontWeight: "bold",
                                backgroundColor: state.graphStyles.tokenStyles.tokenColour
                            }}
                        />
                    </Grid>
                    {(graphFormatCode === "hierarchicalCompare" || graphFormatCode === "treeCompare" || graphFormatCode === "flatCompare") &&
                    <>
                        <Grid item>
                            <Chip
                                label="Similar"
                                style={{
                                    color: "white",
                                    fontWeight: "bold",
                                    backgroundColor: state.graphStyles.compareStyles.nodeColourSimilar
                                }}
                            />
                        </Grid>
                        <Grid item>
                            <Chip
                                label="Dissimilar"
                                style={{
                                    color: "white",
                                    fontWeight: "bold",
                                    backgroundColor: state.graphStyles.compareStyles.nodeColourDissimilar
                                }}
                            />
                        </Grid>
                    </>
                    }
                </Grid>
            </Paper>
        </Grow>
        <FormControlLabel
            color="primary"
            control={<Switch color="primary" checked={checked} onChange={handleChange}/>}
            label={checked ? "Hide Legend" : "Show Legend"}
        />
    </Box>);
}

export default GraphLegend;