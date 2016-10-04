<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Meduusa | Tuntikirjaus</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="url" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" type="text/css" href="${url}/css/bulma.css">
<link rel="stylesheet" type="text/css" href="${url}/css/style.css">
<script src="https://use.fontawesome.com/80f171c042.js"></script>
</head>
<body>
	<div class="columns">
	<!-- Sivupalkin importtaus -->
	<jsp:include page="../components/sivupalkki.jsp" />
	
		<div class="content column is-10">
		<!-- Yläpalkki -->
		<jsp:include page="../components/ylapalkki.jsp" />
			
			<!-- Sisältö alkaa -->
			<div class="container sisalto">
				<div class="columns">
				
					<!-- Tiimin jäsenet / yhteenveto -->
					<jsp:include page="../components/tiimin_tunnit.jsp" />
					
					<!-- Projektin tuntien listaus -->
					<jsp:include page="../components/merkintalista.jsp" />
					
				</div>
			</div>


		</div>

	</div>
	
	<!-- Footer -->


</body>
</html>