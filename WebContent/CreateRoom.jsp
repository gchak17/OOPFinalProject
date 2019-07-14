<%@ page import="dao.Account"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link href="CreateRoom.css" rel="stylesheet" type="text/css">
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

	<form action="PublishRoom" method="post">

		<div class="container">
			<div class="containerSettings">
				<div class="name">Settings</div>
				<div class="roomSettings">
					<div class="roomName">Room</div>
					<div class="roomContent">
						<div class="containerSet">
							<div class="choice">
								<label for="roomSetRounds">Numbers Of Rounds</label> <select
									class="choose-from" id="setRounds" name = "Rounds">
									<option value="2">2</option>
									<option value="4">4</option>
									<option value="6">6</option>
									<option value="8">8</option>
									<option value="10">10</option>
								</select>
							</div>

							<div class="choice">
								<label for="roomSetTime">Turn Duration In Seconds</label> <select
									class="choose-from" id="setRounds" name = "time">
									<option value="20">20</option>
									<option value="30">30</option>
									<option value="40">40</option>
									<option value="50">50</option>
									<option value="60">60</option>
									<option value="80">80</option>
									<option value="100">100</option>
									<option value="120">120</option>
									<option value="140">140</option>
									<option value="160">160</option>
								</select>
							</div>


							<div class="choice">
								<label for="roomSetMaxPlayers">Max Players</label> <select
									class="choose-from" id="setRounds" name = "MaxPlayers">
									<option value="3">3</option>
									<option value="4">4</option>
									<option value="5">5</option>
									<option value="6">6</option>
									<option value="7">7</option>
									<option value="8">8</option>
									<option value="9">9</option>
									<option value="10">10</option>
								</select>
							</div>
						</div>
						
						<div class="container-form-button">
							<div class="wrap-form-button">
								<div class="form-button"></div>
							<button type="submit" class="button" id="publish-button">Publish
								Room</button>
							
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
	</form>


</body>
</html>