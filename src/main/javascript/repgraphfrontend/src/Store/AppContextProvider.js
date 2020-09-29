import React, { useReducer } from "react";
import { dmrsData } from "../store.js";

export const AppContext = React.createContext();

const initialState = {
  dataSet: dmrsData,
  selectedSentence: null,
  selectedSentenceIDs: null,
  isLoading: false
};

const reducer = (state, action) => {
  switch (action.type) {
    case "SET_DATASET":
      return { ...state, dataSet: action.payload.dataSet };
    case "SET_SENTENCE":
      return { ...state, selectedSentence: action.payload.selectedSentence };
    case "SET_LOADING":
      return { ...state, isLoading: action.payload.isLoading };
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
/*
export class AppProvider extends Component {
  componentDidMount = () => {
    const initState = {
      dataSet: {},
      selectedSentence: {},
      isLoading: false,
      dispatch: (action) => {
        this.setState((state) => reducer(state, action));
      }
    };
    this.setState(initState);
  };

  state = {};

  render() {
    return (
      <AppContext.Provider value={this.state}>
        {this.props.children}
      </AppContext.Provider>
    );
  }
}*/
