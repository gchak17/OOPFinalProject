//var wsUri = "ws://" + document.location.host + document.location.pathname + "websocket";

//var gameSocket = new WebSocket("ws://192.168.98.14:8888/OOPFinalProject/client/game");
var gameSocket = new WebSocket("ws://localhost:8888/OOPFinalProject/client/game");
gameSocket.onmessage = function(evt) {
	onMessage(evt)
};

function sendText(json) {
	gameSocket.send(json);
}

var timerVar, seconds;
var modal = document.getElementById("myModal");

function onMessage(evt) {
	var json = JSON.parse(evt.data);
	console.log(json.command);
	if (json.command === "paint") {
		drawImageText(evt.data);
	} else if (json.command === "checkStatus") {
		isArtist = json.answer;
	} else if (json.command === "showResults") {
		document.getElementById("word-place").innerHTML = "";
		showPlayerResults(json);
	} else if (json.command === "clear") {
		context.clearRect(0, 0, canvas.width, canvas.height);
	} else if (json.command === "newturn") {
		seconds = json.seconds + 1;
		// timerVar = setInterval(timerFun, 2000);

		delete json.seconds;
		showPlayerResults(json);
	} else if (json.command === "appearplayers") {
		delete json.command;
		appearPlayers(json);
	} else if (json.command === "startgametimer") {
		seconds = 5 + 1;
		// timerVar = setInterval(timerFun, 1000);
	} else if (json.command === "appearartist") {
		appearArtist(json);
	} else if (json.command === "endgame") {
		//location.replace("192.168.98.14:8888/OOPFinalProject/Main.jsp");
		location.replace("http://localhost:8888/OOPFinalProject/Main.jsp");
	} else if (json.command === "chooseWord") {
		document.getElementById("word-button1").value = json.one;
		document.getElementById("word-button2").value = json.two;
		document.getElementById("word-button3").value = json.three;

		modal.style.display = "block";
		chooseTheWord(json);
	} else if (json.command === "startturn") {
		isArtist = true;
		addCanvasListeners();
	} else if (json.command === "endturn") {
		isArtist = false;
	} else if (json.command === "don't choose") {
		document.getElementById("guesserModal").style.display = "block";
	} else if (json.command === "wordIsChosen") {
		document.getElementById("guesserModal").style.display = "none";
		var word = json.chosen;
		document.getElementById("word-place").innerHTML = word;
	} else if (json.command === "autochooseword") {
		chooseTheWordAutomatically(json);
	} else if (json.command === "finalresults") {
		finalResultsPopUp(json);
	} else if (json.command === "revealword") {
		revealWord(json);
	}
}

function revealWord(json) {
	delete json.command;
	
	var div = document.getElementById("guesserModal");
	var h2s = div.getElementsByTagName("h2");
	h2s[0].innerHTML = "word was " + json["word"];
	
	div.style.display = "block"
		
	setTimeout(function(){
		div.style.display = "none";
		h2s[0].innerHTML = "The Artist Is Choosing The Word";
	}, 2 * 1000);
}

function finalResultsPopUp(json) {
	delete json.command;
	
	var div = document.getElementById("guesserModal");
	var h2s = div.getElementsByTagName("h2");
	h2s[0].innerHTML = "game is over";
	
	for (var k in json) {
		var v = json[k];
		h2s[0].innerHTML += "<br/>" + k + " " + v;
	}
	
	div.style.display = "block"
}

function chooseTheWordAutomatically(json) {
	var arr = [ "one", "two", "three" ];
	var el = arr[Math.floor(Math.random() * arr.length)];
	setTheWordAndSend(json[el], json);
	document.getElementById("word-place").innerHTML = json[el];
}

function chooseTheWord(json) {
	document.getElementById("word-button1").onclick = function() {
		setTheWordAndSend(json.one, json);
		document.getElementById("word-place").innerHTML = json.one;
	}
	document.getElementById("word-button2").onclick = function() {
		setTheWordAndSend(json.two, json);
		document.getElementById("word-place").innerHTML = json.two;
	}
	document.getElementById("word-button3").onclick = function() {
		setTheWordAndSend(json.three, json);
		document.getElementById("word-place").innerHTML = json.three;
	}
}

