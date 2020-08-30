import React from 'react';
import "./App.css";
import Grid from '@material-ui/core/Grid';


class App extends React.Component{
    constructor(props) {
        super(props);
    }

    render() {

        return (
            <React.Fragment>
                Header here
                <Grid container spacing={1}>
                    <Grid item xs={5}>
                        Input Area here
                    </Grid>
                    <Grid item xs={7}>
                        Analysis Area here
                    </Grid>
                </Grid>
            </React.Fragment>
        );
    }

}

export default App;
