//var wsUri = "ws://" + document.location.host + document.location.pathname + "websocket";
var chatSocket = new WebSocket(
		"ws://localhost:8081/OOPFinalProject/client.html");

var websocket = new WebSocket("ws://localhost:8081/OOPFinalProject/client.html");
websocket.onmessage = function(evt) {
	onMessage(evt)
};

chatSocket.onmessage = function(evt){
	onChatMessage(evt)
};

function onChatMessage(evt) {
	alert("got here");
	var json = JSON.parse(evt.data);
	var node = document.createElement("P");

	var textnode = document.createTextNode(json.username + ": " + json.message);
	node.appendChild(textnode);

	document.getElementById("chatBox").appendChild(node);
	document.getElementById("chatText").value = "";
}

function clickOnSend() {
	alert("got here");
	chatSocket.send(document.getElementById("chatText").value);
}

function sendText(json) {
	websocket.send(json);
}

function onMessage(evt) {
	var json = JSON.parse(evt.data);
	if (json.type === "isArtist?") {
		if (json.answer)
			isArtist = true;
	}
	drawImageText(evt.data);
}

var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");
var offsetLeft = canvas.offsetLeft;
var offsetTop = canvas.offsetTop;

var drawing = false;
var isArtist = false;
var lastPos = null;

var currCol = "black";
var currWidth = 4;

listen(canvas, 'mousedown', function(event) {
	checkIfIsArtist();

	drawing = isArtist;
	lastPos = getPos(event);
});

listen(canvas, 'mousemove', function(event) {
	if (!drawing) {
		return;
	}

	var p = getPos(event);

	var json = JSON.stringify({
		"type" : "drawing",
		"clear" : false,
		"start" : {
			"x" : lastPos[0],
			"y" : lastPos[1]
		},
		"end" : {
			"x" : p[0],
			"y" : p[1]
		},
		"color" : currCol,
		"width" : currWidth
	});

	drawImageText(json);
	// string aris es json
	sendText(json);

	lastPos = p;
});

function checkIfIsArtist() {
	var json = JSON.stringify({
		"type" : "isArtist?",
	});
	sendText(json);
}

function color(obj) {
	currCol = obj.id;
}

function width(obj) {
	currWidth = parseInt((obj.id).substring(1));
}

function clearCanvas() {
	var json = JSON.stringify({
		"type" : "shota",
		"clear" : true
	});

	drawImageText(json);
	sendText(json);
}

function drawImageText(image) {
	var json = JSON.parse(image);
	if (json.clear) {
		context.clearRect(0, 0, canvas.width, canvas.height);
	} else {
		context.beginPath();
		context.lineWidth = json.width;
		context.strokeStyle = json.color;
		context.lineCap = "round";
		context.moveTo(json.start.x, json.start.y);
		context.lineTo(json.end.x, json.end.y);
		context.stroke();
	}
}

listen(document, 'mouseup', function(event) {
	drawing = false;
});

function listen(elem, type, listener) {
	elem.addEventListener(type, listener, false);
}

function getPos(event) {
	var x = event.clientX - offsetLeft;
	var y = event.clientY - offsetTop;
	return [ x, y ];
}