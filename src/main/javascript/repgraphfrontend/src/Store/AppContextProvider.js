import React, {useReducer} from "react";

export const AppContext = React.createContext();

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
    dataSet: null,
    dataSetFileName: null,
    dataSetResponse: null,
    parserResponse: null,
    selectedSentenceVisualisation: null,
    selectedSentenceID: null,
    selectedNodeAndEdges: null,
    isLoading: false,
    APIendpoint: "http://localhost:8080",
    testResults: null,
    longestPathVisualisation: null,
    visualisationFormat: "1",
    visualisationOptions: visualisationOptions,
    darkMode: false
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