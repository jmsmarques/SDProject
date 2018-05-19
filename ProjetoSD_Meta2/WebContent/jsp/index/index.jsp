<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login!</title>
</head>
<body>
	<s:div class = "content">
		<s:form action="login" method="post">
			<s:text name="Username:" />
			<s:textfield name="username" /><br>
			<p/>
			<s:text name="Password:" />
			<s:textfield type="password" name="password" /><br>
			<s:submit type = "button">Login</s:submit>
		</s:form>
	</s:div>
	<s:form action="voterfacelogin">

			<s:submit type="button" value="#i" name="">LOGIN COM FACEBOOK</s:submit>
		</s:form>
</body>
</html>