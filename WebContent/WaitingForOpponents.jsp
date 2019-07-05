<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import = "java.io.*,java.util.*" %>
    <%@ page import="game.Room"%>
    <%@ page import="game.Player"%>
<%@ page import="game.GameManager"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
         
         		
				
         <%
	         String RoomId = (String)request.getAttribute("id");
	     %>
	         <form action="StartGameServlet" method="post"  >>
      		<input type="hidden" name="id" value="<%= RoomId %>" />
				<input type = "submit" value = "Start Game">
				</form>
	     
	     
	     <%    
	         Room r = GameManager.getInstance().getRoomById(RoomId);
	         ArrayList<Player> players = r.getPlayers();
	         %>
	         <ul>
	 		<% for(int i = 0; i < players.size(); i++){
	 			%>
	 			<li> <%= players.get(i).toString() %>
	 				
	 			<li>
	 			<%
	 			
	 		}%>
	 		
	 		
	 	  
	 	</ul>

	<script>
	//var wsUri = "ws://" + document.location.host + document.location.pathname + "websocket";

	var websocket = new WebSocket("ws://localhost:8080/OOPFinalProject/PublishRoom");
	websocket.onmessage = function(evt) { onMessage(evt) };

	function sendText(json) {
	    websocket.send(json);
	}

	function onMessage(evt) {
		//daarefreshos gadmocemuli informaciis mixedvit..principshi arc chirdeba gadmocema.. prosta unda utxras ro ganaxsldes
	}


	</script>    
      
</body>
</html>