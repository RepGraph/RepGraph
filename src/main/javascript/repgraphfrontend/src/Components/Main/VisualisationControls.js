import React from "react";
import Typography from "@material-ui/core/Typography";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import FormControl from "@material-ui/core/FormControl";
import FormLabel from "@material-ui/core/FormLabel";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Radio from "@material-ui/core/Radio";
import Grid from "@material-ui/core/Grid";

export default function VisualisationControls() {
  return (
    <Grid item xs={12}>
      <Accordion>
        <AccordionSummary
          expandIcon={<ExpandMoreIcon />}
          aria-controls="visualization-controls"
          id="visualization-controls-header"
        >
          <Typography>Visualization Control Panel</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <FormControl component="fieldset">
            <FormLabel component="legend">
              Select visualization format
            </FormLabel>
            <RadioGroup
              row
              aria-label="visualization-format"
              name="format"
              defaultValue="format1"
            >
              <FormControlLabel
                value="format1"
                control={<Radio color="primary" />}
                label="Flat/Hierarchical"
              />
              <FormControlLabel
                value="format2"
                control={<Radio color="primary" />}
                label="Tree-like"
              />
            </RadioGroup>
          </FormControl>
        </AccordionDetails>
      </Accordion>
    </Grid>
  );
}
