import React, {useContext, useState} from "react";
import {Zoom} from "@visx/zoom";
import {RectClipPath} from "@visx/clip-path";
import {localPoint} from "@visx/event";
import {layoutHierarchy} from "../../LayoutAlgorithms/layoutHierarchy";
import {layoutTree} from "../../LayoutAlgorithms/layoutTree";
import {layoutFlat} from "../../LayoutAlgorithms/layoutFlat";
import {AppContext, defaultGraphLayoutSpacing} from "../../Store/AppContextProvider";

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


    const handleChangeGraphSpacing = (event) => {

        let intraValue,interValue;
        console.log("event", event);
        if (event.target.name === "decrease") {
            intraValue = valuesGraphSpacing.intraLevelSpacing - 20;
            interValue = valuesGraphSpacing.interLevelSpacing - 10;
        }
        else{
            intraValue = valuesGraphSpacing.intraLevelSpacing + 20;
            interValue = valuesGraphSpacing.interLevelSpacing + 10;
        }

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
    };

    const handleUpdateStyles = (newSpacing) => {

        if (state.selectedSentenceID) {

            let graphData = null;

            switch (state.visualisationFormat) {
                case "1":
                    graphData = layoutHierarchy(state.selectedSentenceGraphData, newSpacing);
                    break;
                case "2":
                    graphData = layoutTree(state.selectedSentenceGraphData, newSpacing);
                    break;
                case "3":
                    graphData = layoutFlat(state.selectedSentenceGraphData, false, newSpacing);
                    break;
                default:
                    graphData = layoutHierarchy(state.selectedSentenceGraphData, newSpacing);
                    break;
            }

            dispatch({type: "SET_SENTENCE_VISUALISATION", payload: {selectedSentenceVisualisation: graphData}});
        }
    }

    return (
        <div style={{display: "flex", flexGrow: 1, border: "0px solid green"}}>
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
                    <div className="relative">
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
                                    zoom.reset();
                                }}
                            />
                            <g transform={zoom.toString()}>{props.children}</g>
                        </svg>
                        <div className="controls">
                            <button
                                type="button"
                                className="btn"
                                name="increase"
                                onClick={handleChangeGraphSpacing}
                            >
                                Increase Spacing
                            </button>
                            <button
                                type="button"
                                className="btn btn-bottom"
                                name="decrease"
                                onClick={handleChangeGraphSpacing}
                            >
                               Decrease Spacing
                            </button>
                        </div>
                    </div>
                )}
            </Zoom>
            <style jsx>{`
        .btn {
          margin-top: 10px;
          text-align: center;
          border: none;
          background: #cfcfcf;
          color: #000000;
          padding-top: 10px;
          padding-bottom: 10px;
          width: 75px;
          font-size: 15px;
        }
        .btn-bottom {
          margin-bottom: 1rem;
        }
        .controls {
          position: absolute;
          top: 15px;
          right: 15px;
          display: flex;
          flex-direction: column;
          align-items: flex-end;
        } 
        .relative {
          position: relative;
        }
      `}</style>
        </div>
    );
};

export default ZoomPortal;
