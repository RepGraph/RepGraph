import React from 'react';
import Typography from "@material-ui/core/Typography";
import Paper from "@material-ui/core/Paper";
import Divider from '@material-ui/core/Divider';
import List from '@material-ui/core/List';
import Tooltip from "@material-ui/core/Tooltip";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Zoom from "@material-ui/core/Zoom";

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

        const parsedSentences = this.props.sentences;
        console.log(parsedSentences);
        const onSelect = this.props.onSelect;

        return (
            <Paper style={styles.Paper} variant="elevation" elevation={5}>
                <Typography variant="h6" align="center">Select a Sentence for Visualization:</Typography>
                <Divider/>
                <List component="nav" aria-label="features">
                    <Paper style={styles.SentencePaper}>
                        <List component="ul">
                            {parsedSentences.map((sentence)=>
                                <Tooltip TransitionComponent={Zoom} title="Display Graph >" placement="right">
                                    <div>
                                        <ListItem button key={sentence.id} onClick={()=>onSelect(sentence.id)}>
                                            <ListItemText
                                                primary={sentence.input}
                                            >
                                            </ListItemText>
                                        </ListItem>
                                        <Divider/>
                                    </div>
                                </Tooltip>)}
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