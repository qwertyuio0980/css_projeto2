<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="model" class="presentation.web.model.BuyPassTicketModel" scope="request"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BilGes: Comprar Bilhetes Passe</title>
</head>
<body>

<h2>Indique o numero de bilhetes passe quer comprar e o seu endereço email:</h2>

<p>Para o evento ${model.eventName} que escolheu existem ${model.numMaxTickets} disponíveis para compra.</p>

	<form action="buyPassTicketPurchaseTicket" method="get">

		<div class="mandatory_field">
			<label for="eventName">Nome do Evento: <input type="text"
				name="eventName" size="50" value="${model.eventName}" readonly /></label>
		</div>

		<div class="mandatory_field">
			<label for="numTickets">Número de bilhetes a comprar:</label> <input type="number"
				name="numTickets" min="1" max="${model.numMaxTickets}" value="${model.numTickets}" />
		</div>
		
		<div class="mandatory_field">
			<label for="emailAddress">Indique o seu Email:</label> <input type="text"
				name="emailAddress" size="50" value="${model.emailAddress}" />
		</div>

		<div class="button" align="right">
			<input type="submit" value="Continuar">
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