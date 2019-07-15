<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="managers.AccountData"%>
<%@ page import="managers.ReviewsManager"%>
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
<link href="Main.css" rel="stylesheet" type="text/css">
<!-- <link href="login.css" rel="stylesheet" type="text/css"> -->
<link rel="stylesheet" type="text/css"
	href="js_lib/css/lib/control/iconselect.css">
<script type="text/javascript" src="js_lib/control/iconselect.js"></script>
<script type="text/javascript" src="js_lib/iscroll.js"></script>
<script type="text/javascript" src="notificationSocket.js"></script>

</head>
<body>

	<%
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		Account user = (Account) session.getAttribute("user");
		ReviewsManager reviewsManager = (ReviewsManager) getServletContext().getAttribute("reviewsManager");
	%>

	<%
		Account acc = (Account) session.getAttribute("user");
		if (acc == null) {
			response.sendRedirect("login.jsp");
			return;
		}
	%>

	<div class="image">
		<img src="<%=user.getAvatar().getFullPath()%>" id="img">

		<div class="for-beauty">

			<form action="ChangeAvatarServlet" method="post">
				<div id="my-icon-select"></div>
				<input id="avatar_id_field" type="hidden" name="avatar_id" value="">
				<input type="submit" onclick="getAvatarID()" value="Change Avatar"
					class="button-dif">
			</form>

			<div class="acc-manage">
				<form action="DeleteAccountServlet" method="post">
					<input type="submit" value="Delete Account" class="button-dif">
				</form>


				<form action="LogOutServlet" method="post">
					<input type="submit" value="Log Out" class="button-dif">
				</form>
			</div>

		</div>

		<p class="username">
			<%=user.getUsername()%>
		</p>
		<div class="search-cl">
			<form action="SeeAccountProfile" method="get">
				<div class="search-part">
					<input type="text" name="accountUsername" id="sarchField" size="50">
					<input type="submit" value="Search" class="button-dif">
				</div>
			</form>
		</div>
		<div class="manage-friends">
			<button onclick="location.href='AddFriend.jsp'" type="button"
				class="button-dif">Add Friend</button>

			<div class="friendlist">
				<p class="friends-inf">
					My Friends:<br>
				</p>

				<%
					Iterator<Account> friends = accountData.getFriendAccounts(user.getID());
				%>

				<%
					while (friends.hasNext()) {
				%>
				<%
					Account friend = friends.next();
				%>

				<form id="formId" action="SeeAccountProfile" method="get">
					<input type="hidden" name="accountUsername"
						value=<%=friend.getUsername()%> /> <a href="javascript:;"
						onclick="parentNode.submit();" class="found-friend"><%=friend.getUsername()%></a>
				</form>
				<%
					}
				%>
			</div>
		</div>

		<div class="manage-not">
			<form action="NotificationsServlet" method="post">
				<input type="submit" value="See Notifications" class="button-dif">
			</form>
		</div>

		<div class="manage-username">
			<form action="ChangeUsernameServlet" method="post">
				<div class="change">
					<input type="text" name="newUsername"> <input type="submit"
						value="Change Username" class="button-dif">
				</div>
			</form>
		</div>

		<div class="points">

			<%
				double avgPoint = reviewsManager.getAvgReviewPoint(user.getID());
			%>
			<%
				if (avgPoint == -1) {
			%>
			<p class="points-inf">You have not received any reviews yet</p>
			<%
				} else {
			%>
			<p class="points-inf">
				Your Average Review Point is
				<%=avgPoint%>
			</p>
			<%
				}
			%>
		</div>


	</div>


	<div class="set-container">
		<div class="room-set-wrap">
			<span class="wrap-form-title">Round Settings</span>
			<div class="container-button">
				<form action="CreateRoom" method="post">
					<div class="wrap-button">
						<div class="room-button"></div>
						<input type="submit" value="Create Room" class="button">
					</div>
				</form>
				<div class="divider"></div>
				<form action="WaitingRoomsServlet" method="post" id="buttons">
					<div class="wrap-button">
						<div class="room-button"></div>
						<input type="submit" value="Show Rooms" class="button">
					</div>
				</form>
			</div>

		</div>
	</div>


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


	<script>
		function myFunction() {
			document.getElementById("formId").submit();
		}
	</script>
</body>
</html>