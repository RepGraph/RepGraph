import React, { useState, useContext } from "react";
import { Card, Typography, Button, Grid } from "@material-ui/core";
import CloudUploadIcon from "@material-ui/icons/CloudUpload";
import { Link, useHistory } from "react-router-dom";
import { DropzoneArea } from "material-ui-dropzone";
import { AppContext } from "./Store/AppContextProvider.js";
import Snackbar from '@material-ui/core/Snackbar';
import MuiAlert from '@material-ui/lab/Alert';

export default function HomePage(props) {
  const [fileObjects, setFileObjects] = useState([]);
  const [open, setOpen] = React.useState(false);
  const history = useHistory();
  const { state, dispatch } = useContext(AppContext);

  //Handle close for the alert shown to user
  const handleClose = (event, reason) => {
    if (reason === 'clickaway') {
      return;
    }

    setOpen(false);
  };

  //Handle change when user selects a file
  function handleChange(files) {
    setFileObjects([ ...fileObjects, ...files ]);
  }

  function handleUpload() {
    console.log(fileObjects); //Debugging
    if(fileObjects.length > 0){
      let formData = new FormData();
      formData.append("data", fileObjects[0]);

      let requestOptions = {
        method: "POST",
        body: formData,
        redirect: "follow"
      };

      dispatch({ type: "SET_LOADING", payload: { isLoading: true } }); //Show loading animation while awaiting response

      //Upload the file chosen by the user
      fetch(
          state.APIendpoint + "/UploadData?FileName=SupremeLeaderJanBuys",
          requestOptions
      )
          .then((response) => response.text())
          .then((result) => {
            const jsonResult = JSON.parse(result);
            console.log(jsonResult); //Debugging
            console.log(jsonResult.response); //Debugging
            dispatch({ type: "SET_DATASET", payload: { dataSet: jsonResult.data } }); //Store the data-set (ids and inputs stored in backend)
            dispatch({ type: "SET_DATASET_FILENAME", payload: { dataSetFileName: fileObjects[0].name } }); //store name of data-set uploaded
            dispatch({ type: "SET_DATASET_RESPONSE", payload: { dataSetResponse: jsonResult.response } }); //store response from back-end
            dispatch({ type: "SET_LOADING", payload: { isLoading: false } }); //Stop the loading animation
            history.push("/main"); //Take user to the main page
          })
          .catch((error) => {
            console.log("error", error);
            dispatch({ type: "SET_LOADING", payload: { isLoading: false } });
            history.push("/404"); //Take user to error page
          });
    }else {
    setOpen(true);
    }
  }

  return (
    <Grid
      container
      direction="column"
      justify="center"
      alignItems="center"
      style={{ minHeight: "100vh", minWidth: "100vw" }}
      spacing={2}
    >
      <Grid item>
          <Typography color="primary" variant="h2">Welcome to RepGraph</Typography>
      </Grid>
      <Grid item>
        <Typography variant="h5">Please upload a data-set to begin</Typography>
      </Grid>
      <Grid item style={{ minWidth: "50vw" }}>
        <DropzoneArea
          acceptedFiles={[".dmrs"]}
          dropzoneText={"Drag and drop"}
          icon={<CloudUploadIcon />}
          onChange={(files) => {
            console.log("Files:", files);
            handleChange(files);
          }}
        />
      </Grid>
      <Grid item>
          <Button onClick={handleUpload} color={"primary"} variant={"outlined"} startIcon={<CloudUploadIcon/>}>
          <Typography variant="h6">Upload</Typography>
        </Button>
      </Grid>
      <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
        <MuiAlert elevation={6} variant="filled" onClose={handleClose} severity="warning">
          Please select a data-set first.
        </MuiAlert>
      </Snackbar>
    </Grid>
  );
}
