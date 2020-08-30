import React from 'react';
import Typography from "@material-ui/core/Typography";
import Paper from "@material-ui/core/Paper";
import Divider from '@material-ui/core/Divider';
import List from '@material-ui/core/List';

const styles = {
    Paper: {
        padding: 20,
        marginBottom: 10,
        overflow:'auto'
    },
    SentencePaper: {
        height: 300,
        overflow: 'auto'
    }
};

class InputArea extends React.Component
{

    constructor(props) {
        super(props);
    }

    render(){

        return (
            <Paper style={styles.Paper} variant="elevation" elevation={5}>
                <Typography variant="h6" align="center">Select a Sentence for Visualization:</Typography>
                <Divider/>
                <List component="nav" aria-label="features">
                    <Paper style={styles.SentencePaper}>
                        <List component="ul">
                            Sentence List here
                        </List>
                    </Paper>
                </List>
                <Divider></Divider>
                <Typography variant="h6" align="center">Analysis Tools:</Typography>
                <Divider></Divider>
                Analysis Tools here
            </Paper>
        );
    }
}

export default InputArea;