<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dao.Account"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="login.css" rel="stylesheet" type="text/css">
</head>
<body>
	
	<%
		Account acc = (Account) request.getSession().getAttribute("user");
		if (acc == null) {
			response.sendRedirect("login.jsp");
			return;
		}
	%>

	<script>
		var xhr = null;

		function removeFriend() {
			console.log("wow");
			try {
				xhr = new XMLHttpRequest();
			} catch (e) {
				xhr = new ActiveXObject("Microsoft.XMLHTTP");
			}

			if (xhr == null) {
				alert('ajax not supported');
				return;
			}

			var url = 'RemoveFriendServlet';

			xhr.onreadystatechange = handler;
			xhr.open('POST', url, true);
			var receiver = document.getElementById('friendusername').value;
			xhr.send('{"friendusername" :  "'
					+ document.getElementById('friendusername').value + '"}');
		};

		function handler() {
			if (xhr.readyState == 4) {
				if (xhr.status == 200) {
					console.log(xhr.responseText);
					var jsonResponse = JSON.parse(xhr.responseText);
					alert(jsonResponse.responseText);
					if (jsonResponse.success) {
						sendNotificationMessage(jsonResponse.notificationLog);
					}
				} else {
					alert('something went wrong');
				}
			}
		};
	</script>
	
	<div class = "limiter">
		<div class = "container">
			<div class = "wrap">
				<form action="#" method="post" onsubmit="removeFriend(); return false;">
					<div class = "wrap-input">
						User Name:<input class = "input" id="friendusername" type="text" name="friendusername">
					</div>
					<div  class="container-form-button">
						<div class="wrap-form-button">
							<div class="form-button"></div>
							<input class = "button" type="submit" value="Remove Friend">
						</div>
					</div>
				</form>
				
				<form action="BackToProfileServlet" method="post">
					<div  class="container-form-button">
						<div class="wrap-form-button">
							<div class="form-button"></div>
							<input class = "button" type="submit" value="Back to profile">
						</div>
					</div>
								
				</form>
			</div>
		</div>
	</div>
	
	
</body>
</html>