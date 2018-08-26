var refreshRate = 2000; //mili seconds
var USER_LIST_URL = buildUrlWithContextPath("playerAndRoomlist");
var CHAT_LIST_URL = buildUrlWithContextPath("playerAndRoomlist");
var currRoomID;
var boardsize;
var count = 0;
//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
function refreshUsersList(players) {
    //clear all current users
    $("#userslist").empty();
    // rebuild the list of users: scan all users and add them to the list of users
    $.each(players || [], function(index, username) {
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
		$("#userslist").append("<h3>"+username+"</h3>").append("<br>");
    });
}

//entries = the added chat strings represented as a single string
function appendToRoomArea(entries) {
	$("#roomsarea").empty();
    // add the relevant entries
    $.each(entries || [], appendRoomEntry);
}

function appendRoomEntry(index, entry){
    var entryElement = createRoomEntry(entry);
    $("#roomsarea").append(entryElement).append("<br>");
}

function createRoomEntry (entry){
	return $("<span class=\"success\">").append("<form accept-charset=\"UTF-8\" action=\"http://localhost:8080/WordiadaWeb/secondpage\" autocomplete=\"off\" method=\"GET\">" +
			"<input type=\"text\" name=\"room\" value=\""+entry.username+"\">" +
			"<button class=\"button\" type=\"submit\"  height=\"20px\" width=\"20px\" position=\"absolute\" top=\"50%\">"+
				"<div id=\"images\" height=\"300px\" width=\"250px\">"+	
					"<div id=\"text\">"+
							"Game Status: " + entry.gameStatus+"</br>\n"+
							"Game Title: " + entry.gameTitle+"</br>\n"+
							"User Name: " + entry.username+"</br>\n"+
                            "User Type: " + entry.type+"</br>\n"+
                            "Board Size: " + entry.boardSize +"</br>\n"+
							"Players: " + entry.playersLimit+"\\"+ entry.playersInGame +"</br>\n"+
                            "Dictionary Name: " + entry.dictionaryName +"</br>\n"+
							"Spare-Tile Size: " + entry.spareTileSize +"</br>\n"+
					"</div>"+
				"</div>" +
			"</button>" +
		"</form>" +
        "</br>")
}

function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function(data) {
            refreshUsersList(data.players);
        }
    });
}


//call the server and get the chat version
//we also send it the current chat version so in case there was a change
//in the chat content, we will get the new string as well
function ajaxRoomsContent() {
    $.ajax({
        url: CHAT_LIST_URL,
        dataType: 'json',
        success: function(data) {
            appendToRoomArea(data.entries);
			triggerajaxRoomsContent()
        },
		error: function(data){
			triggerajaxRoomsContent()
		}
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

function triggerajaxRoomsContent() {
    setTimeout(ajaxRoomsContent, refreshRate);
}

//activate the timer calls after the page is loaded
$(function() {

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
	setInterval(ajaxUsersList, refreshRate);
    
    //The chat content is refreshed only once (using a timeout) but
    //on each call it triggers another execution of itself later (1 second later)
    triggerajaxRoomsContent();
});