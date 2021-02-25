import React from "react";
import { makeStyles, useTheme } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableContainer from "@material-ui/core/TableContainer";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import { Button } from "@material-ui/core";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import { Tooltip, Typography } from "@material-ui/core";

import { cloneDeep } from "lodash";

import { AppContext } from "../../Store/AppContextProvider";
import LongestPathVisualisation from "../Main/LongestPathVisualisation";
import IconButton from "@material-ui/core/IconButton";
import InfoIcon from "@material-ui/icons/Info";
import Popover from "@material-ui/core/Popover";
import PlanarVisualisation from "../Main/PlanarVisualisation";
import {ParentSize} from "@visx/responsive";
import {Graph} from "../Graph/Graph";
import {determineAdjacentLinks} from "../../LayoutAlgorithms/layoutHierarchy";
import {layoutFlat} from "../../LayoutAlgorithms/layoutFlat";


const useStyles = makeStyles({
    table: {
        minWidth: 650
    },
    graphDiv: {
        height: "60vh",
        //border: "1px solid red",
        flex: "1",
        width: "100%"
    }
});

//Function to create row data for the formal tests results table
function createData(test, result) {
    return { test, result };
}

export default function FormalTestsResultsDisplay(props) {
    const classes = useStyles();
    const [open, setOpen] = React.useState(false); //Local state of the results dialog
    const [rowClicked, setRowClicked] = React.useState(null); //Local state to store which table row was clicked
    const { state, dispatch } = React.useContext(AppContext); //Provide access to global state
    const response = state.testResults; //Get the test results from the global state

    let newRows = [];

    //Add the rows to the table of results
    for (const [test, result] of Object.entries(response)) {
        if(test !== "PlanarVis"){
            newRows.push(createData(test, result));
        }
    }

    //Handle click on table row
    const handleClickOpen = (event, test) => {
        console.log(test);
        if (test !== "Connected") {
            setRowClicked(test);
            setOpen(true);
        }
    };

    //Handle close of dialog
    const handleClose = () => {
        setOpen(false);
    };

    let dialogElement; //variable to store the element to be displayed in the dialog to the user

    // if (rowClicked === "Planar") {
    //     dialogElement = <PlanarVisualisation planarGraphData={response.PlanarVis}/>;
    // } else if (rowClicked === "LongestPathDirected") {
    //     dialogElement = (
    //         <LongestPathVisualisation
    //             type={rowClicked}
    //         />
    //     );
    // } else if (rowClicked === "LongestPathUndirected") {
    //     dialogElement = (
    //         <LongestPathVisualisation
    //             type={rowClicked}
    //         />
    //     );
    // }

    //Determine graphFormatCode
    let graphFormatCode = null;
    switch (state.visualisationFormat) {
        case "1":
            graphFormatCode = "hierarchicalCompare";
            break;
        case "2":
            graphFormatCode = "treeCompare";
            break;
        case "3":
            graphFormatCode = "flatCompare";
            break;
        default:
            graphFormatCode = "hierarchicalCompare";
            break;
    }

    if (rowClicked === "Planar") {
        //Not finished with planar yet - need to add planar layout algorithm

        const graphData = layoutFlat(response.PlanarVis, true, state.graphLayoutSpacing);

        dialogElement = <div className={classes.graphDiv}>
            <ParentSize>
                {parent => (
                    <Graph
                        width={parent.width}
                        height={parent.height}
                        graph={graphData}
                        adjacentLinks={determineAdjacentLinks(graphData)}
                        graphFormatCode={graphFormatCode}
                    />
                )}
            </ParentSize>
        </div>;
    } else if (rowClicked === "LongestPathDirected") {
        dialogElement = (
            <LongestPathVisualisation
                type={rowClicked}
            />
        );
    } else if (rowClicked === "LongestPathUndirected") {
        dialogElement = (
            <LongestPathVisualisation
                type={rowClicked}
            />
        );
    }

    const [anchorEl, setAnchorEl] = React.useState(null); //Anchor for information popper component

    //Handle information button click
    const handleInfoClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    //Handle information popper close
    const handlePopperClose = () => {
        setAnchorEl(null);
    };

    const popperOpen = Boolean(anchorEl);
    const id = open ? 'simple-popover' : undefined;

    return (
        <TableContainer component={Paper}>
            <Popover
                id={id}
                open={popperOpen}
                anchorEl={anchorEl}
                onClose={handlePopperClose}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'center',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'center',
                }}
            >
                <Typography>Some info about the longest paths.</Typography>
            </Popover>
            <Table className={classes.table} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell>Test</TableCell>
                        <TableCell align="right">Result</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {newRows.map((row) => (
                        <TableRow
                            hover
                            key={row.test}

                        >
                            <TableCell component="th" scope="row">
                                {row.test}
                            </TableCell>
                            <TableCell align="right">{JSON.stringify(row.result)}</TableCell>
                            <TableCell>
                                {
                                    row.test !== "Connected" && (
                                    <Button variant="contained" color="secondary" onClick={(event) => handleClickOpen(event, row.test)} disabled={row.result === "Cycle Detected" ? true : false}>
                                        Visualise
                                    </Button>
                                )}
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            <Dialog
                fullWidth
                maxWidth="xl"
                open={open}
                onClose={handleClose}
                aria-labelledby="longest-path-visualisation-title"
            >
                <DialogTitle id="longest-path-visualisation-title">
                    {rowClicked} Visualisation
                    <IconButton aria-label="Display subset information button" onClick={handleInfoClick}>
                    <InfoIcon />
                    </IconButton>
                </DialogTitle>
                <DialogContent>
                    {dialogElement}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color={"secondary"}>
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
        </TableContainer>
    );
}
