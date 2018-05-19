<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css">
	<title>Manage Elections</title>
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
			<li> <a href = "<s:url action = "elections" />">Create Election</a> </li>
			<li> <a href = "<s:url action = "removeelections" />">Remove Election</a> </li>
			<li> <a href = "<s:url action = "changeelections" />">Change Election</a> </li>
			<li> <a href = "<s:url action = "infoelections" />">Election Info</a> </li>
			<li> <a href = "<s:url action = "detailselections" />">Elections Details</a> </li>
		</ul>
	</div>
	<s:div class = "content">
		<p/>
			<s:form action="createelection" method="post">
				Election Title:<br>
				<s:textfield name="electionBean.title" /><br>
				<br>Start Date:<br>
				<s:textfield placeholder="yyyy/mm/dd hh:mm:ss" name="electionBean.startDate" /><br>
				<br>End Date:<br>
				<s:textfield placeholder="yyyy/mm/dd hh:mm:ss" name="electionBean.endDate" /><br>
				<br>Type:<br>
				<select name="electionBean.type">
	    			<option value="Conselho Geral">Direcao Geral</option>
	    			<option value="Nucleo">Nucleo</option>
				</select><br>
				<br>Members Type:<br>
				<select name="electionBean.membersType">
	    			<option value="Estudante">Estudante</option>
	    			<option value="Docente">Docente</option>
	    			<option value="Funcionario">Funcionario</option>
				</select><br>
				<br>Description:<br>
				<s:textarea name="electionBean.description" /><br><br>
				<s:submit type="button">Create Election</s:submit>
		</s:form>
		<br>
		<s:form action="logout">
			<s:submit type="button">Logout</s:submit>
		</s:form>
	</s:div>
</body>
</html>