<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Incorrect Information</title>
</head>
<body>
	<h1>Please try again</h1>
	
	<p>Either your user name or password is incorrect. Please try <br> again.</p>
	
	<form action="LoginServlet" method="post">
		User Name: <input type="text" name="userName"/><br>
		Password: <input type="password" name="password"/>
		<input type="submit" value="Login">
	</form>
    
    <a href="CreateAccount.jsp">Create new Account</a>
</body>
</html>