var wsUri2 = "ws://" + document.location.host + document.location.pathname + "chatSocket";

var chatSocket = new WebSocket(wsUri2);

chatSocket.onmessage = function(evt) { onChatMessage(evt) };

function onChatMessage(evt){
	alert("not that dumb");
	var json = JSON.parse(evt.data);
	$('#chatBox').append("<p class='username'>"+json.username+"</p><p class='text'>"+":"+json.message+"</p><br/>");
	$('#chatBox').scrollTop($('#chatBox')[0].scrollHeight);
}


function clickOnSend(){
	alert("here");
	chatSocket.send($('#chatText').val());
    $('#chatText').val("")
}