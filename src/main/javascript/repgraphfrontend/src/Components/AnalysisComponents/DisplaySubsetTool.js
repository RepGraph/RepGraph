import React from 'react';
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

const useStyles = makeStyles((theme) => ({
    root: {
        width: "100%",
        marginBottom: 10
    }

}));


function DisplaySubsetTool(props){

    const [value, setValue] = React.useState('adjacent');

    const handleChange = (event) => {
        setValue(event.target.value);
    };

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
                <FormLabel component="legend">Select Type of Subset</FormLabel>
                <RadioGroup aria-label="subset-type" name="subset" value={value} onChange={handleChange}>
                    <FormControlLabel value="adjacent" control={<Radio color="primary"/>} label="Display Adjacent Nodes" />
                    <FormControlLabel value="descendent" control={<Radio color="primary"/>} label="Display Descendent Nodes" />
                </RadioGroup>
                <Button
                    variant="contained"
                    color="primary"
                    endIcon={<LocationSearchingIcon/>}
                    onClick={props.onClick}
                >
                    Select Subset
                </Button>
            </FormControl>
        </Grid>
    );

}

export default DisplaySubsetTool;