function setTheWordAndSend(word, json) {
	var chosenWord = word;
	modal.style.display = "none";

	var newJson = JSON.stringify({
		"command" : "wordIsChosen",
		"chosen" : chosenWord
	});
	console.log(chosenWord);
	gameSocket.send(newJson)
}

var canvas = document.getElementById("canvas-panel");
var context = canvas.getContext("2d");
var offsetLeft, offsetTop;

var drawing = false;
var isArtist = false;
var lastPos = null;

var currCol = "black";
var currWidth = 4;

var mouseUpFun = function() {
	drawing = false;
}

var mouseDownFun = function() {
	checkIfIsArtist();
	drawing = isArtist;
	lastPos = getPos(event);
}

var mouseMoveFun = function() {
	if (!drawing)
		return;

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

	if (!isArtist) {
		removeCanvasListeners();
		return;
	}

	drawImageText(json);
	// string aris es json
	sendText(json);

	lastPos = p;
}

function addCanvasListeners() {
	document.addEventListener('mouseup', mouseUpFun);
	canvas.addEventListener('mousedown', mouseDownFun);
	canvas.addEventListener('mousemove', mouseMoveFun);
}

function removeCanvasListeners() {
	document.addEventListener('mouseup', mouseUpFun);
	canvas.addEventListener('mousedown', mouseDownFun);
	canvas.addEventListener('mousemove', mouseMoveFun);
}

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
	if (isArtist) {
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

function getPos(event) {
	var x = event.clientX - offsetLeft;
	var y = event.clientY - offsetTop;
	return [ x, y ];
}

function changeSizeAndPosition() {
	// canvas-panel
	document.getElementById("canvas-panel").width = innerWidth * 0.6;
	document.getElementById("canvas-panel").height = innerHeight * 0.6;
	document.getElementById("canvas-panel").style.top = innerHeight * 0.2
			+ "px";
	document.getElementById("canvas-panel").style.left = innerWidth * 0.2
			+ "px";

	// center-panel class
	var x = document.getElementsByClassName("center-panel");
	for (var i = 0; i < x.length; i++) {
		x[i].style.width = innerWidth * 0.6 + "px";
		x[i].style.height = innerHeight * 0.2 + "px";
		x[i].style.left = innerWidth * 0.2 + "px";
	}

	// word-panel
	document.getElementById("word-panel").style.top = "0px";

	// paint-options-panel
	document.getElementById("paint-options-panel").style.top = innerHeight
			* 0.8 + "px";

	// side-panel class
	var x = document.getElementsByClassName("side-panel");
	for (var i = 0; i < x.length; i++) {
		x[i].style.width = innerWidth * 0.2 + "px";
		x[i].style.height = innerHeight + "px";
		x[i].style.top = "0px";
	}

	// users-panel
	document.getElementById("users-panel").style.left = "0px";

	// chat-panel
	document.getElementById("chat-panel").style.left = innerWidth * 0.8 + "px";

	// chat-box class
	var x = document.getElementsByClassName("chat-box-class");
	for (var i = 0; i < x.length; i++) {
		x[i].style.width = innerWidth * 0.18 + "px";
		x[i].style.height = innerHeight * 0.87 + "px";
	}

	offsetLeft = canvas.offsetLeft;
	offsetTop = canvas.offsetTop;
}

function showPlayerResults(json) {
	delete json.command;

	var userPanel = document.getElementById("users-panel");

	while (userPanel.hasChildNodes()) {
		userPanel.removeChild(userPanel.firstChild);
	}

	var newArtist = json.artist;
	delete json.artist;
	for ( var k in json) {
		var v = json[k];
		var newP = document.createElement("P");
		var t = document.createTextNode(k + " : " + v);
		newP.appendChild(t);
		if (k === newArtist)
			newP.style.color = "yellow";
		userPanel.appendChild(newP);
	}
}

function appearPlayers(json) {
	var userPanel = document.getElementById("users-panel");

	for (var k in json) {
		var v = json[k];
		var newP = document.createElement("P");
		var t = document.createTextNode(k + " : " + v);
		newP.appendChild(t);
		userPanel.appendChild(newP);
	}
}

// function timerFun() {
// seconds--;
// //console.log(seconds);
// document.getElementById("timer-div").innerHTML = seconds;
// if(seconds <= 0){
// console.log("checks");
// clearInterval(timerVar);
// }
// }
