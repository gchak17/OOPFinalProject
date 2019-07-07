<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="managers.AccountData"%>
<%@ page import="dao.Account"%>
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

	<% 
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		Account currAccount = (Account)session.getAttribute("user");
	%>

	<img src="<%=currAccount.getAvatar().getFullPath() %>">
	
	<form action="DeleteAccountServlet" method="post">
		<input type = "submit" value = "Delete Account">
	</form>
	
	<form action="CreateRoom" method="post">
		<input type="submit" value="Create Room">
	</form>
	
	<form action="WaitingRoomsServlet" method="post">
		<input type="submit" value="Show Rooms">
	</form>
	

</body>
</html>