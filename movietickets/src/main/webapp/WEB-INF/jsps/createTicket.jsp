<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Create Ticket</title>
</head>
<body>
	<form action="createTicket" method="post">
		Movie Name: <input type="text" name="movieName" />
		Screen: <input type="text" name="screenNo" />
		Seat Number: <input type="text" name="seatNo" />
		<input type="submit" value="purchase" />
	</form>
${msg}
</body>
</html>