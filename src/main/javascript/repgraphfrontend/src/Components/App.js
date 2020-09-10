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
            sentence: dmrsData[0],
            selectingSubset: false
        };
        this.handleSentenceSelected.bind(this);
        this.handleClose.bind(this);
        this.handleToggle.bind(this);
    }

    handleSentenceSelected = (id) => {
        this.setState((prevState)=> (
                {sentence: prevState.sentences.find(sentence => sentence.id === id)}
            )
        );
    }

    handleClose = () => {
        this.setState({selectingSubset: false});
    };

    handleToggle = () => {
        this.setState((prevState)=> (
                {selectingSubset: !prevState.selectingSubset}
            )
        );
    }

    render() {

        const {sentences, sentence, selectingSubset} = this.state; //destructure internal state

        return (
            <React.Fragment>
                <Header/>
                <Grid container spacing={1}>
                    <Grid item xs={5}>
                        <InputArea sentences={sentences}
                                   sentence={sentence}
                                   onSelect={this.handleSentenceSelected}
                                   subsetHandleToggle={this.handleToggle}/>

                    </Grid>
                    <Grid item xs={7}>
                        <VisualizerArea
                            sentence={sentence}
                            selectingSubset={selectingSubset}
                            subsetHandleClose={this.handleClose}
                        />
                    </Grid>
                </Grid>
            </React.Fragment>
        );
    }

}

export default App;
