var chatSocket = new WebSocket(
		"ws://localhost:8081/OOPFinalProject/EnterChatServlet");

chatSocket.onmessage = function(evt) {
	onChatMessage(evt)
};
chatSocket.onopen = function(evt) {
};
chatSocket.onclose = function(evt) {
};
function onChatMessage(evt) {
	alert("kill me");
	var json = JSON.parse(evt.data);
	$('#chatBox').append(
			"<p class='username'>" + json.username + "</p><p class='text'>"
					+ ":" + json.message + "</p><br/>");
	$('#chatBox').scrollTop($('#chatBox')[0].scrollHeight);
}

function clickOnSend() {
	alert("sth");
	chatSocket.send($('#chatText').val());
	$('#chatText').val("")
}