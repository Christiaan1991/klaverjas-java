//create websocket
var socket = new WebSocket("ws://localhost:8080/events/");
let userId = null;

var timerID = 0; 
function keepAlive() { 
    var timeout = 20000;  
    if (socket.readyState == socket.OPEN) {  
        //socket.send('');  
    }  
    var timerID = setTimeout(keepAlive, timeout);  
}  

function cancelKeepAlive() {  
    if (timerID) {  
        clearTimeout(timerID);  
    }  
}

socket.onopen = () => {
    console.log("Websocket is now open!");

    //this is to keep the socket keepAlive
    keepAlive();
};

socket.onmessage = message =>{
    const response = JSON.parse(message.data);

    //connect
    if(response.method === "connect"){
        userId = response.userId;
        console.log("clientID is successfully set: " + userId);
    }
};

export function isOpen() { return socket.readyState === socket.OPEN}
export default socket; 



