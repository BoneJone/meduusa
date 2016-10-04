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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script type="text/javascript" src="${url}/js/jquery.toast.min.js"></script>
<script>
var naytaLisaysModal = function() {
	$('#merkintamodal').addClass('is-active');
};
var piilotaLisaysModal = function() {
	$('#merkintamodal').removeClass('is-active');
};

var piilotaPoistoModal = function() {
	$('#poistomodal').removeClass('is-active');
}

var poistaMerkinta = function(id) {
	$("#poistonappi").attr("href", "${url}/1/poista/" + id);
	$('#poistomodal').addClass('is-active');
};
</script>
<!-- jQuery toast pluginin konfiguraatio -->
<script type="text/javascript">
var ilmoitus = function(viesti) {
	$.toast({
		text: viesti,
		heading: '<strong>Ilmoitus</strong>',
		showHideTransition: 'fade',
		allowToastClose: true,
		hideAfter: 3000,
		stack: 5,
		position: 'top-right',
		bgColor: '#1EAEDB',
		textColor: '#FFF',
		textAlign: 'left',
		loader: false,
		});
	}
</script>
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
			
			<!-- Tuntimerkinnän lisäys modal -->
			<jsp:include page="../components/merkinnan_lisays_modal.jsp" />

		</div>

	</div>
	
	<!-- Footer -->
	
</body>
</html>