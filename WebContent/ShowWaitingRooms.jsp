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
</head>
<body>
	<% 
		ArrayList<Room> rooms = GameManager.getInstance().getWaitingRooms();
	%>
	
		<ul>
		<% for(int i = 0; i < rooms.size(); i++){
			%>
			<li> <%= rooms.get(i).toString() %>
				
				<form action="JoinRoomServlet" method="post"  >>
				<input type = "submit" name="id" value = <%= "Room:" + i %>>
				</form>
			<li>
			<%
			
		}%>
		
		
	  
	</ul>

</body>
</html>