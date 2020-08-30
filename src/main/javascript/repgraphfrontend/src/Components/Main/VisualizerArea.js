import React from 'react';
import Typography from "@material-ui/core/Typography";
import Paper from "@material-ui/core/Paper";
import {Divider} from "@material-ui/core";
import TextareaAutosize from "@material-ui/core/TextareaAutosize";

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

        const {sentence} = this.props;

        return (

            <Paper style={styles.Paper} variant="elevation" elevation={5}>
                <Typography variant="h6" align="center">Visualization Area</Typography>
                <Divider/>
                <TextareaAutosize
                    style={styles.TextareaAutosize}
                    rowsMax={3}
                    aria-label="graph data"
                    placeholder="graph data"
                    defaultValue="graph data"
                    value={JSON.stringify(sentence)}
                />
                <Divider/>
                Graph diagram here
            </Paper>
        );
    }
}
export default VisualizerArea;