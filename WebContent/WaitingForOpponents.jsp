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
	         response.setIntHeader("Refresh", 2);
	         int RoomId = (Integer)request.getAttribute("id");
	     %>
	         <form action="StartGameServlet" method="post"  >>
      		<input type="hidden" name="id" value="<%= RoomId %>" />
				<input type = "submit" value = "Start Game">
				</form>
	     
	     
	     <%    
	         Room r = GameManager.getInstance().getWaitingRooms().get(RoomId);
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

	         
      
</body>
</html>