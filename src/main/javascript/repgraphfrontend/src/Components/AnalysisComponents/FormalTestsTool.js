import React from 'react';
import FormControl from "@material-ui/core/FormControl";
import FormLabel from "@material-ui/core/FormLabel";
import FormGroup from "@material-ui/core/FormGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import Button from "@material-ui/core/Button";
import ArrowForwardIcon from "@material-ui/icons/ArrowForward";
import FormHelperText from "@material-ui/core/FormHelperText";
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/core/styles";


const useStyles = makeStyles((theme) => ({
    root: {
        width: "100%",
        marginBottom: 10
    }

}));

function FormalTestsTool(props){

    const classes = useStyles();

    return(
        <Grid
            container
            direction="column"
            justify="center"
            alignItems="center"
            className={classes.root}
        >
            <FormControl component="fieldset">
                <FormLabel component="legend">Graph Property Tests</FormLabel>
                <FormGroup>
                    <FormControlLabel
                        control={<Checkbox color="primary"  name="Planar" />}
                        label="Graph Planar?"
                    />
                    <FormControlLabel
                        control={<Checkbox  color="primary" name="Path" />}
                        label="Find Longest Path"
                    />
                    <FormControlLabel
                        control={<Checkbox  color="primary" name="Connected" />}
                        label="Graph Connected?"
                    />
                    <Button
                        variant="contained"
                        color="primary"
                        endIcon={<ArrowForwardIcon/>}
                    >
                        Run Tests
                    </Button>
                </FormGroup>
                <FormHelperText></FormHelperText>
            </FormControl>
        </Grid>
    );

}

export default FormalTestsTool;
