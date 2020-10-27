import React, {useState, useEffect, useContext} from "react";
import Graph from "react-graph-vis";
import {AppContext} from "../../Store/AppContextProvider.js";


const SubsetVisualisation = (props) => {
        const {state, dispatch} = useContext(AppContext);
        const {subsetGraphData} = props;

        const events = {
        };

        return (
            <Graph
                graph={subsetGraphData}
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
                    // if you want access to vis.js network api you can set the state in a parent component using this property
                    network.on("hoverNode", function (params) {
                        network.canvas.body.container.style.cursor = 'pointer'
                    });
                }}
            />
        );
    }
;

export default SubsetVisualisation;
