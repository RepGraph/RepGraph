import React, { useState } from "react";
import { Zoom } from "@visx/zoom";
import { RectClipPath } from "@visx/clip-path";
import { localPoint } from "@visx/event";

const initialTransform = {
    scaleX: 1.25,
    scaleY: 1.25,
    translateX: 0,
    translateY: 0,
    skewX: 0,
    skewY: 0
};

const ZoomPortal = (props) => {
    const { width, height, backgroundColour } = props;

    return (
        <div style={{ display: "flex", flexGrow: 1, border: "0px solid green" }}>
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
                style={{ flexGrow: 1, marginBottom: "2.5vw" }}
            >
                {(zoom) => (
                    <svg
                        width={width}
                        height={height}
                        style={{
                            cursor: zoom.isDragging ? "grabbing" : "grab",
                            border: "8px solid #dedede",
                            borderRadius: "8px"
                        }}
                        rx={8}
                        ry={8}
                    >
                        <rect width={width} height={height} fill={backgroundColour} />
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
                                const point = localPoint(event) || { x: 0, y: 0 };
                                zoom.scale({ scaleX: 1.1, scaleY: 1.1, point });
                            }}
                        />
                        <g transform={zoom.toString()}>{props.children}</g>
                    </svg>
                )}
            </Zoom>
        </div>
    );
};

export default ZoomPortal;
