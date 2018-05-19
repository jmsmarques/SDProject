<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css">
	<script type="text/javascript">

        var websocket = null;
        
        window.onload = function() { // URI = ws://10.16.0.165:8080/WebSocket/ws
            connect('ws://' + window.location.host + '/ProjetoSD_Meta2/ws');
            document.getElementById("chat").focus();
        }

        function connect(host) { // connect to the host websocket
            if ('WebSocket' in window)
                websocket = new WebSocket(host);
            else if ('MozWebSocket' in window)
                websocket = new MozWebSocket(host);
            else {
                writeToHistory('Get a real browser which supports WebSocket.');
                return;
            }

            websocket.onopen    = onOpen; // set the event listeners below
            websocket.onclose   = onClose;
            websocket.onmessage = onMessage;
            websocket.onerror   = onError;
        }

        function onOpen(event) {
            writeToHistory('Connected to ' + window.location.host + '.');
            sendInfo();
        }
        
        function onClose(event) {
            writeToHistory('WebSocket closed.');
        }
        
        function onMessage(message) { // print the received message
            writeToHistory(message.data);
        }
        
        function onError(event) {
            writeToHistory('WebSocket error (' + event.data + ').');
        }

        function writeToHistory(text) {
            var history = document.getElementById('history');
            history.innerHTML = text;
        }
        
        function sendInfo() {
        	var res = 'tables'.concat(':');
        	websocket.send(res);
        }

    </script>
	<title>Manage Tables</title>
</head>
<body>
	<div class = "navbar"> 
		<ul>
			<li> <a href = "<s:url action = "createusers" />">Manage Persons</a> </li>
			<li> <a href = "<s:url action = "createcolleges" />">Manage Colleges</a> </li>
			<li> <a href = "<s:url action = "departments" />">Manage Department</a> </li>
			<li> <a href = "<s:url action = "elections" />">Manage Election</a> </li>
			<li> <a href = "<s:url action = "lists" />">Manage Lists</a> </li>
			<li> <a href = "<s:url action = "tables" />">Manage Voting Tables</a> </li>
			<li> <a href = "<s:url action = "inadvance" />">Vote In Advance</a> </li>
			<li> <a href = "<s:url action = "nrvotes" />">Get Connected Users</a> </li>
		</ul>
		<ul>
			<li> <a href = "<s:url action = "tables" />">Create Table</a> </li>
			<li> <a href = "<s:url action = "removetables" />">Remove Table</a> </li>
			<li> <a href = "<s:url action = "addpersontables" />">Add Person to Table</a> </li>
			<li> <a href = "<s:url action = "removepersontables" />">Remove Person from Table</a> </li>
			<li> <a href = "<s:url action = "tableinfo" />">Table Info</a> </li>
		</ul>
	</div>
	<s:div class = "content">
		<p/>
		<div id="history"></div>
		<br>
		<s:form action="logout">
			<s:submit type="button">Logout</s:submit>
		</s:form>
	</s:div>
</body>
</html>