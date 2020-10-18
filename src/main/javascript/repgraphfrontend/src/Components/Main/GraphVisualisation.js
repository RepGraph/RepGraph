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
        options={{
            ...state.visualisationOptions,
            edges: {...state.visualisationOptions.edges, color: state.darkMode ? "#ffffff" : "#1d1d1d"}
        }} //Options from global state
        events={events}
        style={{width: "100%", height: "100%"}}
        getNetwork={(network) => {
            //  if you want access to vis.js network api you can set the state in a parent component using this property

            //hacky fix - calls when physics of edges stabilises
            network.on("startStabilizing", function (params) {
                network.fit();
            });

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
