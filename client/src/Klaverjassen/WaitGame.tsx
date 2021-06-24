import React, { useState } from "react";
import type { GameState } from "../gameState";
import { Klaverjassen } from "./Klaverjassen";
import socket, {userId} from "../SocketProvider";
import "./WaitGame.css";


/**
 * WaitGame function
 */
export function WaitGame( ) {

    return (
        <p>Waiting for other players...</p>
    )
}