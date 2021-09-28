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

	<h2>Indique as datas do evento para qual quer comprar bilhetes:</h2>

	<form action="buyTicketSeatChoiceSetDate" method="get">

		<div class="mandatory_field">
			<label for="eventName">Nome do Evento: ${model.eventName}<input type="hidden"
				name="eventName" size="50" value="${model.eventName}" readonly/></label>
		</div>

		<div class="mandatory_field">
			<label for="data">Datas Disponíveis:</label>
			<select name="chosenDate"> 
				<c:forEach var="data" items="${model.eventDates}">
					<c:choose>
						<c:when test="${model.chosenDate == data}">
							<option selected="selected" value="${data}">${model.chosenDate}</option>
						</c:when>
						<c:otherwise>
							<option value="${data}">${data}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
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