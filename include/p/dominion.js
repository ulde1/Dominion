var evtSource;


window.onload = function() {
	console.log("onload");
	evtSource = new EventSource("e/?session="+document.getElementById("session").value);
	evtSource.addEventListener("neu", function(e) {
		console.log("neu: "+e.data);
		evtSource.close();
		location = e.data;
	}, false);
	evtSource.addEventListener("send", function(e) {
		console.log("neu: "+e.data);
		updateBody(e.data);
	}, false);
	evtSource.addEventListener("playAudio", function(e) {
		console.log("playAudio: "+e.data);
		new Audio(e.data).play();
	}, false);
};


window.onbeforeunload = function() {
	console.log("onbeforeunload");
	evtSource.close();
};


function updateBody(json) {
	var obj = JSON.parse(json);
	body = document.getElementById("body");
	body.innerHTML = obj.body;
	body.className = obj.className;
}


function submitButton(button) {
    var http = new XMLHttpRequest();
    http.open("POST", "?", true);
    http.onload = function() {
        console.log(http.responseText);
    	updateBody(http.responseText);
    }
    http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    var params = "session="+document.getElementById("session").value+
    	"&ajax=1&"+button.name+"="+button.value;
    var token = document.getElementById("token");
    if (token!=null) {
		params = params + "&token="+token.value;
	}
    http.send(params);
}

