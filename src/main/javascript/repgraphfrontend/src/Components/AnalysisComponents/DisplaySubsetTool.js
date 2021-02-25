import React, {useContext} from 'react';
import {makeStyles} from "@material-ui/core/styles";
import FormLabel from "@material-ui/core/FormLabel";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Radio from "@material-ui/core/Radio";
import Button from "@material-ui/core/Button";
import LocationSearchingIcon from '@material-ui/icons/LocationSearching';
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import {
    Typography
} from "@material-ui/core";

import {AppContext} from "../../Store/AppContextProvider.js";
import {useHistory} from "react-router-dom";
import GraphVisualisation from "../Main/GraphVisualisation";
import Chip from "@material-ui/core/Chip";

import SubsetVisualisation from "../Main/SubsetVisualisation";
import CardContent from "@material-ui/core/CardContent";
import {cloneDeep} from "lodash";

import {Graph} from "../Graph/Graph";
import {determineAdjacentLinks, layoutHierarchy} from "../../LayoutAlgorithms/layoutHierarchy";
import {layoutTree} from "../../LayoutAlgorithms/layoutTree";
import {layoutFlat} from "../../LayoutAlgorithms/layoutFlat";
import {ParentSize} from "@visx/responsive";

const useStyles = makeStyles((theme) => ({
    root: {
        height: "100%",
        width: "100%",
    },
    graphDiv: {
        height: "100%",
        //border: "1px solid red",
        flex: "1",
        width: "100%"
    }
}));

