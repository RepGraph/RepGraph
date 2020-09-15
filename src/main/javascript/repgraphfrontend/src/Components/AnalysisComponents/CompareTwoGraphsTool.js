import React from 'react';
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/core/styles";
import Tooltip from "@material-ui/core/Tooltip";
import Zoom from "@material-ui/core/Zoom";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Divider from "@material-ui/core/Divider";
import List from "@material-ui/core/List";
import TextField from "@material-ui/core/TextField";
import Autocomplete from "@material-ui/lab/Autocomplete";


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

function CompareTwoGraphsTool(props){

    const classes = useStyles();

    const parsedSentences = props.sentences;
    const parsedSentencesInputs = parsedSentences.map(sentenceObj => sentenceObj.input);

    return(
        <Grid
            container
            direction="column"
            justify="center"
            alignItems="center"
            className={classes.root}
        >
            <div className={classes.root}>
            <Autocomplete className={classes.autoComplete}
                          multiple
                          id="tags-outlined"
                          options={parsedSentencesInputs}
                          getOptionLabel={(option) => option}
                          defaultValue={[parsedSentencesInputs[0]]}
                          filterSelectedOptions
                          renderInput={(params) => (
                              <TextField
                                  {...params}
                                  variant="outlined"
                                  label="Select two sentences for comparison:"
                                  placeholder="Selected Sentences"
                              />
                          )}
            />
            </div>
        </Grid>
    );

}

export default CompareTwoGraphsTool;
