<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css">
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
			<s:form action="addpersontable" method="post">
				Person ID:<br>
				<s:textfield name="tableBean.ID" /><br>
				<br>Department:<br>
				<s:textfield name="tableBean.department" /><br>
				<br>Election:<br>
				<s:textfield name="tableBean.election" /><br><br>
				<s:submit type="button">Add</s:submit>
		</s:form>
		<br>
		<s:form action="logout">
			<s:submit type="button">Logout</s:submit>
		</s:form>
	</s:div>
</body>
</html>