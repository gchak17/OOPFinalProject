<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Publish Room</title>
</head>
<body>

 <form action="PublishRoom" method="post">
 
 
	Rounds:
	<select name = "Rounds">	 
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			  <option value="6">6</option>
			  <option value="7">7</option>
			  <option value="8">8</option>
			  <option value="9">9</option>
			  <option value="10">10</option>
	</select> <br><br>
	 
 	Round Duration in seconds:
	<select name = "time">	 
			  <option value="10">10</option>
			  <option value="20">20</option>
			  <option value="60">60</option>
			  <option value="100">100</option>
	</select> <br><br>
	
	Max Players: 
		<select name = "MaxPlayers">	 
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			  <option value="6">6</option>
			  <option value="7">7</option>
			  <option value="8">8</option>
			  <option value="9">9</option>
			  <option value="10">10</option>
	</select> <br><br>
	
		<input type="submit" value="Publish Room">
	</form>

  
</body>
</html>