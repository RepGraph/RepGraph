import React, {useState, useContext} from "react";
import {Card, Typography, Button, Grid} from "@material-ui/core";
import CloudUploadIcon from "@material-ui/icons/CloudUpload";
import {Link, useHistory} from "react-router-dom";
import {DropzoneArea} from "material-ui-dropzone";
import {AppContext} from "./Store/AppContextProvider.js";
import Snackbar from '@material-ui/core/Snackbar';
import MuiAlert from '@material-ui/lab/Alert';
import ArrowForwardIcon from '@material-ui/icons/ArrowForward';
import ToggleButtonGroup from "@material-ui/lab/ToggleButtonGroup";
import ToggleButton from "@material-ui/lab/ToggleButton";
import uuid from 'react-uuid'

import {dmrsData, edsData, ptgData, uccaData, amrData} from "./store";

export default function HomePage(props) {
    const [fileObjects, setFileObjects] = useState([]);
    const [open, setOpen] = React.useState(false);
    const history = useHistory();
    const [framework, setFramework] = useState("1");
    const {state, dispatch} = useContext(AppContext);


    //Handle close for the alert shown to user
    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }

        setOpen(false);
    };

    //Handle change when user selects a file
    function handleChange(files) {
        setFileObjects([...fileObjects, ...files]);
    }

    function handleUpload() {
        let userID = uuid();
        dispatch({type: "SET_USER_ID", payload: {userID: userID}}); //Show loading animation while awaiting response

        console.log(userID); //Debugging
        if (fileObjects.length > 0) {
            let formData = new FormData();
            console.log("fileObjects", fileObjects);
            formData.append("data", fileObjects[0]);

            let myHeaders = new Headers();
            myHeaders.append("X-USER", userID);

            let requestOptions = {
                method: "POST",
                headers: myHeaders,
                body: formData,
                redirect: "follow"
            };

            dispatch({type: "SET_LOADING", payload: {isLoading: true}}); //Show loading animation while awaiting response

            //Upload the file chosen by the user
            fetch(
                state.APIendpoint + "/UploadData?FileName=SupremeLeaderJanBuys&Framework=" + state.framework,
                requestOptions
            )
                .then((response) => {
                    console.log(response);
                    if (!response.ok) {
                        throw "Uploaded File Response not OK";
                    }
                    return response.text();
                })
                .then((result) => {
                    const jsonResult = JSON.parse(result);
                    console.log(jsonResult); //Debugging
                    console.log(jsonResult.response); //Debugging

                    dispatch({type: "SET_DATASET", payload: {dataSet: jsonResult.data}}); //Store the data-set (ids and inputs stored in backend)
                    dispatch({type: "SET_DATASET_FILENAME", payload: {dataSetFileName: fileObjects[0].name}}); //store name of data-set uploaded
                    dispatch({type: "SET_DATASET_RESPONSE", payload: {dataSetResponse: jsonResult.response}}); //store response from back-end

                    //Reset state data for new dataset
                    dispatch({type: "SET_SENTENCE_GRAPHDATA", payload: {selectedSentenceGraphData: null}});
                    dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: null}});
                    dispatch({type: "SET_SELECTED_SENTENCE_ID", payload: {selectedSentenceID: null}});
                    dispatch({type: "SET_DATASET_ANALYSIS", payload: {datasetAnalysis: null}});


                    dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation


                    // if(framework === "5"){
                    //     request_AMR_align(userID);
                    // }else{
                    //     request_Token_Process(userID);
                    // }


                    history.push("/main"); //Take user to the main page

                })
                .catch((error) => {
                    console.log("error", error);
                    dispatch({type: "SET_LOADING", payload: {isLoading: false}});
                    history.push("/404"); //Take user to error page
                });
        } else {
            setOpen(true);
        }
    }

    const request_Token_Process = (userID) =>{

            let myHeaders = new Headers();
            myHeaders.append("X-USER", userID);

            let requestOptions = {
                method: 'PATCH',
                headers: myHeaders,
                redirect: 'follow'
            };

            fetch(state.APIendpoint+"/ParseTokens", requestOptions)
                .then(response => response.text())
                .then(result => console.log(result))
                .catch(error => console.log('error', error));

    }

    const request_AMR_align = (userID) =>{

            let myHeaders = new Headers();
            myHeaders.append("X-USER", userID);

            let requestOptions = {
                method: 'PATCH',
                headers: myHeaders,
                redirect: 'follow'
            };

            fetch(state.APIendpoint+"/AlignAMR", requestOptions)
                .then(response => response.text())
                .then(result => console.log(result))
                .catch(error => console.log('error', error));

    }

    function handleSkip() {

        let userID = uuid();
        dispatch({type: "SET_USER_ID", payload: {userID: userID}}); //Show loading animation while awaiting response

        let myHeaders = new Headers();
        myHeaders.append("X-USER", userID);
        myHeaders.append("Content-Type", "application/json");

        let raw = null;

        switch (framework) {
            case "1":
                raw = JSON.stringify({"data": dmrsData});
                break;
            case "2":
                raw = JSON.stringify({"data": edsData});
                break;
            case "3":
                raw = JSON.stringify({"data": ptgData});
                break;
            case "4":
                raw = JSON.stringify({"data": uccaData});
                break;
            case "5":
                raw = JSON.stringify({"data": amrData});
                break;
        }

        console.log(raw);

        let requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow'
        };

        fetch(state.APIendpoint+"/UploadDemo?Framework="+ framework, requestOptions)
            .then((response) => {
                        console.log(response);
                        if (!response.ok) {
                            throw "Uploaded File Response not OK";
                        }
                        return response.text();
                    })
            .then((result) => {
                    const jsonResult = JSON.parse(result);
                    console.log(jsonResult); //Debugging
                    console.log(jsonResult.response); //Debugging

                    dispatch({type: "SET_DATASET", payload: {dataSet: jsonResult.data}}); //Store the data-set (ids and inputs stored in backend)
                    dispatch({type: "SET_DATASET_FILENAME", payload: {dataSetFileName: "DemoData"}}); //store name of data-set uploaded
                    dispatch({type: "SET_DATASET_RESPONSE", payload: {dataSetResponse: jsonResult.response}}); //store response from back-end

                    dispatch({type: "SET_SENTENCE_GRAPHDATA", payload: {selectedSentenceGraphData: null}});
                    dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: null}});
                    dispatch({type: "SET_SELECTED_SENTENCE_ID", payload: {selectedSentenceID: null}});
                    dispatch({type: "SET_DATASET_ANALYSIS", payload: {datasetAnalysis: null}});

                    dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation
                    history.push("/main"); //Take user to the main page

                }).catch((error) => {
                        console.log("error", error);
                        dispatch({type: "SET_LOADING", payload: {isLoading: false}});
                        history.push("/404"); //Take user to error page
                    });

    }

    const handleFormatChange = (event, newFormat) => {
        if (newFormat !== null) {
            setFramework(newFormat);
            dispatch({type: "SET_FRAMEWORK", payload: {framework: newFormat}}); //Show loading animation while awaiting response
        }
    }

    return (
        <Grid
            container
            direction="column"
            justify="center"
            alignItems="center"
            style={{minHeight: "100vh", minWidth: "100vw"}}

            spacing={2}
        >
            <Grid item>
                <Typography color="primary" variant="h2">Welcome to RepGraph</Typography>
            </Grid>
            <ToggleButtonGroup
                value={state.framework}
                exclusive
                onChange={handleFormatChange}
                aria-label="Framework formats"
            >
                <ToggleButton color="primary" value="1" aria-label="DMRS">
                    <Typography color="textPrimary">DMRS</Typography>
                </ToggleButton>
                <ToggleButton color="primary" value="2" aria-label="EDS">
                    <Typography color="textPrimary">EDS</Typography>
                </ToggleButton>
                <ToggleButton color="primary" value="3" aria-label="PTG">
                    <Typography color="textPrimary">PTG</Typography>
                </ToggleButton>
                <ToggleButton color="primary" value="4" aria-label="UCCA">
                    <Typography color="textPrimary">UCCA</Typography>
                </ToggleButton>
                <ToggleButton color="primary" value="5" aria-label="AMR">
                    <Typography color="textPrimary">AMR</Typography>
                </ToggleButton>
            </ToggleButtonGroup>
            <Grid item>
                <Typography variant="h5">Please upload a data-set to begin</Typography>
            </Grid>
            <Grid item style={{minWidth: "50vw"}}>
                <DropzoneArea
                    acceptedFiles={[".dmrs", ".eds", ".amr", ".ptg", ".ucca", ".mrp"]}
                    dropzoneText={"Drag and drop"}
                    icon={<CloudUploadIcon/>}
                    onChange={(files) => {
                        console.log("Files:", files);
                        handleChange(files);
                    }}
                />
            </Grid>
            <Grid item>
                <Button onClick={handleUpload} color="primary" variant="contained" disableElevation
                        startIcon={<CloudUploadIcon/>}>
                    <Typography variant="h6">Upload</Typography>
                </Button>
            </Grid>
            <Grid><Button onClick={handleSkip} color="primary" variant="contained" disableElevation
                          startIcon={<ArrowForwardIcon/>}>
                <Typography variant="h6">Skip Upload </Typography>
            </Button></Grid>
            <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                <MuiAlert elevation={6} variant="filled" onClose={handleClose} severity="warning">
                    Please select a data-set first.
                </MuiAlert>
            </Snackbar>

        </Grid>
    );
}
