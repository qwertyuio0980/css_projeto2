<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="model" class="presentation.web.model.BuyIndTicModel"
	scope="request" />

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BilGes: Comprar Bilhetes Individuais</title>

</head>
<body>

	<h2>Indique os lugares desejados:</h2>

	<form action="buyTicketSeatShowReservation" method="post">

		<div class="mandatory_field">
			<label for="eventName">Nome do Evento: ${model.eventName}<input type="hidden"
				name="eventName" size="50" value="${model.eventName}" readonly /></label>
		</div>

		<div class="mandatory_field">
			<label for="chosenDate">Data do Evento: ${model.chosenDate}<input type="hidden"
				name="chosenDate" size="6" value="${model.chosenDate}" readonly /></label>
		</div>

		<div class="mandatory_field">
			<label for="email">Indique o seu Email:</label> <input type="text"
				name="email" size="50" value="${model.email}" />
		</div>

		<div class="mandatory_field">
			<label for="chosenTickets">Lugares Disponíveis:</label>

			<c:forEach var="seat" items="${model.seatChoices}">
				<input type="checkbox" name="chosenTickets" value="${seat}">
				<c:out value="${seat}" />
			</c:forEach>
		</div>

		<div class="button" align="right">
			<input type="submit" value="Comprar">
		</div>

	</form>


	<c:if test="${model.hasMessages}">
		<p>Mensagens</p>
		<ul>
			<c:forEach var="mensagem" items="${model.messages}">
				<li>${mensagem}
			</c:forEach>
		</ul>
	</c:if>
</body>
</html>