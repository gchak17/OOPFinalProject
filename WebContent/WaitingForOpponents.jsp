<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="game.Room"%>
<%@ page import="game.Player"%>
<%@ page import="game.GameManager"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="notificationSocket.js"></script>
<link href="login.css" rel="stylesheet" type="text/css">
</head>
<body>

	<%
		Player pl = (Player) session.getAttribute("player");
		if (pl == null) {
			response.sendRedirect("main.jsp");
			return;
		}
	%>
	<div class = "limiter">
		<div class = "container">
			<div class = "wrap">
				
				<div class="container-form-button">
					<div class="wrap-form-button">
						<div class="form-button"></div>
						<button class = "button" onclick="startGame()">Start Game</button>
					</div>
				</div>
				
				<p class = "txt" id="demo"></p>						
			</div>
		</div>
	</div>

	<p id="demo"></p>

	<script>
		//var wsUri = "ws://" + document.location.host + document.location.pathname + "websocket";
		//var websocket = new WebSocket("ws://localhost:8888/OOPFinalProject/PublishRoom");
		var websocket = new WebSocket("ws://192.168.98.14:8888/OOPFinalProject/PublishRoom");
		//var websocket = new WebSocket('ws://' + window.location.host + '/OOPFinalProject/PublishRoom');
		websocket.onmessage = function(evt) {
			updatePage(evt)
		};

		function updatePage(evt) {

			var json = JSON.parse(evt.data);

			if (json.type === "playersList") {
				document.getElementById("demo").innerHTML = json.players;
			} else if (json.type === "start") {
				if (json.admin) {
					if (json.forward) {
						//location.replace("http://localhost:8888/OOPFinalProject/client.jsp");
						location.replace("http://192.168.98.14:8888/OOPFinalProject/client.jsp");
						//location.replace('http://' + window.location.host + '/OOPFinalProject/client.jsp');
					}
				} else {
					//alert("ar xar adzmini dzmao");
				}
			} else if (json.type == "redirect"){
				//location.replace("http://localhost:8888/OOPFinalProject/Main.jsp");
				//location.replace('http://' + window.location.host + '/OOPFinalProject/Main.jsp');
				location.replace("http://192.168.98.14:8888/OOPFinalProject/Main.jsp");
			}
		}

		function startGame() {
			var json = JSON.stringify({
				"type" : "start",
				"forward" : false
			});
			websocket.send(json);
		}
	</script>
</body>
</html>