<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="model" class="presentation.web.model.BuyPassTicketModel" scope="request"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BilGes: Comprar Bilhetes Passe</title>
</head>
<body>

	<h2>A sua compra:</h2>
	
	<form action="/bilges-web-client/" method="get">

		<div class="mandatory_field">
			<label for="eventName">Nome do Evento: <input type="text"
				name="eventName" size="50" value="${model.eventName}" readonly /></label>
		</div>

		<div class="mandatory_field">
			<label for="chosenDate">Número de bilhetes passe comprados: <input type="text"
				name="chosenDate" value="${model.numTickets}" readonly /></label>
		</div>

		<div class="mandatory_field">
			<label for="email">O seu Email:</label> <input type="text"
				name="email" size="50" value="${model.emailAddress}" readonly />
		</div>

		<h2>Detalhes da sua reserva:</h2>
			<p> ${model.entity} </p>
			<p> ${model.reference} </p>
			<p> ${model.totalPrice} </p>

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