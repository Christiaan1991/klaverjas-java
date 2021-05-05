import React, { Component } from "react";
import ReactDOM from "react-dom";
import { App } from "./App";


ReactDOM.render(
    <App/>,
    document.getElementById("root")
)

// Hot Module Replacement (HMR) - Remove this snippet to remove HMR.
// Learn more: https://www.snowpack.dev/#hot-module-replacement
// This is to immediately implement changes in the server
const hotMudleReplacement = import.meta.hot;
if (hotMudleReplacement) {
    hotMudleReplacement.accept();
}





