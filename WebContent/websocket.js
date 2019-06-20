var wsUri = "ws://" + document.location.host + document.location.pathname + "websocket";
var websocket = new WebSocket(wsUri);
websocket.onmessage = function(evt) { onMessage(evt) };

function sendText(json) {
    websocket.send(json);
}

function onMessage(evt) {
    drawImageText(evt.data);
}