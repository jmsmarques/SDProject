<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/style.css">
<title>iVOTAS</title>
</head>
<body>
	<div class="navbar">
		<ul>
			<li><a href="<s:url action = "voterinfo" />">Dados</a></li>
			<li><a href="<s:url action = "votervote" />">Votar</a></li>
			<li><a href="<s:url action = "voterabout" />">About</a></li>

			<li><a href="<s:url action = "voterlogout" />">LOGOUT</a></li>

		</ul>
	</div>
	<s:div class="content">
		<p>UPS!</p>
		<p>J� existe um utilizador associado a este facebook</p>
	</s:div>

</body>
</html>