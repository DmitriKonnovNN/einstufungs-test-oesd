import React from 'react';

import './App.css';
import {useTaskset} from "./hooks/useTaskset";

function App() {
  const {taskset, loading, error} = useTaskset()
  console.log(taskset);
  console.log(error)
  console.log(loading)
  return (
    <div className="App">
    </div>
  );
}

export default App;
