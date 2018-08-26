var refreshRate = 2000; //mili seconds
var USER_LIST_URL = buildUrlWithContextPath("gameupdater");
var CHAT_LIST_URL = buildUrlWithContextPath("gameupdater");
var boardsize=0;
var count=0;
//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
function refreshUsersList(players) {
    //clear all current users
    $("#userslist").empty();
    // rebuild the list of users: scan all users and add them to the list of users
    $.each(players || [], function(index, username) {
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
		$("#userslist").append("<h3>"+(index+1)+"."+username+"</h3>").append("<br>");
    });
}

//entries = the added chat strings represented as a single string
function appendToBoardArea(entries) {
	$("#boardprint").empty();
    // add the relevant entries
		
    $.each(entries || [], appendBoardEntry);
}

function appendBoardEntry(index, entry){
    var entryElement = createRoomEntry(entry);
	if (count===0)
		$("#boardprint").append("<tr>")
	else if(count%boardsize==0){
		$("#boardprint").append("</tr><tr>")
	}
	else if(count===boardsize*boardsize){
		$("#boardprint").append("</tr>")
	}
	count=count+1;
    $("#boardprint").append(entryElement);
}

function createRoomEntry (entry){
	if(entry.exposed==="false")
		return $("<span class=\"success\">").append("<td><form action=\"http://localhost:8080/WordiadaWeb/wordiada\" method=\"post\"><Button class=\"button\" style=\"vertical-align: top; height: 30px; width:30px; font-size:12px\" name=\"values\" value= " + entry.i +"_"+ entry.j+ " ></Button></form></td>")
	else if(entry.letter === "*") {//disable button
		console.log("disable tile")
		return $("<span class=\"success\">").append("<td><form action=\"http://localhost:8080/WordiadaWeb/wordiada\" method=\"post\"><Button class=\"button\" style=\"vertical-align: top; height: 30px; width:30px; font-size:12px\" name=\"values\" value= " + entry.i +"_"+ entry.j+ " disabled>*</Button></form></td>")
	}
	else return $("<span class=\"success\">").append("<td><form action=\"http://localhost:8080/WordiadaWeb/wordiada\" method=\"post\"><Button class=\"button\" style=\"vertical-align: top; height: 30px; width:30px; font-size:12px\" name=\"values\" value= " + entry.i +"_"+ entry.j+ " >"+entry.letter+"</Button></form></td>")
}

$(document).ready(function(){
	$(".button").click(function(){
		var boardCordinates = $(this).val();
		var http = new XMLHttpRequest();
		var url = "http://localhost:8080/WordiadaWeb/wordiada";
		var params = "values="+boardCordinates;
		console.log("in button class trying " + boardCordinates)
		http.open("POST", url, true);

		//Send the proper header information along with the request
		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		http.send(params);
		
	});
});
	
$(document).ready(function(){
	$(".quitgame").click(function(){
		var quitgame = $(this).val();
		var http = new XMLHttpRequest();
		var url = "http://localhost:8080/WordiadaWeb/wordiada";
		var params = "values="+quitgame	;
		console.log("in button class trying " + quitgame)
		http.open("POST", url, true);

		//Send the proper header information along with the request
		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		http.send(params);
		window.location = "../RoomsPage/Rooms.html";
		
	});
});

	

//call the server and get the chat version
//we also send it the current chat version so in case there was a change
//in the chat content, we will get the new string as well
function ajaxBoardContent() {
    $.ajax({
        url: CHAT_LIST_URL,
        dataType: 'json',
        success: function(data) {
			boardsize=data.boardsize;
            appendToBoardArea(data.boadprint);
			refreshUsersList(data.players);
			appendMassage(data.massage)
			appendWordComposed(data.wordcomposed)
			triggerajaxBoardContent();
        },
		error: function(data){
			triggerajaxBoardContent();
		}
    });
}

function appendMassage(massage){
	$("#massage").empty();
	$("#massage").append("<h3>"+"Massage: " + massage+"</h3>")
}

function appendWordComposed(wordcomposed){
	$("#wordcomposed").empty();
	$.each(wordcomposed || [], function(index, word){
		$("#wordcomposed").append("<h3>"+(index+1)+"."+word+"</h3>").append("<br>");
	});
	
}

//add a method to the button in order to make that form use AJAX
//and not actually submit the form
$(function() { // onload...do
    //add a function to the submit event
    $("#chatform").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function() {
                console.error("Failed to submit");
            },
            success: function(r) {
                //do not add the user string to the chat area
                //since it's going to be retrieved from the server
                //$("#result h1").text(r);
            }
        });

        $("#userstring").val("");
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});

function triggerajaxBoardContent() {
    setTimeout(ajaxBoardContent, refreshRate);
}

//activate the timer calls after the page is loaded
$(function() {

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});
    
    //The chat content is refreshed only once (using a timeout) but
    //on each call it triggers another execution of itself later (1 second later)
    triggerajaxBoardContent();
});


