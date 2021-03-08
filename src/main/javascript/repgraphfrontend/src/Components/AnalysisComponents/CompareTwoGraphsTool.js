import React, {useContext} from 'react';
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import LocationSearchingIcon from '@material-ui/icons/LocationSearching';
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import {AppContext} from "../../Store/AppContextProvider.js";
import {useHistory} from "react-router-dom";
import CompareTwoGraphsVisualisation from "../Main/CompareTwoGraphsVisualisation";
import CompareArrowsIcon from '@material-ui/icons/CompareArrows';

const useStyles = makeStyles((theme) => ({
    root: {
        height: "100%"
    }
}));

function CompareTwoGraphsTool(props) {
    const {state, dispatch} = useContext(AppContext); //Provide access to global state
    const history = useHistory();
    const [open, setOpen] = React.useState(false);


    //Handle click open for dialog
    const handleClickOpen = () => {
        setOpen(true);
    };

    //Handle close for dialog
    const handleClose = () => {
        setOpen(false);
    };

    const classes = useStyles(); //Use styles created above

    return (
        <Grid
            container
            direction="column"
            justify="center"
            alignItems="center"
            className={classes.root}
        >
                <Button
                    variant="contained" color="primary" disableElevation onClick={handleClickOpen}
                    endIcon={<CompareArrowsIcon/>}
                    disabled={state.dataSet === null}
                >
                Compare Two Graphs
            </Button>

            <Dialog
                open={open}
                fullWidth={true}
                maxWidth="xl"
                onClose={handleClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">{"Compare two graphs"}</DialogTitle>
                <DialogContent>
                    <CompareTwoGraphsVisualisation/>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} variant="contained" color="primary" disableElevation autoFocus>
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
        </Grid>
    );

}

export default CompareTwoGraphsTool;
