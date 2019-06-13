<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
</head>
<body>
	<h1>The Name <%=request.getParameter("userName") %> is <br>
	Already In Use</h1>
	
	<p>Please enter another name and password</p>
	
	<form action="AccountCreationServlet" method="post">
		User Name: <input type="text" name="userName"/><br>
		Password: <input type="password" name="password"/>
		<select name="avatar">
			<!-- avatar images must be shown  -->
			<option value="1.png">avatar1</option>
			<option value="2.png">avatar2</option>
			<option value="3.png">avatar3</option>
			<option value="4.png">avatar4</option>
			<option value="5.png">avatar5</option>
			<option value="6.png">avatar6</option>
			<option value="7.png">avatar7</option>
			<option value="8.png">avatar8</option>
			<option value="9.png">avatar9</option>
		</select> <br>
		<input type="submit" value="Sign Up">
	</form>
</body>
</html>