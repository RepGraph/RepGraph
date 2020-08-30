import React from 'react';
import Typography from "@material-ui/core/Typography";
import Paper from "@material-ui/core/Paper";
import {Divider} from "@material-ui/core";

const styles = {
    Paper: {
        padding: 20,
        marginBottom: 10,
        height:"100%",
        overflow: "auto"
    },
    TextareaAutosize: {
        width: "100%"
    }
};


class VisualizerArea extends React.Component
{
    constructor(props) {
        super(props);
    }

    render() {

        return (

            <Paper style={styles.Paper} variant="elevation" elevation={5}>
                <Typography variant="h6" align="center">Visualization Area</Typography>
                <Divider/>
                Graph data here - debugging purposes
                <Divider/>
                Graph diagram here
            </Paper>
        );
    }
}
export default VisualizerArea;