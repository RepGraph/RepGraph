import React, {useState} from 'react';
import Chip from '@material-ui/core/Chip';
import Autocomplete from '@material-ui/lab/Autocomplete';
import {makeStyles} from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import Grid from "@material-ui/core/Grid";
import {Typography} from "@material-ui/core";
import LocationSearchingIcon from "@material-ui/icons/LocationSearching";
import Button from "@material-ui/core/Button";

const useStyles = makeStyles((theme) => ({
    root: {
        width: "80%",
        '& > * + *': {
            marginTop: theme.spacing(3),
        },
    },
    autoComplete: {
        marginBottom: 10
    }
}));

function SearchSubgraphPatternTool(props) {
    const classes = useStyles();

    const [selectedLabels, setSelectedLabels] = useState([]);

    const {sentence} = props;

    //const nodeLabels = props.sentence.nodes.map(node => node.label);

    return (
        <Grid
            container
            direction="column"
            justify="center"
            alignItems="center"
        >
            {(sentence.nodes === undefined) ? <div>Please select sentence.</div>:
            <div className={classes.root}>
                <Autocomplete className={classes.autoComplete}
                              onChange={(event, value) => {
                                  console.log(value);
                                  setSelectedLabels(value)}}
                              multiple
                              id="tags-outlined"
                              options={sentence.nodes.map(node => node.label)}
                              getOptionLabel={(option) => option}
                              filterSelectedOptions
                              renderInput={(params) => (
                                  <TextField
                                      {...params}
                                      variant="outlined"
                                      label="Node Labels:"
                                      placeholder="Selected Node Labels"
                                  />
                              )}
                />
            </div>}
            <Button variant="contained" color="primary" onClick={
                () => {
                    console.log("Search pressed: "+selectedLabels); //Debugging
                    props.handleSearchNodeSet(selectedLabels);
                }
            }>
                Search
            </Button>
            <Typography>Or visually select a sub-graph pattern on the currently displayed graph:</Typography>
            <Button
                variant="contained"
                color="primary"
                endIcon={<LocationSearchingIcon/>}
                onClick={props.onClick}
                style={{marginBottom: 10, marginTop: 10}}
            >
                Select sub-graph pattern
            </Button>
        </Grid>

    );
}

export default SearchSubgraphPatternTool;