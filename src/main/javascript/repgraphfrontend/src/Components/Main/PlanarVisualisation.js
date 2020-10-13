import React, {useState, useEffect, useContext} from "react";
import Graph from "react-graph-vis";
import {AppContext} from "../../Store/AppContextProvider.js";

const PlanarVisualisation = (props) => {
        const {state, dispatch} = useContext(AppContext);
        const {planarGraphData} = props;

        const events = {
        };

        return (
            <Graph
                graph={planarGraphData}
                options={state.visualisationOptions}
                events={events}
                style={{width: "100%", height: "50vh"}}
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

export default PlanarVisualisation;
