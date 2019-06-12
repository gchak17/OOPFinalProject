var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");
canvas.addEventListener("click", defineImage, false);
            
function getCurrentPos(evt) {
    var rect = canvas.getBoundingClientRect();
    return {
        x: evt.clientX - rect.left,
        y: evt.clientY - rect.top
    };
}
            
function defineImage(evt) {
    var currentPos = getCurrentPos(evt);
    
    var json = JSON.stringify({
        "shape": "circle",
        "color": "black",
        "coords": {
            "x": currentPos.x,
            "y": currentPos.y
        }
    });
    
    drawImageText(json);
    sendText(json);
}


function drawImageText(image) {
    var json = JSON.parse(image);
    context.fillStyle = json.color;
    context.beginPath();
    context.arc(json.coords.x, json.coords.y, 5, 0, 2 * Math.PI, false);
    context.fill();
}