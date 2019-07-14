<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Welcome</title>
	<link href="login.css" rel="stylesheet" type="text/css">
</head>
<body>
	
	<div class = "limiter">
		<div class = "container">
			<div class = "wrap">
			    <form action="LoginServlet" method="post">
			    		
			    		<span class = "form-title">Welcome to Our Game!</span>
			    		<div class = "wrap-input">
							User Name: <input class = "input" type="text" name="userName" required="required" pattern="[A-Za-z0-9]{1,20}">
						</div>
						<div class = "wrap-input">
							Password: <input class = "input" type="password" name="password" required="required" pattern="[A-Za-z0-9]{1,20}">
						</div>
						<div class="container-form-button">
							<div class="wrap-form-button">
								<div class="form-button"></div>
								<input class = "button" type="submit" value="Login">
							</div>
						</div>		
						<a class = "txt" style = "text-align:center;z-index:1000;" href="CreateAccount.jsp">Create new Account</a>
					
				</form>
			</div>
		</div>
	</div>
    
</body>
</html>