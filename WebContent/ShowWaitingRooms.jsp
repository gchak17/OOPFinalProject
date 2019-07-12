<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="game.Room"%>
<%@ page import="game.GameManager"%>
<%@	page import="java.util.ArrayList"%>
<%@	page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Rooms</title>
<script type="text/javascript" src="notificationSocket.js"></script>
</head>
<body>
	<% 
		ArrayList<String> roomIDs = GameManager.getInstance().getWaitingRooms();
	%>
	
		<ul>
		<% for(int i = 0; i < roomIDs.size(); i++){
			%>
			<li> <%= GameManager.getInstance().getRoomById(roomIDs.get(i)).toString() %>
				
				<form action="JoinRoomServlet" method="post"  >>
				<input type = "submit" value = "Join Room" >
				<input type="hidden" name="id" value="<%= roomIDs.get(i) %>" />
				</form>
			</li>
			<%
			
		}%>
		
		
	  
	</ul>

</body>
</html>