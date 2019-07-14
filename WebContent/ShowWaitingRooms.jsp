<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="game.Room"%>
<%@ page import="game.GameManager"%>
<%@ page import="dao.Account"%>
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
<link href="login.css" rel="stylesheet" type="text/css">
</head>
<body>
	<% 
		ArrayList<String> roomIDs = GameManager.getInstance().getWaitingRooms();
	
	%>
	
		<%
		Account acc = (Account) session.getAttribute("user");
		if (acc == null) {
			response.sendRedirect("login.jsp");
			return;
		}
	%>
	<div class = "limiter">
		<div class = "container">
			<div class = "wrap">
				<ul>
				<% for(int i = 0; i < roomIDs.size(); i++){
					%>
					<li class = "txt"> <%= GameManager.getInstance().getRoomById(roomIDs.get(i)).toString() %>
						
						<form action="JoinRoomServlet" method="post"  >
						<div class="container-form-button">
							<div class="wrap-form-button">
								<div class="form-button"></div>
								<input class = "button" type = "submit" value = "Join Room" >
							</div>
						</div>
						<input type="hidden" name="id" value="<%= roomIDs.get(i) %>" />
						</form>
					</li>
					
					<%
					}%>

				</ul>
	
			</div>
		</div>
	</div>

</body>
</html>