<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="managers.AccountData"%>
<%@ page import="dao.Account"%>
<%@	page import="java.util.Iterator"%>

<% 
	AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
	Account currAccount = (Account)session.getAttribute("currAccount");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= currAccount.getUsername() %>'s profile </title>
</head>

<body>
	<form action="BackToProfileServlet" method="post">
		<input type="submit" value="Back to profile">
	</form>
	
	<img src="<%=currAccount.getAvatar().getFullPath() %>">
	
	<br>
	
	<%= currAccount.getUsername() %>
	
	<p>My Friends:<br></p>
	
	<% Iterator<Account> friends = currAccount.getFriendList(); %>
	<ul>
		<% while(friends.hasNext()){
			%>
			<li> <%= friends.next().getUsername() %>
			</li>
			<%
			
		}%>
	</ul>

</body>
</html>