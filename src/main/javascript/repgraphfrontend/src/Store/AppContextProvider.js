import React, {useReducer} from "react";
import {dmrsData} from "../store";
export const AppContext = React.createContext();

export const defaultGraphStyles = {
    backgroundColour: "#efefef",
    hierarchicalStyles: {
        nodeStyles: {
            nodeColour: "rgba(0,172,237,1)",
            hoverColour: "rgba(82, 208, 255,1)",
            spanColour: "rgba(0,0,0,0.3)",
            selectedColour: "rgba(61, 230, 140, 1)",
            labelColour: "rgba(255,255,255,1)"
        },
        linkStyles: {
            linkColour: "rgba(120,120,120,1)",
            hoverColour: "rgba(0,0,0,1)",
            selectedColour: "rgba(61, 230, 140, 1)"
        },
        tokenStyles: {
            tokenColour: "rgba(255, 220, 106,1)",
            hoverColour: "rgba(255, 232, 156,1)",
            selectedColour: "rgba(61, 230, 140, 1)",
            labelColour: "rgba(0,0,0,1)"
        }
    },
    treeStyles: {
        nodeStyles: {
            nodeColour: "rgba(0,172,237,1)",
            hoverColour: "rgba(82, 208, 255,1)",
            spanColour: "rgba(0,0,0,0.3)",
            selectedColour: "rgba(61, 230, 140, 1)"
        },
        linkStyles: {
            linkColour: "rgba(120,120,120,1)",
            hoverColour: "rgba(0,0,0,1)",
            selectedColour: "rgba(61, 230, 140, 1)"
        },
        tokenStyles: {
            tokenColour: "rgba(255, 220, 106,1)",
            hoverColour: "rgba(255, 232, 156,1)",
            selectedColour: "rgba(61, 230, 140, 1)"
        }
    },
    flatStyles: {
        nodeStyles: {
            nodeColour: "rgba(0,172,237,1)",
            hoverColour: "rgba(82, 208, 255,1)",
            spanColour: "rgba(0,0,0,0.3)",
            selectedColour: "rgba(61, 230, 140, 1)"
        },
        linkStyles: {
            linkColour: "rgba(120,120,120,1)",
            hoverColour: "rgba(0,0,0,1)",
            selectedColour: "rgba(61, 230, 140, 1)"
        },
        tokenStyles: {
            tokenColour: "rgba(255, 220, 106,1)",
            hoverColour: "rgba(255, 232, 156,1)",
            selectedColour: "rgba(61, 230, 140, 1)"
        }
    },
    longestPathStyles: {
        linkColour: "rgba(225, 9, 9, 1)",
        nodeColour: "rgba(225, 9, 9, 1)",
        hoverColour: "rgba(248, 84, 84, 1)"
    },
    compareStyles: {
        linkColourDissimilar: "rgba(225, 9, 9, 1)",
        linkColourSimilar: "rgba(67, 220, 24, 1)",
        nodeColourDissimilar: "rgba(225, 9, 9, 1)",
        nodeColourSimilar: "rgba(67, 220, 24, 1)",
        hoverNodeColourSimilar: "rgba(125, 237, 94, 1)",
        hoverNodeColourDissimilar: "rgba(248, 84, 84, 1)",
        hoverLinkColourSimilar: "rgba(125, 237, 94, 1)",
        hoverLinkColourDissimilar: "rgba(248, 84, 84, 1)"
    },
    planarStyles: {
        linkColourCross: "rgba(225, 9, 9, 1)",
        hoverColour: "rgba(248, 84, 84, 1)"
        //still to do
    }
};

export const defaultGraphLayoutSpacing = {
    nodeHeight: 40,
    nodeWidth: 80,
    interLevelSpacing: 80,
    intraLevelSpacing: 50,
    tokenLevelSpacing: 140
};

