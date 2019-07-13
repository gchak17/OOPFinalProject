var notificationSocket = new WebSocket("ws://192.168.98.14:8888/OOPFinalProject/notifications");
		
notificationSocket.onmessage = function(evt){
	var notification = JSON.parse(evt.data);
	var senderID = notification.sender;
	alert('account ' + senderID + ' has sent you a friend request');
};

function sendNotificationMessage(message){
	notificationSocket.send(JSON.stringify(message));
}