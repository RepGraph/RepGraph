import React from 'react';
import Chip from '@material-ui/core/Chip';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { makeStyles } from '@material-ui/core/styles';
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
    autoComplete :{
        marginBottom: 10
    }
}));

function SearchSubgraphPatternTool(props) {
    const classes = useStyles();

    const nodeLabels = props.sentence.nodes.map(node => node.label);

    return (
        <Grid
            container
            direction="column"
            justify="center"
            alignItems="center"
        >
        <div className={classes.root}>
            <Autocomplete className={classes.autoComplete}
                          multiple
                          id="tags-outlined"
                          options={nodeLabels}
                          getOptionLabel={(option) => option}
                          defaultValue={[nodeLabels[0]]}
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
        </div>
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