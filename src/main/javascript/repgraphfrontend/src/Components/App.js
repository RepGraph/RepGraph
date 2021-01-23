import React, {useContext} from "react";
import "../../src/styles.css";
import Routes from "../../src/Routes";
import {BrowserRouter as Router} from "react-router-dom";

import {MuiThemeProvider, createMuiTheme} from "@material-ui/core/styles";

import {AppContext} from "../../src/Store/AppContextProvider";
import Backdrop from "@material-ui/core/Backdrop";
import CircularProgress from "@material-ui/core/CircularProgress";
import makeStyles from "@material-ui/core/styles/makeStyles";

//import { AppContext } from "./Store/AppContext.js";


const useStyles = makeStyles((theme) => ({
    backdrop: {
        zIndex: theme.zIndex.drawer + 1,
        color: '#fff',
    },
}));

export default function App() {
    const {state, dispatch} = useContext(AppContext);
    const classes = useStyles();


    const font = "'Open Sans', 'Helvetica', 'Arial', sans-serif";

    const primaryColor = state.darkMode ? "#444444" : "#7e006c";
    const secondaryColor = state.darkMode ? "#7e006c" : "#444444";
    const textPrimary = state.darkMode ? "#ffffff" : "#ffffff";
    const textSecondary = state.darkMode ? "#000000" : "#000000";

    const palette = {
        type: state.darkMode ? "dark" : "light",
        primary: {main: primaryColor},
        secondary: {main: secondaryColor},
        text: {
            primary: textPrimary,
            secondary: textSecondary
        },
        info: {main: "#03a9f4"},
        error: {main: "#fa5419"},
        warning: {main: "#fad431"},
        success: {main: "#70fa7f"},
        accent: {main: "#1aff00"}
    };

    const theme = createMuiTheme({
        palette,
        typography: {
            fontFamily: font,
            fontSize: 14,
            fontWeightBold: 700,
            fontWeightLight: 300,
            fontWeightMedium: 600,
            fontWeightRegular: 400,
            body1: {
                fontFamily: font,
                fontSize: "1rem",
                fontWeight: 400,
                letterSpacing: "0.00938em",
                lineHeight: 1.5
            },
            body2: {
                fontFamily: font,
                fontSize: "0.875rem",
                fontWeight: 400,
                letterSpacing: "0.01071em",
                lineHeight: 1.43
            },
            button: {
                fontFamily: font,
                fontSize: "0.875rem",
                fontWeight: 500,
                letterSpacing: "0.02857em",
                lineHeight: 1.75,
                textTransform: "uppercase"
            },
            caption: {
                fontFamily: font,
                fontSize: "0.75rem",
                fontWeight: 400,
                letterSpacing: "0.03333em",
                lineHeight: 1.66
            },
            h1: {
                fontFamily: font,
                fontSize: "6rem",
                fontWeight: 300,
                letterSpacing: "-0.01562em",
                lineHeight: 1.167
            },
            h2: {
                fontFamily: font,
                fontSize: "3.75rem",
                fontWeight: 300,
                letterSpacing: "-0.00833em",
                lineHeight: 1.2
            },
            h3: {
                fontFamily: font,
                fontSize: "3rem",
                fontWeight: 400,
                letterSpacing: "0em",
                lineHeight: 1.167
            },
            h4: {
                fontFamily: font,
                fontSize: "2.125rem",
                fontWeight: 400,
                letterSpacing: "0.00735em",
                lineHeight: 1.235
            },
            h5: {
                fontFamily: font,
                fontSize: "1.5rem",
                fontWeight: 400,
                letterSpacing: "0em",
                lineHeight: 1.334
            },
            h6: {
                fontFamily: font,
                fontSize: "1.25rem",
                fontWeight: 500,
                letterSpacing: "0.0075em",
                lineHeight: 1.6
            },
            subtitle1: {
                fontFamily: font,
                fontSize: "1rem",
                fontWeight: 400,
                letterSpacing: "0.00938em",
                lineHeight: 1.75
            },
            subtitle2: {
                fontFamily: font,
                fontSize: "0.875rem",
                fontWeight: 500,
                letterSpacing: "0.00714em",
                lineHeight: 1.57
            }
        }
    });


    return (
        <MuiThemeProvider theme={theme}>
            <Backdrop className={classes.backdrop} open={state.isLoading}>
                <CircularProgress color="inherit"/>
            </Backdrop>
            <Router>
                <Routes/>
            </Router>
        </MuiThemeProvider>
    );
}
