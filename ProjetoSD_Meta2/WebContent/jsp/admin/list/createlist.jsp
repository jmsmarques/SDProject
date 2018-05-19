<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/style.css">
<title>Manage Lists</title>
</head>
<body>
	<div class="navbar">
		<ul>
			<li><a href="<s:url action = "createusers" />">Manage
					Persons</a></li>
			<li><a href="<s:url action = "createcolleges" />">Manage
					Colleges</a></li>
			<li><a href="<s:url action = "departments" />">Manage
					Department</a></li>
			<li><a href="<s:url action = "elections" />">Manage Election</a>
			</li>
			<li><a href="<s:url action = "lists" />">Manage Lists</a></li>
			<li><a href="<s:url action = "tables" />">Manage Voting
					Tables</a></li>
			<li><a href="<s:url action = "inadvance" />">Vote In Advance</a>
			</li>
			<li> <a href = "<s:url action = "nrvotes" />">Get Connected Users</a> </li>
		</ul>
		<ul>
			<li><a href="<s:url action = "lists" />">Create List</a></li>
			<li><a href="<s:url action = "removelists" />">Remove List</a></li>
			<li><a href="<s:url action = "addpersonlists" />">Add Person
					to List</a></li>
			<li><a href="<s:url action = "removepersonlists" />">Remove
					Person from List</a></li>
		</ul>
	</div>
	<s:div class="content">
		<p />
		<s:form action="createlist" method="post">
			List Name:<br>
			<s:textfield name="listBean.name" />
			<br>
			<br>List Members Type:<br>
			<select name="listBean.membersType">
				<option value="Estudante">Estudante</option>
				<option value="Docente">Docente</option>
				<option value="Funcionario">Funcionario</option>
			</select>
			<br>
			<br>List Election:<br>
			<s:textfield name="listBean.election" />
			<br>
			<br>
			<s:submit type="button">Create Table</s:submit>
		</s:form>
		<br>
		<s:form action="logout">
			<s:submit type="button">Logout</s:submit>
		</s:form>
	</s:div>
</body>
</html>