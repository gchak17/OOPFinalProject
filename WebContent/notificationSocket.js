//var notificationSocket = new WebSocket("ws://192.168.98.14:8888/OOPFinalProject/notifications");
//var notificationSocket = new WebSocket("ws://localhost:8888/OOPFinalProject/notifications");
var notificationSocket = new WebSocket('ws://' + window.location.host + '/OOPFinalProject/notifications');
		
notificationSocket.onmessage = function(evt){
	var notification = JSON.parse(evt.data);
	var message = notification.message;
	alert(message);
};

function sendNotificationMessage(message){
	console.log(message);
	notificationSocket.send(JSON.stringify(message));
}