//create websocket
var socket = new WebSocket("ws://localhost/events/");
var socket = new WebSocket("ws://localhost/anderedingen/");

socket.onopen = () => {
    console.log("Websocket is now open!");
};

socket.onmessage = message =>{
    const response = JSON.parse(message.data);

    //connect
    if(response.method === "connect"){
        userId = response.userId;
        console.log("clientID is successfully set: " + userId);
    }
};

var userId = null;

export default socket; 
export {userId};



