var chatSocket = new WebSocket(
		"ws://localhost:8081/OOPFinalProject/EnterChatServlet");

chatSocket.onmessage = function(evt) {
	onChatMessage(evt)
};

function onChatMessage(evt) {
	alert("kill me");
	var json = JSON.parse(evt.data);
	alert(json.username + ": " + json.message);
	/*$("#chatBox").append(
			"<p class='username'>" + json.username + "</p><p class='text'>"
					+ ":" + json.message + "</p><br/>");
	$("#chatBox").scrollTop($('#chatBox')[0].scrollHeight);*/
}

function clickOnSend() {
	chatSocket.send(document.getElementById("chatText").value);
	//$('#chatText').value("")
}