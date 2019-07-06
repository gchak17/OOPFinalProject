var chatSocket = new WebSocket(
		"ws://localhost:8081/OOPFinalProject/EnterChatServlet");

chatSocket.onmessage = function(evt) {
	onChatMessage(evt)
};

function onChatMessage(evt) {
	var json = JSON.parse(evt.data);
	document.getElementById("chatBox").append("text\n");
	document.getElementById("chatText").value = "";
}

function clickOnSend() {
	chatSocket.send(document.getElementById("chatText").value);
}