import React, {useState, useEffect, useContext} from "react";
import Graph from "react-graph-vis";
import {AppContext} from "../../Store/AppContextProvider.js";


const GraphVisualisation = () => {
    const {state, dispatch} = useContext(AppContext);

    const events = {
        select: function (event) {
            let {nodes, edges} = event;
            console.log(nodes, edges);
            dispatch({type: "SET_SELECT_NODE_EDGE", payload: {selectedNodeAndEdges: {nodes, edges}}});
        }
    };

return (
    <Graph
        graph={state.selectedSentenceVisualisation}
        options={state.visualisationOptions} //Options from global state
        events={events}
        style={{width: "100%", height: "100%"}}
        getNetwork={(network) => {
            //  if you want access to vis.js network api you can set the state in a parent component using this property
            network.on("hoverNode", function (params) {
                network.canvas.body.container.style.cursor = 'pointer'
            });
            network.on("beforeDrawing", function (ctx) {
                //console.log(state.selectedSentence.nodes);
                /*
                for (let node of state.selectedSentence.nodes) {
                  if (node.type === "node") {
                    let nodeId = node.id;
                    let nodePosition = network.getPositions([nodeId]);
                    ctx.strokeStyle = "#A6D5F7";
                    ctx.fillStyle = "#294475";
                    ctx.lineWidth = 3;

                    let nodeBoundingBox = network.getBoundingBox(nodeId);
                    console.log(nodePosition[nodeId]);

                    ctx.beginPath();
                    ctx.moveTo(
                      nodePosition[nodeId].x - 40,
                      nodeBoundingBox.bottom + 5
                    );
                    ctx.lineTo(nodePosition[nodeId].x - 40, nodeBoundingBox.bottom);
                    ctx.lineTo(
                      nodePosition[nodeId].x +
                        40 +
                        60 * (node.anchors.end - node.anchors.from),
                      nodeBoundingBox.bottom
                    );
                    ctx.lineTo(
                      nodePosition[nodeId].x +
                        40 +
                        60 * (node.anchors.end - node.anchors.from),
                      nodeBoundingBox.bottom + 5
                    );

                    ctx.stroke();
                    //ctx.fill();
                    //ctx.stroke();
                  }
                }*/
            });
        }}
    />
);
}
;

export default GraphVisualisation;
