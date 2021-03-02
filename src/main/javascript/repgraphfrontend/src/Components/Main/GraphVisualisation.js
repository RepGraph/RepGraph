import React, {useState, useEffect, useContext} from "react";
import Graph from "react-graph-vis";
import {AppContext} from "../../Store/AppContextProvider.js";


const GraphVisualisation = () => {
    const {state, dispatch} = useContext(AppContext);

    const events = {
        select: function (event) {
            let {nodes, edges} = event;

            dispatch({type: "SET_SELECT_NODE_EDGE", payload: {selectedNodeAndEdges: {nodes, edges}}});
        }
    };

return (
    <Graph
        graph={state.selectedSentenceVisualisation}
        options={{
            ...state.visualisationOptions,
            edges: {
                ...state.visualisationOptions.edges,
                color: state.darkMode ? state.visualisationOptions.darkMode.edgeColor : state.visualisationOptions.edges.color,
            }
        }} //Options from global state
        events={events}
        style={{width: "100%", height: "100%"}}
        getNetwork={(network) => {

            //double click to center graph
            network.on("doubleClick", function (params) {
                network.fit();
            });

            network.on("hoverNode", function (params) {
                network.canvas.body.container.style.cursor = 'pointer'
            });

        }}
    />
);
}
;

export default GraphVisualisation;
