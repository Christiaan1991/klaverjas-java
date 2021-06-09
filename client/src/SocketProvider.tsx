//create websocket
var socket = new WebSocket("ws://192.168.2.11:8080/events/");
var userId = null;

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

export default socket; 
export {userId};



