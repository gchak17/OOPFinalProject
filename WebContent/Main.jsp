<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="nonDefaultPackage.AccountData"%>
<%@ page import="nonDefaultPackage.Account"%>
<%@	page import="java.util.ArrayList"%>
<%@	page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Welcome to your account</title>
</head>
<body>

	<% AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		Account currAccount = accountData.getAccount(request.getParameter("userName"), request.getParameter("password"));
		List<Account> friends = accountData.getFriendsFor(currAccount.getUsername());
	%>

	<img src="<%=currAccount.getAvatar() %>">
	
	<form action="CreateRoom" method="post">
		<input type="submit" value="Create Room">
	</form>
	
	<form action="GameServlet" method="post">
		<input type="submit" value="Join Room">
	</form>

	<ul>
		<% for(int i = 0; i < friends.size(); i++){
			%>
			<li>"<%= friends.get(i).getUsername() %>"</li>
			<%
		}%>
	  
	</ul>

</body>
</html>