<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="nonDefaultPackage.AccountData"%>
<%@ page import="nonDefaultPackage.Account"%>
<%@	page import="java.util.ArrayList"%>
<%@	page import="java.util.Iterator"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Welcome to your account</title>
</head>
<body>

	<% AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		Account currAccount = accountData.getAccount(request.getParameter("userName"), request.getParameter("password"));
		Iterator<Account> friends = currAccount.getFriendsList();
	%>

	<img src="<%=currAccount.getAvatar() %>">
	
	<form action="CreateRoom" method="post">
		<input type="submit" value="Create Room">
	</form>
	
	<form action="GameServlet" method="post">
		<input type="submit" value="Join Room">
	</form>

	<ul>
		<% while(friends.hasNext()) {
			%>
			<li>"<%= friends.next().getUsername() %>"</li>
			<%
		}%>
	  
	</ul>

</body>
</html>