var canvas, 
    ctx, 
    flag = false,
    prevX = 0,
    currX = 0,
    prevY = 0,
    currY = 0,
    dot_flag = false,
    currCol = "black", 
    currWidth = 2;

function init() {
    canvas = document.getElementById('paintercanvas');
    ctx = canvas.getContext("2d");
    w = canvas.width;
    h = canvas.height;

    canvas.addEventListener("mousemove", function (e) {findxy('move', e)}, false);
    canvas.addEventListener("mousedown", function (e) {findxy('down', e)}, false);
    canvas.addEventListener("mouseup", function (e) {findxy('up', e)}, false);
    canvas.addEventListener("mouseout", function (e) {findxy('out', e)}, false);
}

function color(obj) {
    currWidth = (obj.id == "white") ? 14 : 2
    currCol = obj.id;
}

function draw() {
    ctx.beginPath();
    ctx.moveTo(prevX, prevY);
    ctx.lineTo(currX, currY);
    ctx.strokeStyle = currCol;
    ctx.lineWidth = currWidth;
    ctx.stroke();
    ctx.closePath();
    send();
}

function clear() {
    ctx.clearRect(0, 0, w, h);
    document.getElementById("canvasimg").style.display = "none";
}

function send() {
    document.getElementById("canvasimg").style.border = "2px solid";
    var dataURL = canvas.toDataURL();
    document.getElementById("canvasimg").src = dataURL;
    document.getElementById("canvasimg").style.display = "inline";
}

function findxy(res, e) {
    if (res == 'down') {
        prevX = currX;
        prevY = currY;
        currX = e.clientX - canvas.offsetLeft;
        currY = e.clientY - canvas.offsetTop;

        flag = true;
        dot_flag = true;
        if (dot_flag) {
            ctx.beginPath();
            ctx.fillStyle = currCol;
            ctx.fillRect(currX, currY, 2, 2);
            ctx.closePath();
            dot_flag = false;
        }
    }
    if (res == 'up' || res == "out") {
        flag = false;
    }
    if (res == 'move') {
        if (flag) {
            prevX = currX;
            prevY = currY;
            currX = e.clientX - canvas.offsetLeft;
            currY = e.clientY - canvas.offsetTop;
            draw();
        }
    }
}