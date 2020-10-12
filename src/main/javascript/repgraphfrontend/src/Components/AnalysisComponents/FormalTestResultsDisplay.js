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

const useStyles = makeStyles({
    table: {
        minWidth: 650
    }
});

//Function to create row data for the formal tests results table
function createData(test, result) {
    return { test, result };
}

export default function FormalTestsResultsDisplay(props) {
    const theme = useTheme();
    const classes = useStyles();
    const [open, setOpen] = React.useState(false); //Local state of the results dialog
    const [rowClicked, setRowClicked] = React.useState(null); //Local state to store which table row was clicked
    const { state, dispatch } = React.useContext(AppContext); //Provide access to global state

    const response = state.testResults; //Get the test results from the global state

    let newRows = [];

    //Add the rows to the table of results
    for (const [test, result] of Object.entries(response)) {
        newRows.push(createData(test, result));
    }

    //
    const handleClickOpen = (event, test) => {
        console.log(test);
        if (test !== "Connected") {
            setRowClicked(test);
            setOpen(true);
        }
    };

    const handleClose = () => {
        setOpen(false);
    };

    let dialogElement;

    if (rowClicked === "Planar") {
        dialogElement = <Paper> planar vis </Paper>;
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

    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleInfoClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

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
                            onClick={(event) => handleClickOpen(event, row.test)}
                        >
                            <TableCell component="th" scope="row">
                                {row.test}
                            </TableCell>
                            <TableCell align="right">{JSON.stringify(row.result)}</TableCell>
                            <TableCell>
                                {row.test !== "Connected" && (
                                    <Button variant="outlined" color="primary">
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
                    <Button onClick={handleClose} color="primary">
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
        </TableContainer>
    );
}
