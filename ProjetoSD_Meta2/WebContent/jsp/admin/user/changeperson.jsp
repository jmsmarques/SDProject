<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css">
	<title>Manage Persons</title>
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
			<li> <a href = "<s:url action = "createusers" />">Create Person</a> </li>
			<li> <a href = "<s:url action = "removeusers" />">Remove Person</a> </li>
			<li> <a href = "<s:url action = "changeusers" />">Change Person</a> </li>
			<li> <a href = "<s:url action = "userselectioninfo" />">Person Election Info</a> </li>
		</ul>
	</div>
	<s:div class = "content">
		<p/>
		<s:form action="changeuser" method="post">
			Person ID:<br>
			<s:textfield name="userBean.ID" /><br>
			<br>New Name:<br>
			<s:textfield name="userBean.name" /><br>
			<br>New Password:<br>
			<s:textfield name="userBean.password" /><br>
			<br>New Type:<br>
			<select name="userBean.type">
    			<option value="Estudante">Estudante</option>
    			<option value="Docente">Docente</option>
    			<option value="Funcionario">Funcionario</option>
			</select><br>
			<br>New Cellphone:<br>
			<s:textfield name="userBean.cellphone" /><br>
			<br>New Address:<br>
			<s:textfield name="userBean.address" /><br>
			<br>New Validity CC:<br>
			<s:textfield placeholder="yyyy/mm/dd hh:mm:ss" name="userBean.validityCC" /><br><br>
			<s:submit type="button">Change User</s:submit>
		</s:form>
		<br>
		<s:form action="logout">
			<s:submit type="button">Logout</s:submit>
		</s:form>
	</s:div>
</body>
</html>