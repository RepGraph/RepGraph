import React, {useState} from "react";
import {Zoom} from "@visx/zoom";
import {RectClipPath} from "@visx/clip-path";
import {localPoint} from "@visx/event";

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
                                    zoom.center()
                                }}
                            />
                            <g transform={zoom.toString()}>{props.children}</g>
                        </svg>
                        <div className="controls">
                            <button
                                type="button"
                                className="btn"
                                onClick={() => zoom.scale({scaleX: 1.2, scaleY: 1.2})}
                            >
                                Increase Spacing
                            </button>
                            <button
                                type="button"
                                className="btn btn-bottom"
                                onClick={() => zoom.scale({scaleX: 0.8, scaleY: 0.8})}
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
