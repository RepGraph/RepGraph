import React, { useReducer } from "react";
import { dmrsData } from "../store.js";

export const AppContext = React.createContext();

const initialState = {
  dataSet: null,
  dataSetFileName: null,
  dataSetResponse: null,
  selectedSentenceVisualisation: null,
  selectedSentenceID: null,
  selectedNodeAndEdges: null,
  isLoading: false,
  APIendpoint: "http://repgraph-api.herokuapp.com",
  testResults: null,
  longestPathVisualisation: null,
  visualisationFormat: "1",
};

const reducer = (state, action) => {
  switch (action.type) {
    case "SET_DATASET":
      return { ...state, dataSet: action.payload.dataSet };
    case "SET_DATASET_FILENAME":
      return { ...state, dataSetFileName: action.payload.dataSetFileName };
    case "SET_DATASET_RESPONSE":
      return { ...state, dataSetResponse: action.payload.dataSetResponse };
    case "SET_SENTENCE_VISUALISATION":
      return { ...state, selectedSentenceVisualisation: action.payload.selectedSentenceVisualisation };
    case "SET_LONGEST_VISUALISATION":
      return { ...state, longestPathVisualisation: action.payload.longestPathVisualisation };
    case "SET_SELECTED_SENTENCE_ID":
      return { ...state, selectedSentenceID: action.payload.selectedSentenceID };
    case "SET_LOADING":
      return { ...state, isLoading: action.payload.isLoading };
    case "SET_SELECT_NODE_EDGE":
      return { ...state, selectedNodeAndEdges: action.payload.selectedNodeAndEdges };
    case "SET_TEST_RESULTS":
      return { ...state, testResults: action.payload.testResults };
    case "SET_VISUALISATION_FORMAT":
      return { ...state, visualisationFormat: action.payload.visualisationFormat };
    default:
      break;
  }
};

export default function AppContextProvider({ children }) {
  const [state, dispatch] = useReducer(reducer, initialState);

  return (
    <AppContext.Provider value={{ state, dispatch }}>
      {children}
    </AppContext.Provider>
  );
}