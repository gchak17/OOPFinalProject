//var wsUri = "ws://" + document.location.host + document.location.pathname + "websocket";

var websocket = new WebSocket("ws://localhost:8080/OOPFinalProject/client.jsp/web");
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
	} else if (json.command === "showResults" || json.command === "showplayers") {
		showPlayerResults(json);
	} else if (json.command === "clear") {
		context.clearRect(0, 0, canvas.width, canvas.height);
	} 
//	else if (json.command === "showplayers"){
//		showPlayers(json);
//	}
}

var canvas = document.getElementById("canvas-panel");
var context = canvas.getContext("2d");
var offsetLeft, offsetTop;

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
	return [x, y];
}

function changeSizeAndPosition(){
    //canvas-panel
    document.getElementById("canvas-panel").width = innerWidth * 0.6;
    document.getElementById("canvas-panel").height = innerHeight * 0.6;
    document.getElementById("canvas-panel").style.top = innerHeight * 0.2 + "px";
    document.getElementById("canvas-panel").style.left = innerWidth * 0.2 + "px";

    //center-panel class
    var x = document.getElementsByClassName("center-panel");
    for (var i = 0; i < x.length; i++) {
        x[i].style.width = innerWidth * 0.6 + "px";
        x[i].style.height = innerHeight * 0.2 + "px";
        x[i].style.left = innerWidth * 0.2 + "px";
    }

    //word-panel
    document.getElementById("word-panel").style.top = "0px";

    //paint-options-panel
    document.getElementById("paint-options-panel").style.top = innerHeight * 0.8 + "px";

    //side-panel class
    var x = document.getElementsByClassName("side-panel");
    for (var i = 0; i < x.length; i++) {
        x[i].style.width = innerWidth * 0.2 + "px";
        x[i].style.height = innerHeight + "px";
        x[i].style.top = "0px";
    }

    //users-panel
    document.getElementById("users-panel").style.left = "0px";

    //chat-panel
    document.getElementById("chat-panel").style.left = innerWidth * 0.8 + "px";

    //chat-box class
    var x = document.getElementsByClassName("chat-box-class");
    for (var i = 0; i < x.length; i++) {
        x[i].style.width = innerWidth * 0.18 + "px";
        x[i].style.height = innerHeight * 0.87 + "px";
    }

	offsetLeft = canvas.offsetLeft;
	offsetTop = canvas.offsetTop;
}

function showPlayerResults(json){
	var userPanel = document.getElementById("users-panel");
	
	while(userPanel.hasChildNodes()){   
		userPanel.removeChild(list.firstChild);
	}
	
	for(var k in json){
		if(k === "command") continue;
		var v = json[k];
		var newP = document.createElement("P");
		var t = document.createTextNode(k + " : " + v);
		newP.appendChild(t);
		userPanel.appendChild(newP);
	}
}