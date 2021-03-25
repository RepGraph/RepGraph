import React, {useState, useEffect, useContext} from "react";
import Graph from "react-graph-vis";
import {AppContext} from "../../Store/AppContextProvider.js";



const SubgraphVisualisation = (props) => {
        const {state, dispatch} = useContext(AppContext);
        const {subgraphGraphData} = props;

        const events = {
        };

        return (
            <Graph
                graph={subgraphGraphData}
                options={{
                    ...state.visualisationOptions,
                    edges: {
                        ...state.visualisationOptions.edges,
                        color: state.visualisationOptions.edges.color,
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

export default SubgraphVisualisation;
