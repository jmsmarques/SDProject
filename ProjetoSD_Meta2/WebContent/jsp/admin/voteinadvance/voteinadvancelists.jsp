<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css">
	<title>Vote in Advance</title>
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
	</div>
	<s:div class = "content">
		<p/>
		<s:form action="voteinadvancevote" method="post">
			<s:select list="voteInAdvanceBean.lists" name="voteInAdvanceBean.vote" />
			<s:submit type="button">Vote</s:submit>
		</s:form>
		<br>
		<s:form action="backinadvanceelection">
			<s:submit type="button">Back</s:submit>
		</s:form>
		<s:form action="logout">
			<s:submit type="button">Logout</s:submit>
		</s:form>
	</s:div>
</body>
</html>