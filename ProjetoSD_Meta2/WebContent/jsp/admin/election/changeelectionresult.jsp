<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
		<c:out value = "${electionBean.electionChange}" />
		<s:form action="logout">
			<s:submit type="button">Logout</s:submit>
		</s:form>
	</s:div>
</body>
</html>