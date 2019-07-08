<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="managers.AccountData"%>
<%@ page import="dao.Account"%>
<%@	page import="managers.FriendRequestManager"%>
<%@	page import="java.util.Iterator"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Your Notifications</title>
</head>

<body>
	<% 
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		Account currAccount = (Account)session.getAttribute("user");
		FriendRequestManager friendRequestManager = (FriendRequestManager) getServletContext().getAttribute("friendRequestManager");
	%>
	
	<% Iterator<Long> requests = friendRequestManager.getRequests(currAccount.getID()); %>
    <ul>
		<% while(requests.hasNext()) {
			%>
			<li>"<%= accountData.getAccountByID(requests.next()).getUsername() + "sent a friend request" %>"</li>
			<%
		}%>
	  
	</ul>
</body>
</html>