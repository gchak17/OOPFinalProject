<%@ page import="game.Player"%>
<%@ page import="dao.Account"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
   	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en-US">
<head>
<meta charset="ISO-8859-1">
<title>whiteboard</title>
<link href="client.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="notificationSocket.js"></script>
</head>
<body onload="loadResizeFun();" onresize="loadResizeFun();">
	
	<%	
		String game_id = (String)request.getSession().getAttribute("gameId");
	%>
	
	<script>
		var game_id = "<%= game_id %>";
	</script>

	<%
		Player pl = (Player)request.getSession().getAttribute("player");
		if (pl == null) {
			Account acc = (Account) session.getAttribute("user");
			if (acc == null) {
				response.sendRedirect("login.jsp");
			}else{
				response.sendRedirect("main.jsp");
			}
			return;
		}
	%>

	<div id="users-panel" class="side-panel">
	</div>
	<div id="chat-panel" class="side-panel">
		<div id="chat-box-div" class="chat-box-class"></div>
		<input type="text" placeholder=" type message" id="chat-text"
			size="30" required="required"
			onkeydown="if (event.keyCode == 13) clickOnSend()" />
		<button onclick="clickOnSend()" class="send-button">Send</button>
	</div>

	<!-- The Modal -->
	<div id="myModal" class="modal">

		<!-- Modal content -->
		<div class="modal-content">
			<div class="modal-header">
				<h2>Choose A Word</h2>
			</div>
			<div class="modal-body">
				<input type="button" value="word1" id="word-button1"></input> <input
					type="button" value="word2" id="word-button2"></input> <input
					type="button" value="word3" id="word-button3"></input>
			</div>
		</div>

	</div>
	<!-- The Modal ends here -->

	<div id="guesserModal" class="modal">
		<div class="modal-content">
			<div class="modal-header">
				<h2>The Artist Is Choosing The Word</h2>
			</div>
		</div>

	</div>


	<canvas id="canvas-panel"></canvas>

	<div id="word-panel" class="center-panel">
		<p id="word-place" style="left:50vw;top:50vh"></p>
		<div id="timer-div"></div>
	</div>
	<div id="paint-options-panel" class="center-panel">      
		<div id="choosecolor">choose color:</div>
        <input type="button" value="clear canvas" id="clr" size="23" onclick="clearCanvas()"> 

		<div id="colors-div">
	        <div id="green" onclick="color(this)" class="paint-options-panel-colors"></div>
	        <div id="blue" onclick="color(this)" class="paint-options-panel-colors"></div>
	        <div id="red" onclick="color(this)" class="paint-options-panel-colors"></div>
	        <div id="yellow" onclick="color(this)" class="paint-options-panel-colors"></div>
	        <div id="orange" onclick="color(this)" class="paint-options-panel-colors"></div>
	        <div id="black" onclick="color(this)" class="paint-options-panel-colors"></div>
	        <div id="gray" onclick="color(this)" class="paint-options-panel-colors"></div>
	        <div id="purple" onclick="color(this)" class="paint-options-panel-colors"></div>
	        <div id="brown" onclick="color(this)" class="paint-options-panel-colors"></div>
	        <div id="aqua" onclick="color(this)" class="paint-options-panel-colors"></div>
	        <div id="pink" onclick="color(this)" class="paint-options-panel-colors"></div>
	        <div id="magenta" onclick="color(this)" class="paint-options-panel-colors"></div>
    	</div>

        <div id="eraser">get eraser:</div>
        <div id="white" onclick="color(this)" class="paint-options-panel-colors"></div>
            
        <div id="choosewidth">choose width:</div>
        <div class="slidecontainer"> <input type="range" min="2" max="8" value="5" class="slider" id="myRange"> </div>
    </div>
	<script src="client.js"></script>
	<script src="chat.js"></script>
</body>
</html>