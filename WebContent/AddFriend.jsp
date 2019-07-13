<%@ page import="dao.Account"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="notificationSocket.js"></script>
</head>
<body>
	<%
		Account acc = (Account) request.getSession().getAttribute("user");
		if (acc == null) {
			response.sendRedirect("login.jsp");
			return;
		}
	%>
	<form action="BackToProfileServlet" method="post">
		<input type="submit" value="Back to profile">
	</form>

	<br>

	<script>
		var xhr = null;

		function sendRequest() {
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

			var url = 'SendRequestServlet';

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


	<form action="#" method="post" onsubmit="sendRequest(); return false;">
		<input id="friendusername" type="text" name="friendusername">
		<input type="submit" value="Send Request">
	</form>
</body>
</html>