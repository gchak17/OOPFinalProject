<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="managers.AccountData"%>
<%@ page import="dao.Account"%>
<%@	page import="java.util.ArrayList"%>
<%@	page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome to your account</title>

<link rel="stylesheet" type="text/css"
	href="js_lib/css/lib/control/iconselect.css">
<script type="text/javascript" src="js_lib/control/iconselect.js"></script>
<script type="text/javascript" src="js_lib/iscroll.js"></script>
<script type="text/javascript" src="notificationSocket.js"></script>

</head>
<body>

	<%
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		Account currAccount = (Account) session.getAttribute("user");
	%>

	<%
		Account acc = (Account) session.getAttribute("user");
		if (acc == null) {
			response.sendRedirect("login.jsp");
			return;
		}
	%>

	<img src="<%=currAccount.getAvatar().getFullPath()%>">

	<form action="ChangeAvatarServlet" method="post">
		<div id="my-icon-select"></div>
		<input id="avatar_id_field" type="hidden" name="avatar_id" value="">
		<input type="submit" onclick="getAvatarID()" value="Change Avatar">
	</form>

	<script>
		var iconSelect;

		window.onload = function() {
			iconSelect = new IconSelect("my-icon-select", {
				'selectedIconWidth' : 48,
				'selectedIconHeight' : 48,
				'selectedBoxPadding' : 5,
				'iconsWidth' : 48,
				'iconsHeight' : 48,
				'boxIconSpace' : 3,
				'vectoralIconNumber' : 2,
				'horizontalIconNumber' : 2
			});

			var icons = [];
			icons.push({
				'iconFilePath' : 'avatars/1.png',
				'iconValue' : '1'
			});
			icons.push({
				'iconFilePath' : 'avatars/2.png',
				'iconValue' : '2'
			});
			icons.push({
				'iconFilePath' : 'avatars/3.png',
				'iconValue' : '3'
			});
			icons.push({
				'iconFilePath' : 'avatars/4.png',
				'iconValue' : '4'
			});
			icons.push({
				'iconFilePath' : 'avatars/5.png',
				'iconValue' : '5'
			});
			icons.push({
				'iconFilePath' : 'avatars/6.png',
				'iconValue' : '6'
			});
			icons.push({
				'iconFilePath' : 'avatars/7.png',
				'iconValue' : '7'
			});
			icons.push({
				'iconFilePath' : 'avatars/8.png',
				'iconValue' : '8'
			});
			icons.push({
				'iconFilePath' : 'avatars/9.png',
				'iconValue' : '9'
			});

			iconSelect.refresh(icons);
		};
	</script>

	<script>
		function getAvatarID() {
			document.getElementById('avatar_id_field').value = iconSelect
					.getSelectedValue();
		};
	</script>

	<form action="DeleteAccountServlet" method="post">
		<input type="submit" value="Delete Account">
	</form>

	<form action="CreateRoom" method="post">
		<input type="submit" value="Create Room">
	</form>

	<form action="WaitingRoomsServlet" method="post">
		<input type="submit" value="Show Rooms">
	</form>

	<button onclick="location.href='AddFriend.jsp'" type="button">Add
		Friend</button>

	<form action="NotificationsServlet" method="post">
		<input type="submit" value="See Notifications">
	</form>
<<<<<<< HEAD
	
	
	<form action="SeeProfileServlet" method="post">
		<input type = "text" name = "currAccountUsername">
		<input type = "submit" value = "Search Account">
	</form>
=======
>>>>>>> e9b67ae7c8432ee23a09507d3b2b7d3feeaf532b

	<form action="LogOutServlet" method="post">
		<input type="submit" value="Log Out">
	</form>
<<<<<<< HEAD
	
	
	<p>Friends:<br></p>
	
	<% Iterator<Account> friends = currAccount.getFriendList(); %>

	<% while(friends.hasNext())  {
		%>
		<% Account friend = friends.next(); %>
		
<<<<<<< HEAD
		<form id="formId" action="<%=request.getContextPath()%>/SeeProfileServlet" method="post">
		    <input type="hidden" name="currAccountUsername" value=<%=friend.getUsername()%> />
		    <a href="javascript:myFunction()"><%=friend.getUsername()%></a>
=======
		<form id="formId" action="SeeFriendProfile" method="get">
		    <input type="hidden" name="friendName" value=<%=friend.getUsername()%> />
		    <a href="javascript:;" onclick = "parentNode.submit();"><%=friend.getUsername()%></a>
>>>>>>> 05d02e715fff1027bac19cf50318b947c254101d
		</form>
		<%
	}%>
	
=======


	<p>
		Friends:<br>
	</p>

	<%
		Iterator<Account> friends = currAccount.getFriendList();
	%>

	<%
		while (friends.hasNext()) {
	%>
	<%
		Account friend = friends.next();
	%>

	<form id="formId" action="SeeFriendProfile" method="get">
		<input type="hidden" name="friendName" value=<%=friend.getUsername()%> />
		<a href="javascript:;" onclick="parentNode.submit();"><%=friend.getUsername()%></a>
	</form>
	<%
		}
	%>

>>>>>>> e9b67ae7c8432ee23a09507d3b2b7d3feeaf532b
	<script>
		function myFunction() {
			document.getElementById("formId").submit();
		}
	</script>

</body>
</html>