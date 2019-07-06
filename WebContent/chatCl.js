var chatSocket = new WebSocket(
		"ws://localhost:8081/OOPFinalProject/EnterChatServlet");

chatSocket.onmessage = function(evt) {
	onChatMessage(evt)
};

function onChatMessage(evt) {
	alert("kill me");
	var json = JSON.parse(evt.data);
	alert(json.username + ": " + json.message);
	alert(document.getElementById("chatBox"));
	document.getElementById("chatBox").append("<p>" + json.username + ": "+json.message+"</p>");
	//$('#chatBox').scrollTop($('#chatBox')[0].scrollHeight);
		//	"<p class='username'>" + json.username + "</p><p class='text'>"
		//			+ ":" + json.message + "</p><br/>");
	alert("holy forking shirtballs");
	alert("holy forking shirtballs");
	alert("holy forking shirtballs");
}

function clickOnSend() {
	alert("here");
	chatSocket.send(document.getElementById("chatText").value);
}