function DisplaySubsetTool(props) {
    const {state, dispatch} = useContext(AppContext); //Provide access to global state
    const [subsetResponse, setSubsetResponse] = React.useState(null); //Store selected subset response in local state

    const history = useHistory(); //Use history for page routing

    const [open, setOpen] = React.useState(false); //Store local state for whether dialog is open or not
    const [resultOpen, setResultOpen] = React.useState(false); //Store local state for whether result dialog is open or not
    const [selectedNodes, setSelectedNodes] = React.useState(null); //Store local state for currently selected node ids

    //Determine graphFormatCode
    let graphFormatCode = null;
    switch (state.visualisationFormat) {
        case "1":
            graphFormatCode = "hierarchical";
            break;
        case "2":
            graphFormatCode = "tree";
            break;
        case "3":
            graphFormatCode = "flat";
            break;
        default:
            graphFormatCode = "hierarchical";
            break;
    }

    //Handle click open for first dialog to select subset
    const handleClickOpen = () => {
        setOpen(true);
    };
    //Handle close for first dialog to select subset
    const handleClose = () => {
        setOpen(false);
    };

    //handle close for second dialog to display subset result
    const handleResultClose = () => {
        setResultOpen(false);
    };

    const [subsetType, setSubsetType] = React.useState('adjacent'); //Store subset type in local state

    //Handle change of selected subset type
    const handleChange = (event) => {
        setSubsetType(event.target.value);
    };

    const classes = useStyles();

    function handleDisplaySubset() {
        setOpen(false);

        let myHeaders = new Headers();
        myHeaders.append("X-USER", state.userID);
        let requestOptions = {
            method: 'GET',
            headers : myHeaders,
            redirect: 'follow'
        };

        console.log(state.selectedSentenceID, selectedNodes[0].id); //Debugging
        dispatch({type: "SET_LOADING", payload: {isLoading: true}}); //Show loading animation

        console.log(state.visualisationFormat); //Debugging

        //Request subset from backend
        fetch(state.APIendpoint + "/GetSubset?graphID=" + state.selectedSentenceID + "&NodeID=" + selectedNodes[0] + "&SubsetType=" + subsetType, requestOptions)
            .then((response) => {
                if (!response.ok) {
                    throw "Response not OK";
                }
                return response.text();
            })
            .then((result) => {
                console.log(result);//debugging
                console.log(JSON.parse(result));//debugging
                const jsonResult = JSON.parse(result); //Parse response into JSON

                let graphData = null;

                switch (state.visualisationFormat) {
                    case "1":
                        graphData = layoutHierarchy(jsonResult, state.graphLayoutSpacing);
                        break;
                    case "2":
                        graphData = layoutTree(jsonResult, state.graphLayoutSpacing);
                        break;
                    case "3":
                        graphData = layoutFlat(jsonResult,false, state.graphLayoutSpacing);
                        break;
                    default:
                        graphData = layoutHierarchy(jsonResult, state.graphLayoutSpacing);
                        break;
                }

                console.log("graphData", graphData);
                console.log("graphData", JSON.stringify(graphData));

                setSelectedNodes(null);
                setSubsetResponse(graphData); //Store the response in local state
                setResultOpen(true); //Show the result in dialog

                // dispatch({type: "SET_SENTENCE_GRAPHDATA", payload: {selectedSentenceGraphData: jsonResult}});
                // dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: graphData}});
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation

                //setOpen(false);

            })
            .catch((error) => {
                dispatch({type: "SET_LOADING", payload: {isLoading: false}}); //Stop the loading animation
                console.log("error", error); //Log the error
                history.push("/404"); //Take user to error page
            });
    }

    //Events that are enabled on the graph visualisation
    // const events = {
    //     //Select event for when user selects a node on the graph
    //     select: function (event) {
    //         let { nodes, edges } = event;
    //
    //         let currentStandardVisualisation = cloneDeep(state.selectedSentenceVisualisation); //Deep clone the current visualisation
    //         let nodeSelected = null; //value to set local state after event
    //         //User selected or deselected a node -> update the graph visually
    //         if(nodes.length > 0){
    //             for (const [i, x] of currentStandardVisualisation.nodes.entries()) {
    //                 if (x.id === nodes[0] && (x.group === "node" || x.group === "surfaceNode")) {
    //                     //Mark as selected
    //                     currentStandardVisualisation.nodes[i].group = "Selected";
    //                     nodeSelected = {id: x.id, label: x.label}; //new local state value
    //                     break;
    //                 }
    //             }
    //         }
    //         console.log(nodeSelected);
    //         setSelectedNode(nodeSelected); //set local state for selected node
    //         setCurrentVis(currentStandardVisualisation); //Update the current visualisation to reflect the selections made by the user
    //     }
    // };

    const events = {
        select: {
            selectMode: "subset",
            selectedNodesStateSetter: setSelectedNodes,
        }
    }

    function getSelectedNodeLabel(){
        if(selectedNodes !== null && selectedNodes.length === 1){ //User has selected a node
            return state.selectedSentenceGraphData.nodes.find(node => node.id === selectedNodes[0]).label;
        }else{ //User has not selected a node
            return "No node selected";
        }
    }

    return (
        <CardContent>
            <FormLabel><Typography color={"textPrimary"}>Select Type of Subset</Typography></FormLabel>
            <RadioGroup aria-label="subset-type" name="subset" value={subsetType} onChange={handleChange}>
                <FormControlLabel value="adjacent" control={<Radio />} label={<Typography color={"textPrimary"}> Display Adjacent Nodes</Typography>}/>
                <FormControlLabel value="descendent" control={<Radio color="secondary"/>}
                                  label={<Typography color={"textPrimary"}> Display Descendent Nodes</Typography>}/>
            </RadioGroup>
            <Button
                variant="contained" color="primary" onClick={handleClickOpen}
                endIcon={<LocationSearchingIcon/>}
                disabled={state.selectedSentenceID === null}
            >
                Select Node
            </Button>
            <Dialog
                open={open}
                fullWidth={true}
                maxWidth="xl"
                onClose={handleClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">{"Select a node on the graph:"}<Chip label={`Selected Node: ${getSelectedNodeLabel()}`}/></DialogTitle>
                <DialogContent style={{height: "80vh"}}>
                    {state.selectedSentenceID === null ? (
                        <div>Select a sentence first.</div>
                    ) : (
                        // <Graph
                        //     graph={currentVis}
                        //     options={state.visualisationOptions} //Options from global state
                        //     events={events}
                        //     getNetwork={(network) => {
                        //         network.on("hoverNode", function (params) {
                        //             network.canvas.body.container.style.cursor = 'pointer'
                        //         });
                        //     }}
                        // />
                        <div className={classes.graphDiv}>
                            <ParentSize>
                                {parent => (
                                    <Graph
                                        width={parent.width}
                                        height={parent.height}
                                        graph={state.selectedSentenceVisualisation}
                                        adjacentLinks={determineAdjacentLinks(state.selectedSentenceVisualisation)}
                                        graphFormatCode={graphFormatCode}
                                        events={events}
                                    />
                                    // <Graph
                                    //     width={parent.width}
                                    //     height={parent.height}
                                    //     graph={JSON.parse(
                                    //         '{"nodes":[{"id":"5","label":"_year_n_1","anchors":[{"from":3,"end":3}],"properties":null,"values":null,"x":130,"y":120,"relativeX":1,"relativeY":0,"type":"node","group":"node","span":true},{"id":"8","label":"_old_a_1","anchors":[{"from":4,"end":4}],"properties":null,"values":null,"x":260,"y":120,"relativeX":2,"relativeY":0,"type":"node","group":"node","span":true},{"id":"6","label":"measure","anchors":[{"from":2,"end":3}],"properties":null,"values":null,"x":65,"y":0,"relativeX":0,"relativeY":1,"type":"node","group":"node","span":true},{"index":2,"form":"61","lemma":"61","carg":"61","x":0,"y":300,"label":"61","type":"token","group":"token"},{"index":3,"form":"years","lemma":"year","carg":null,"x":130,"y":300,"label":"years","type":"token","group":"token"},{"index":4,"form":"old,","lemma":"old","carg":null,"x":260,"y":300,"label":"old,","type":"token","group":"token"}],"links":[{"id":0,"source":{"id":"6","label":"measure","anchors":[{"from":2,"end":3}],"properties":null,"values":null,"x":65,"y":0,"relativeX":0,"relativeY":1,"type":"node","group":"node","span":true},"target":{"id":"8","label":"_old_a_1","anchors":[{"from":4,"end":4}],"properties":null,"values":null,"x":260,"y":120,"relativeX":2,"relativeY":0,"type":"node","group":"node","span":true},"label":"ARG1","x1":162.5,"y1":60,"type":"link"},{"id":1,"source":{"id":"6","label":"measure","anchors":[{"from":2,"end":3}],"properties":null,"values":null,"x":65,"y":0,"relativeX":0,"relativeY":1,"type":"node","group":"node","span":true},"target":{"id":"5","label":"_year_n_1","anchors":[{"from":3,"end":3}],"properties":null,"values":null,"x":130,"y":120,"relativeX":1,"relativeY":0,"type":"node","group":"node","span":true},"label":"ARG2","x1":97.5,"y1":60,"type":"link"}]}'
                                    //     )}
                                    //     adjacentLinks={determineAdjacentLinks(
                                    //         JSON.parse(
                                    //             '{"nodes":[{"id":"5","label":"_year_n_1","anchors":[{"from":3,"end":3}],"properties":null,"values":null,"x":130,"y":120,"relativeX":1,"relativeY":0,"type":"node","group":"node","span":true},{"id":"8","label":"_old_a_1","anchors":[{"from":4,"end":4}],"properties":null,"values":null,"x":260,"y":120,"relativeX":2,"relativeY":0,"type":"node","group":"node","span":true},{"id":"6","label":"measure","anchors":[{"from":2,"end":3}],"properties":null,"values":null,"x":65,"y":0,"relativeX":0,"relativeY":1,"type":"node","group":"node","span":true},{"index":2,"form":"61","lemma":"61","carg":"61","x":0,"y":300,"label":"61","type":"token","group":"token"},{"index":3,"form":"years","lemma":"year","carg":null,"x":130,"y":300,"label":"years","type":"token","group":"token"},{"index":4,"form":"old,","lemma":"old","carg":null,"x":260,"y":300,"label":"old,","type":"token","group":"token"}],"links":[{"id":0,"source":{"id":"6","label":"measure","anchors":[{"from":2,"end":3}],"properties":null,"values":null,"x":65,"y":0,"relativeX":0,"relativeY":1,"type":"node","group":"node","span":true},"target":{"id":"8","label":"_old_a_1","anchors":[{"from":4,"end":4}],"properties":null,"values":null,"x":260,"y":120,"relativeX":2,"relativeY":0,"type":"node","group":"node","span":true},"label":"ARG1","x1":162.5,"y1":60,"type":"link"},{"id":1,"source":{"id":"6","label":"measure","anchors":[{"from":2,"end":3}],"properties":null,"values":null,"x":65,"y":0,"relativeX":0,"relativeY":1,"type":"node","group":"node","span":true},"target":{"id":"5","label":"_year_n_1","anchors":[{"from":3,"end":3}],"properties":null,"values":null,"x":130,"y":120,"relativeX":1,"relativeY":0,"type":"node","group":"node","span":true},"label":"ARG2","x1":97.5,"y1":60,"type":"link"}]}'
                                    //         )
                                    //     )}
                                    //     graphFormatCode={graphFormatCode}
                                    // />
                                )}
                            </ParentSize>
                        </div>


                    )}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDisplaySubset} color="secondary" autoFocus disabled={selectedNodes === null || selectedNodes.length === 0}>
                        Display
                    </Button>
                </DialogActions>
            </Dialog>
            <Dialog
                open={resultOpen}
                fullWidth={true}
                maxWidth="xl"
                onClose={handleResultClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">{"Subset result based upon node selection:"}</DialogTitle>
                <DialogContent style={{height: "80vh"}}>
                    {subsetResponse === null ? (
                        <div>No result to display</div>
                    ) : (
                        <div className={classes.graphDiv}>
                            <ParentSize>
                                {parent => (
                                    <Graph
                                        width={parent.width}
                                        height={parent.height}
                                        graph={subsetResponse}
                                        adjacentLinks={determineAdjacentLinks(subsetResponse)}
                                        graphFormatCode={graphFormatCode}
                                    />
                                )}
                            </ParentSize>
                        </div>
                    )}
                </DialogContent>
                {/*{subsetResponse === null ? (*/}
                {/*    <div>No result to display</div>*/}
                {/*) : (*/}
                {/*    <div className={classes.graphDiv}>*/}
                {/*        <ParentSize>*/}
                {/*            {parent => (*/}
                {/*                <Graph*/}
                {/*                    width={parent.width}*/}
                {/*                    height={parent.height}*/}
                {/*                    graph={subsetResponse}*/}
                {/*                    adjacentLinks={determineAdjacentLinks(subsetResponse)}*/}
                {/*                    graphFormatCode={graphFormatCode}*/}
                {/*                />*/}
                {/*            )}*/}
                {/*        </ParentSize>*/}
                {/*    </div>*/}
                {/*)}*/}
                <DialogActions>
                    <Button onClick={handleResultClose} color="secondary" autoFocus>
                        close
                    </Button>
                </DialogActions>
            </Dialog>
        </CardContent>
    );

}

export default DisplaySubsetTool;
