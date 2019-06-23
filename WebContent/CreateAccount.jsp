<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Create account</title>
</head>
<body>
	<h1>Create new account.</h1>
	
	<p>Please enter proposed name and password</p>
	
    <form action="AccountCreationServlet" method="post">
		User Name: <input type="text" name="userName"/><br>
		Password: <input type="password" name="password"/><br>
		<select name="avatar">
			<!-- avatar images must be shown  -->
			<option value="./avatars/1.png">avatar1</option>
			<option value="./avatars/2.png">avatar2</option>
		</select> <br>
		<input type="submit" value="Sign Up">
	</form>
</body>
</html>