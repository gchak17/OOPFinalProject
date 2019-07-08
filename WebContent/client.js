//var wsUri = "ws://" + document.location.host + document.location.pathname + "websocket";

var websocket = new WebSocket(
		"ws://localhost:8080/OOPFinalProject/client.jsp/web");
websocket.onmessage = function(evt) {
	onMessage(evt)
};

function sendText(json) {
	websocket.send(json);
}

function onMessage(evt) {
	var json = JSON.parse(evt.data);

	if (json.command === "paint") {
		drawImageText(evt.data);
	} else if (json.command === "checkStatus") {
		isArtist = json.answer;
	} else if (json.command === "showResults") {

	} else if (json.command === "clear") {
		context.clearRect(0, 0, canvas.width, canvas.height);
	}
}

var canvas = document.getElementById("canvas");
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
		"command" : "paint",
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
		"command" : "checkStatus"
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
	checkIfIsArtist();
	if(isArtist){
		var json = JSON.stringify({
			"command" : "clear"
		});
		sendText(json);
	}
}

function drawImageText(image) {
	var json = JSON.parse(image);

	context.beginPath();
	context.lineWidth = json.width;
	context.strokeStyle = json.color;
	context.lineCap = "round";
	context.moveTo(json.start.x, json.start.y);
	context.lineTo(json.end.x, json.end.y);
	context.stroke();
	
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