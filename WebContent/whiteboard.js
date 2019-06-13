var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");
var offsetLeft = canvas.offsetLeft;
var offsetTop  = canvas.offsetTop;

var drawing = false;
var lastPos = null;

listen(canvas, 'mousedown', function(event) {
    drawing = true;
    lastPos = getPos(event);
});

listen(canvas, 'mousemove', function(event) {
    if (!drawing) {
        return;
    }
    
    var p = getPos(event);
    
    var json = JSON.stringify({
        "start": { "x": lastPos[0], "y": lastPos[1]},
	    "end": { "x": p[0], "y": p[1]}
    });
    
    drawImageText(json);
    sendText(json);
    
    lastPos = p;
});

function drawImageText(image) {
    var json = JSON.parse(image);
    context.beginPath();
    context.moveTo(json.start.x, json.start.y);
    context.lineTo(json.end.x, json.end.y);
    context.stroke();
}

listen(document, 'mouseup', function(event) {
    drawing = false;
});

function listen(elem, type, listener) {
    elem.addEventListener(type, listener, false);
}

function getPos(event) {
    var x = event.clientX - offsetLeft;
    var y = event.clientY - offsetTop;
    return [x, y];
}