const visualisationOptions = {
    physics: {
        enabled: true,
        forceAtlas2Based: {
            gravitationalConstant: -50000,
            centralGravity: 0.0,
            springConstant: 0.08,
            springLength: 100,
            damping: 0,
            avoidOverlap: 1
        }
    },
    autoResize: true,
    edges: {
        color: "#959595",
        smooth: true,
        physics: true,
        arrows: {
            to: {
                enabled: true,
                scaleFactor: 1.3
            }
        },
        arrowStrikethrough: false,
        width: 1.4
    },
    nodes: {
        shape: "box",
        color: "rgba(97,195,238,0.5)",
        font: {size: 14, strokeWidth: 4, strokeColor: "white"},
        widthConstraint: {
            minimum: 60,
            maximum: 60
        },
        heightConstraint: {
            minimum: 30
        }
    },
    height: "100%",
    width: "100%",
    interaction: {hover: true},
    groups: {
        node: {
            shape: "box",
            color: "rgba(0,172,237,0.71)",
            font: {size: 14, strokeWidth: 4, strokeColor: "white"},
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        },
        surfaceNode: {
            shape: "box",
            color: "rgba(0,237,107,0.76)",
            font: {size: 14, strokeWidth: 4, strokeColor: "white"},
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        },
        token: {
            shape: "box",
            color: "rgba(255,87,34,0.85)",
            font: {size: 14, strokeWidth: 4, strokeColor: "white"},
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        },
        tokenEdge: {
            color: "rgba(156, 154, 154, 1)",
            smooth: true,
            physics: true,
            dashed: true,
            arrows: {
                to: {
                    enabled: false
                }
            },
            arrowStrikethrough: false
        },
        longestPath: {
            shape: "box",
            color: "rgba(245, 0, 87, 0.7)",
            font: {size: 14, strokeWidth: 4, strokeColor: "white"},
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        },
        similarNode: {
            shape: "box",
            color: "rgba(0,153,0,0.7)",
            font: {size: 14, strokeWidth: 4, strokeColor: "white"},
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        },
        top: {
            shape: "box",
            color: "rgba(255,243,33,0.7)",
            font: {size: 14, strokeWidth: 4, strokeColor: "white"},
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        },
        differentNode: {
            shape: "box",
            color: "rgba(245, 0, 87, 0.7)",
            font: {size: 14, strokeWidth: 4, strokeColor: "white"},
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        },
        Selected: {
            shape: "box",
            color: "rgba(255, 0, 0, 0.65)",
            font: {size: 14, strokeWidth: 4, strokeColor: "white"},
            widthConstraint: {
                minimum: 60,
                maximum: 60
            },
            heightConstraint: {
                minimum: 30
            }
        }
    },
    darkMode: {
        edgeColor: "#FFFFFF"
    }

};


const initialState = {
    dataSet: dmrsData,
    dataSetFileName: "dmrsData",
    dataSetResponse: null,
    parserResponse: null,
    selectedSentenceID: null,
    selectedSentenceGraphData: null,
    selectedSentenceVisualisation : null,
    selectedNodeAndEdges: null,
    isLoading: false,
    APIendpoint: "http://localhost:8080",
    testResults: null,
    longestPathVisualisation: null,
    visualisationFormat: "1",
    visualisationOptions: visualisationOptions,
    darkMode: false,
    framework : "1",
    userID: 1,
    graphStyles: defaultGraphStyles,
    graphLayoutSpacing: defaultGraphLayoutSpacing
};

const reducer = (state, action) => {
    switch (action.type) {
        case "SET_DATASET":
            return {...state, dataSet: action.payload.dataSet};
        case "SET_DATASET_FILENAME":
            return {...state, dataSetFileName: action.payload.dataSetFileName};
        case "SET_DATASET_RESPONSE":
            return {...state, dataSetResponse: action.payload.dataSetResponse};
        case "SET_PARSER_RESPONSE":
            return {...state, parserResponse: action.payload.parserResponse};
        case "SET_SENTENCE_GRAPHDATA":
            return {...state, selectedSentenceGraphData: action.payload.selectedSentenceGraphData};
        case "SET_SENTENCE_VISUALISATION":
            return {...state, selectedSentenceVisualisation: action.payload.selectedSentenceVisualisation};
        case "SET_LONGEST_VISUALISATION":
            return {...state, longestPathVisualisation: action.payload.longestPathVisualisation};
        case "SET_SELECTED_SENTENCE_ID":
            return {...state, selectedSentenceID: action.payload.selectedSentenceID};
        case "SET_LOADING":
            return {...state, isLoading: action.payload.isLoading};
        case "SET_SELECT_NODE_EDGE":
            return {...state, selectedNodeAndEdges: action.payload.selectedNodeAndEdges};
        case "SET_TEST_RESULTS":
            return {...state, testResults: action.payload.testResults};
        case "SET_VISUALISATION_FORMAT":
            return {...state, visualisationFormat: action.payload.visualisationFormat};
        case "SET_DARK_MODE":
            return {...state, darkMode: action.payload.darkMode};
        case "SET_VISUALISATION_OPTIONS":
            return {...state, visualisationOptions: action.payload.visualisationOptions};
        case "SET_FRAMEWORK":
            return {...state, framework: action.payload.framework};
        case "SET_USER_ID":
            return {...state, userID: action.payload.userID};
        case "SET_GRAPH_STYLES":
            return {...state, graphStyles: action.payload.graphStyles};
        case "SET_GRAPH_LAYOUT_SPACING":
            return {...state, graphLayoutSpacing: action.payload.graphLayoutSpacing};
        default:
            break;
    }
};

export default function AppContextProvider({children}) {
    const [state, dispatch] = useReducer(reducer, initialState);

    return (
        <AppContext.Provider value={{state, dispatch}}>
            {children}
        </AppContext.Provider>
    );
}