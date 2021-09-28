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

	<h2>A sua compra:</h2>

	<form action="/bilges-web-client/" method="get">

		<div class="mandatory_field">
			<label for="eventName">Nome do Evento: ${model.eventName}<input
				type="hidden" name="eventName" size="50" value="${model.eventName}"
				readonly /></label>
		</div>

		<div class="mandatory_field">
			<label for="chosenDate">Data do Evento: ${model.chosenDate}<input
				type="hidden" name="chosenDate" size="6" value="${model.chosenDate}"
				readonly /></label>
		</div>

		<div class="mandatory_field">
			<label for="email">O seu Email: ${model.email}</label> <input
				type="hidden" name="email" size="50" value="${model.email}" readonly />
		</div>

		<div class="mandatory_field">
			<label for="chosenTickets">Lugares Escolhidos:</label>

			<c:forEach var="seat" items="${model.chosenSeats}">
				<input type="checkbox" name="chosenTickets" value="${seat}" checked
					disabled>
				<c:out value="${seat}" />
			</c:forEach>

			<label for="notChosenTickets"></label>
			<c:forEach var="seat" items="${model.seatChoices}">
				<input type="checkbox" name="notChosenTickets" value="${seat}" disabled>
				<c:out value="${seat}" />
			</c:forEach>
		</div>

		<h2>Detalhes da sua reserva:</h2>

		<c:forEach var="campo" items="${model.reservation}">
			<p>${campo}</p>
		</c:forEach>

		<div class="button" align="right">
			<input type="submit" value="OK">
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