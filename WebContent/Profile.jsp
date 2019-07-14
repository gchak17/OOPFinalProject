<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="managers.AccountData"%>
<%@ page import="managers.ReviewsManager"%>
<%@ page import="dao.Account"%>
<%@	page import="java.util.Iterator"%>
<%@ page import="dao.Account"%>

<% 
	AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
	Account currAccount = (Account)session.getAttribute("currAccount");
	Account user = (Account)session.getAttribute("user");
	ReviewsManager reviewsManager = (ReviewsManager) getServletContext().getAttribute("reviewsManager");
%>

	<%
		Account acc = (Account) session.getAttribute("user");
		if (acc == null || (Account)session.getAttribute("currAccount") == null) {
			response.sendRedirect("login.jsp");
			return;
		} 
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
	
	<% if(user.isFriendsWith(currAccount.getID())) { %>
		<p>Your Friend!</p>
	<%} else {%>
		<form action="AddFriend.jsp">
			<input type = "submit" value = "Add Friend">
		</form>
	<%} %>
	
	<% double avgPoint = reviewsManager.getAvgReviewPoint(currAccount.getID()); %>
	<% if (avgPoint == -1) {%>
		<p>You have not recieved any reviews yet</p>
	 <%} else { %>
	 	<p> Average Review Point is <%= avgPoint %> </p>
	 <%} %>
	 
	 <p>Give Review Point! It helps them become better !!! </p>
	 <form action = "WriteReview" method = "post">
	 	<input type="hidden" name="reviewRecieverUsername" value=<%=currAccount.getUsername()%> />
		<select name = "point">
			<option value="5">5</option>
			<option value="4">4</option>
			<option value="3">3</option>
			<option value="2">2</option>
			<option value="1">1</option>
			<option value="0">0</option>
		</select>
		<input type="submit" onclick="parentNode.submit();" value="Write">
    </form>
    
	<p>Friends:<br></p>
	
	<% Iterator<Account> friends = accountData.getFriendAccounts(currAccount.getID()); %>
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