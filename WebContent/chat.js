var chatSocket = new WebSocket("ws://localhost:8888/OOPFinalProject/client/chat/" + game_id);

chatSocket.onmessage = function(evt){
	onChatMessage(evt)
};



function onChatMessage(evt) {
	var json = JSON.parse(evt.data);
	var node = document.createElement("P");

	var textnode = document.createTextNode(json.username + ": " + json.message);
	node.appendChild(textnode);

	document.getElementById("chat-box-div").appendChild(node);
	document.getElementById("chat-text").value = "";
	document.getElementById("chat-box-div").scrollTop = document.getElementById("chat-box-div").scrollHeight
}

function clickOnSend() {
	var json = JSON.stringify({
		"command" : "receiveMessage",
		"message" : document.getElementById("chat-text").value
	});
	//console.log("got here");
	chatSocket.send(json);
}