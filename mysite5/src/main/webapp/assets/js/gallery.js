var request = new XMLHttpRequest();
request.open("GET", "./assets/images");
request.onreadystatechange = function(){
	if(this.readyState == 4){
		//console.log(this.response);
		var data = JSON.parse(this.response);
		for(var i = 0; i < data.length; i++){
			var div = document.createElement("div");
			div.setAttribute("class", "image");
			var img = document.createElement("img");
			img.src = data[i];
			div.appendChild(img);
			document.body.appendChild(div);
		}
	}
}
request.send();