<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="managers.AccountData"%>
<%@ page import="dao.Account"%>
<%@	page import="managers.FriendRequestManager"%>
<%@	page import="java.util.Iterator"%>
<%@ page import="dao.Account"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Your Notifications</title>
<script type="text/javascript" src="notificationSocket.js"></script>
</head>

<body>

	<%
		Account acc = (Account) session.getAttribute("user");
		if (acc == null) {
			response.sendRedirect("login.jsp");
			return;
		}
	%>
	<form action="BackToProfileServlet" method="post">
		<input type="submit" value="Back to profile">
	</form>

	<%
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		Account currAccount = (Account) session.getAttribute("user");
		FriendRequestManager friendRequestManager = (FriendRequestManager) getServletContext()
				.getAttribute("friendRequestManager");
	%>

	<%
		Iterator<Long> requests = friendRequestManager.getRequests(currAccount.getID());
	%>


	<%
		while (requests.hasNext()) {
	%>
	<%
		Account requestSender = accountData.getAccountByID(requests.next());
	%>
	<%=requestSender.getUsername() + " sent a friend request"%>
	<form action="AcceptFriendRequestServlet" method="post">
		<input type="submit" value="Accept"> <input
			name="requestSenderUsername" type="hidden"
			value=<%=requestSender.getUsername()%>>
	</form>
	<form action="RejectFriendRequestServlet" method="post">
		<input type="submit" value="Reject"> <input
			name="requestSenderUsername" type="hidden"
			value=<%=requestSender.getUsername()%>>
	</form>
	<%
		}
	%>

</body>
</html>