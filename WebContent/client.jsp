<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="game.GameManager"%>
<%@ page import="game.Game"%>
<%@ page import="game.Player"%>
<%@	page import="java.util.ArrayList"%> 
<!DOCTYPE html>
<html lang="en-US">
<head>
    <meta charset="ISO-8859-1">
    <title>whiteboard</title>
    <link href="client.css" rel="stylesheet" type="text/css">
</head>
<body onresize="changeSizeAndPosition()" onload="changeSizeAndPosition()">
    <script>
    	function changeSizeAndPosition(){
            //canvas-panel
            document.getElementById("canvas-panel").width = innerWidth * 0.6;
            document.getElementById("canvas-panel").height = innerHeight * 0.6;
            document.getElementById("canvas-panel").style.top = innerHeight * 0.2 + "px";
            document.getElementById("canvas-panel").style.left = innerWidth * 0.2 + "px";

            //center-panel class
            var x = document.getElementsByClassName("center-panel");
            for (var i = 0; i < x.length; i++) {
                x[i].style.width = innerWidth * 0.6 + "px";
                x[i].style.height = innerHeight * 0.2 + "px";
                x[i].style.left = innerWidth * 0.2 + "px";
            }

            //word-panel
            document.getElementById("word-panel").style.top = "0px";

            //paint-options-panel
            document.getElementById("paint-options-panel").style.top = innerHeight * 0.8 + "px";

            //side-panel class
            var x = document.getElementsByClassName("side-panel");
            for (var i = 0; i < x.length; i++) {
                x[i].style.width = innerWidth * 0.2 + "px";
                x[i].style.height = innerHeight + "px";
                x[i].style.top = "0px";
            }

            //users-panel
            document.getElementById("users-panel").style.left = "0px";

            //chat-panel
            document.getElementById("chat-panel").style.left = innerWidth * 0.8 + "px";

            //chat-box class
            var x = document.getElementsByClassName("chat-box-class");
            for (var i = 0; i < x.length; i++) {
                x[i].style.width = innerWidth * 0.18 + "px";
                x[i].style.height = innerHeight * 0.87 + "px";
            }
    	}    
    </script>

   	<!--   
    <% 
		//String gameId = (String)request.getSession().getAttribute("gameId");
		//Game game = GameManager.getInstance().getGame(gameId);
		//ArrayList<Player> players = game.getPlayers();
	%>
	-->

    <div id="users-panel" class="side-panel"></div>
	<div id="chat-panel" class="side-panel">
		<div id="chat-box-div" class="chat-box-class"></div>
		<input type="text" placeholder=" type message" id="chat-text" size="30">		
        <button onclick="clickOnSend()" class="send-button">Send</button>
	</div>

    <canvas id="canvas-panel"></canvas>

    <div id="word-panel" class="center-panel"></div>
    <div id="paint-options-panel" class="center-panel">
    <!--    
        <div id="choosecolor">choose color:</div>
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

        <div id="eraser">get eraser:</div>
        <div id="white" onclick="color(this)" class="paint-options-panel-colors"></div>
            
        <div id="choosewidth">choose width:</div>
        <div id="w4" onclick="width(this)"></div>
        <div id="w8" onclick="width(this)"></div>
        <div id="w16" onclick="width(this)"></div>

        <input type="button" value="clear canvas" id="clr" size="23" onclick="clearCanvas()"> 
    -->
    </div> 
    <script src = "client.js"></script>
    <script src = "chat.js"></script>
</body>
</html>