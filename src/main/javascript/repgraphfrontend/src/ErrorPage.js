import {Button, Grid, Typography} from "@material-ui/core";
import React from "react";
import { Link, useHistory } from "react-router-dom";
import {DropzoneArea} from "material-ui-dropzone";
import CloudUploadIcon from "@material-ui/icons/CloudUpload";

export default function ErrorPage() {
  const history = useHistory();
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
          <Typography variant="h2">Error 404</Typography>
        </Grid>
        <Grid item>
          <Button onClick={() => {history.push("/")}}>Take Me Home</Button>
        </Grid>
      </Grid>
      );
}
