<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Average</title>
</head>
<body>
<%
int result = (Integer) request.getAttribute("result");
out.println("Average of two numbers is: " + result);
%>
</body>
</html>