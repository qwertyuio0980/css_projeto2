<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="model" class="presentation.web.model.BuyPassTicketModel" scope="request"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BilGes: Comprar Bilhetes Passe</title>
</head>
<body>

<h2>Indique o evento para qual quer comprar bilhetes-passe:</h2>

	<form action="buyPassTicketInsertEventName" method="get">

		<!-- Campo para preencher nome de evento -->
		<div class="mandatory_field">
			<label for="eventName">Nome do Evento:</label> <input type="text"
				name="eventName" size="50" value="${model.eventName}" />
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