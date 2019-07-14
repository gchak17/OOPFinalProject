<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Welcome</title>
	<link href="login.css" rel="stylesheet" type="text/css">
</head>
<body>
	
	<div class = "limiter">
		<div class = "container-login">
			<div class = "wrap-login">
			    <form action="LoginServlet" method="post">
			    		
			    		<span class = "login-form-title">Welcome to Our Game!</span>
			    		<div class = "wrap-input">
							User Name: <input class = "input" type="text" name="userName" required="required"><br>
						</div>
						<div class = "wrap-input">
							Password: <input class = "input" type="password" name="password" required="required">
						</div>
						<div class="container-login-form-button">
							<div class="wrap-login-form-button">
								<div class="login-form-button"></div>
								<input class = "login-button" type="submit" value="Login">
								</div>
						</div>		
						<a class = "txt" style = "text-align:center;" href="CreateAccount.jsp">Create new Account</a>
					
				</form>
			</div>
		</div>
	</div>
    
</body>
</html>