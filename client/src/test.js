//-----------------------------------------------------------------------------------------
//Main
//-----------------------------------------------------------------------------------------

//get local storage and create nodes
var val = JSON.parse(localStorage.getItem(localStorage.key(0)));
if(val != null){
	val.forEach(function(obj) {
	createNode(obj.Attraction, obj.No_adults, obj.No_kids)
	})
} else{
	console.log("No LocalStorage");
}

//event listener to final payment button
var finalize_pay_button = document.getElementById("finalizepaymentbutton");
finalize_pay_button.addEventListener("click", function(){finalizePayButtonClicked(val)});	


//-----------------------------------------------------------------------------------------
//Functions
//-----------------------------------------------------------------------------------------
function createNode(attraction, No_adults, No_kids){
	var main = document.querySelector("main");
	var template = document.querySelector("#ticket");

	var clone = template.content.cloneNode(true);
	var div = clone.querySelectorAll("div");
	div[0].textContent = attraction;
	div[1].textContent = "Adults: " + No_adults;
	div[2].textContent = "Kids: " + No_kids;

	main.insertBefore(clone, main.childNodes[0]); //insert before first node in main

}

function finalizePayButtonClicked(val){
	//create user
	var user = ID();

	val.forEach(function(order) {
		//add user object to order
		order.User = user;
		sendToAPI(order);
	})
	localStorage.clear();
	location.replace("orderplaced.html");
}

function sendToAPI(data){

	const options = {
		method: 'POST',
		headers: {
			'Content-type': 'application/json'
		},
		body: JSON.stringify(data)
	};

	fetch('api/placeorder', options);
}

function ID(){
	var customerName = prompt("Please enter your name", "");
	return customerName;
}







