
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
<link href="login.css" rel="stylesheet" type="text/css">
</head>

<body>

	<%
		Account acc = (Account) session.getAttribute("user");
		if (acc == null) {
			response.sendRedirect("login.jsp");
			return;
		}
	%>
	

	<%
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		Account currAccount = (Account) session.getAttribute("user");
		FriendRequestManager friendRequestManager = (FriendRequestManager) getServletContext()
				.getAttribute("friendRequestManager");
	%>

	<%
		Iterator<Long> requests = friendRequestManager.getRequests(currAccount.getID());
	%>

	<div class = "limiter">
		<div class = "container">
			<div class = "wrap">
	
				
				<form action="BackToProfileServlet" method="post">
					<div class="container-form-button">
						<div class="wrap-form-button">
							<div class="form-button"></div>
							<input class = "button" type="submit" value="Back to profile">
						</div>
					</div>
				</form>

				<%
					while (requests.hasNext()) {
				%>
				<%
					Account requestSender = accountData.getAccountByID(requests.next());
				%>
				
				<div class = "txt" style = "font-size:25px;">
					<%=requestSender.getUsername() + " sent a friend request"%>
				</div>
				
				<form action="AcceptFriendRequestServlet" method="post">
					<div class="container-form-button">
						<div class="wrap-form-button">
							<div class="form-button"></div>
							<input class = "button" type="submit" value="Accept"> <input
								name="requestSenderUsername" type="hidden"
								value=<%=requestSender.getUsername()%>>
						</div>
					</div>
				</form>
				
				<form action="RejectFriendRequestServlet" method="post">
				<div class="container-form-button">
					<div class="wrap-form-button">
						<div class="form-button"></div>
						<input class = "button" type="submit" value="Reject"> <input
							name="requestSenderUsername" type="hidden"
							value=<%=requestSender.getUsername()%>>
					</div>
				</div>
				</form>
				<%
					}
				%>
				
			</div>
		</div>
	</div>

</body>
</html>