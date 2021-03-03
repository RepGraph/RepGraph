import React, {useContext, useState} from "react";
import {Zoom} from "@visx/zoom";
import {RectClipPath} from "@visx/clip-path";
import {localPoint} from "@visx/event";
import {layoutHierarchy} from "../../LayoutAlgorithms/layoutHierarchy";
import {layoutTree} from "../../LayoutAlgorithms/layoutTree";
import {layoutFlat} from "../../LayoutAlgorithms/layoutFlat";
import {AppContext, defaultGraphLayoutSpacing} from "../../Store/AppContextProvider";
import ButtonGroup from "@material-ui/core/ButtonGroup";
import Button from "@material-ui/core/Button";
import { positions } from '@material-ui/system';
import Box from "@material-ui/core/Box";

import ArrowUpwardIcon from '@material-ui/icons/ArrowUpward';
import ArrowDownwardIcon from '@material-ui/icons/ArrowDownward';
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';

const initialTransform = {
    scaleX: 1.25,
    scaleY: 1.25,
    translateX: 0,
    translateY: 0,
    skewX: 0,
    skewY: 0
};

const ZoomPortal = (props) => {
    const {width, height, backgroundColour} = props;
    const {state, dispatch} = useContext(AppContext);
    const [valuesGraphSpacing, setValuesGraphSpacing] = useState(state.graphLayoutSpacing);


    const handleChangeGraphSpacing = (name) => {

        let intraValue,interValue;
        if (name === "decrease") {
            intraValue = valuesGraphSpacing.intraLevelSpacing - 20;
            interValue = valuesGraphSpacing.interLevelSpacing - 10;
        }
        else{
            intraValue = valuesGraphSpacing.intraLevelSpacing + 20;
            interValue = valuesGraphSpacing.interLevelSpacing + 10;
        }

        if (intraValue >= 0 && interValue >= 0){
            setValuesGraphSpacing({
                ...valuesGraphSpacing,
                ["intraLevelSpacing"]: intraValue,
                ["interLevelSpacing"]: interValue,
            });


            dispatch({
                type: "SET_GRAPH_LAYOUT_SPACING",
                payload: {
                    graphLayoutSpacing: {
                        ...valuesGraphSpacing,
                        ["intraLevelSpacing"]: intraValue,
                        ["interLevelSpacing"]: interValue,
                    }
                }
            });

            handleUpdateStyles({
                ...valuesGraphSpacing,
                ["intraLevelSpacing"]: intraValue,
                ["interLevelSpacing"]: interValue,
            });
        }
    };

    const handleUpdateStyles = (newSpacing) => {

        if (state.selectedSentenceID) {

            let graphData = null;

            switch (state.visualisationFormat) {
                case "1":
                    graphData = layoutHierarchy(state.selectedSentenceGraphData, newSpacing, state.framework);
                    break;
                case "2":
                    graphData = layoutTree(state.selectedSentenceGraphData, newSpacing, state.framework);
                    break;
                case "3":
                    graphData = layoutFlat(state.selectedSentenceGraphData, false, newSpacing, state.framework);
                    break;
                default:
                    graphData = layoutHierarchy(state.selectedSentenceGraphData, newSpacing, state.framework);
                    break;
            }

            dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: graphData}});
        }
    }

    return (
            <Zoom
                width={width}
                height={height}
                scaleXMin={1 / 4}
                scaleXMax={4}
                scaleYMin={1 / 4}
                scaleYMax={4}
                transformMatrix={{
                    scaleX: 1,
                    scaleY: 1,
                    translateX: 0,
                    translateY: 0,
                    skewX: 0,
                    skewY: 0
                }}

            >
                {(zoom) => (
                    <div style={{position: 'relative' }}>
                        <svg
                            width={width}
                            height={height}
                            style={{
                                cursor: zoom.isDragging ? "grabbing" : "grab",
                            }}
                        >
                            <rect width={width} height={height} fill={backgroundColour}/>
                            <rect
                                width={width}
                                height={height}
                                fill={backgroundColour}
                                onTouchStart={zoom.dragStart}
                                onTouchMove={zoom.dragMove}
                                onTouchEnd={zoom.dragEnd}
                                onMouseDown={zoom.dragStart}
                                onMouseMove={zoom.dragMove}
                                onMouseUp={zoom.dragEnd}
                                onMouseLeave={() => {
                                    if (zoom.isDragging) zoom.dragEnd();
                                }}
                                onDoubleClick={(event) => {
                                    zoom.center();
                                    //zoom.reset();
                                }}
                            />
                            <g transform={zoom.toString()}>{props.children}</g>
                        </svg>
                        <Box
                            p={2}
                            position="absolute"
                            top={0}
                            right={0}
                            zIndex="modal"
                        >
                            <ButtonGroup
                                orientation="vertical"
                                color="primary"
                                aria-label="vertical contained primary button group"
                                variant="contained"
                            >
                                <Button size="small" startIcon={<AddIcon />} onClick={ () => handleChangeGraphSpacing("increase")} disableElevation>Spacing</Button>
                                <Button size="small" startIcon={<RemoveIcon />} onClick={() => handleChangeGraphSpacing("decrease")} disableElevation>Spacing</Button>
                            </ButtonGroup>
                        </Box>
                    </div>
                )}
            </Zoom>
    );
};

export default ZoomPortal;
