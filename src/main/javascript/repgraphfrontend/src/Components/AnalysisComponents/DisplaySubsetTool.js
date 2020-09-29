import React, {useContext} from 'react';
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/core/styles";
import FormLabel from "@material-ui/core/FormLabel";
import FormControl from "@material-ui/core/FormControl";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Radio from "@material-ui/core/Radio";
import ArrowForwardIcon from "@material-ui/icons/ArrowForward";
import Button from "@material-ui/core/Button";
import LocationSearchingIcon from '@material-ui/icons/LocationSearching';
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogActions from "@material-ui/core/DialogActions";

import { AppContext } from "../../Store/AppContextProvider.js";
import {useHistory} from "react-router-dom";
import GraphVisualisation from "../Main/GraphVisualisation";

const useStyles = makeStyles((theme) => ({
    root: {
        width: "100%",
        marginBottom: 10
    }

}));


function DisplaySubsetTool(props){
    const { state, dispatch } = useContext(AppContext);
    const history = useHistory();

    const [open, setOpen] = React.useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const [value, setValue] = React.useState('adjacent');

    const handleChange = (event) => {
        setValue(event.target.value);
    };

    const classes = useStyles();

    function handleDisplaySubset(){
        console.log();
    }

    return(
        <Grid
            container
            direction="column"
            justify="center"
            alignItems="center"
            className={classes.root}
        >
            <FormControl component="fieldset">
                <FormLabel component="legend">Select Type of Subset</FormLabel>
                <RadioGroup aria-label="subset-type" name="subset" value={value} onChange={handleChange}>
                    <FormControlLabel value="adjacent" control={<Radio color="primary"/>} label="Display Adjacent Nodes" />
                    <FormControlLabel value="descendent" control={<Radio color="primary"/>} label="Display Descendent Nodes" />
                </RadioGroup>
                <Button
                    variant="outlined" color="primary" onClick={handleClickOpen}
                    endIcon={<LocationSearchingIcon/>}
                >
                    Select Subset
                </Button>
                <Dialog
                    open={open}
                    onClose={handleClose}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">{"Select a node on the graph:"}</DialogTitle>
                    <DialogContent>
                        {state.selectedSentence === null ? (
                            <div>Select a sentence first.</div>
                        ) : (
                            <GraphVisualisation />
                        )}
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleClose} color="primary">
                            Disagree
                        </Button>
                        <Button onClick={handleClose} color="primary" autoFocus>
                            Agree
                        </Button>
                    </DialogActions>
                </Dialog>
            </FormControl>
        </Grid>
    );

}

export default DisplaySubsetTool;
