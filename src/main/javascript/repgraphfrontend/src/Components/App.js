import React from 'react';
import "./App.css";
import Grid from '@material-ui/core/Grid';
import Header from "./Layouts/Header";
import InputArea from "./Main/InputArea";
import VisualizerArea from "./Main/VisualizerArea";

import {dmrsData} from "../store";


class App extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            sentences: dmrsData,
            sentence: {}
        };
        this.handleSentenceSelected.bind(this);
    }

    handleSentenceSelected = (id) => {
        this.setState((prevState)=> (
                {sentence: prevState.sentences.find(sentence => sentence.id === id)}
            )
        );
    }

    render() {

        const {sentences, sentence} = this.state; //destructure internal state

        return (
            <React.Fragment>
                <Header/>
                <Grid container spacing={1}>
                    <Grid item xs={5}>
                        <InputArea sentences={sentences}
                                   onSelect={this.handleSentenceSelected}/>
                    </Grid>
                    <Grid item xs={7}>
                        <VisualizerArea sentence={sentence}/>
                    </Grid>
                </Grid>
            </React.Fragment>
        );
    }

}

export default App;
