<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<!-- ASCII-meduusa source: http://ascii.co.uk/art/jellyfish
                _ -- ~~~ -- _      _______
            .-~               ~-.{__-----. :
          /                       \      | |
         :         O     O         :     | |
         /\                       /------' j
        { {/~-.               .-~\~~~~~~~~~
         \/ /  |~:- .___. -.~\  \ \
        / /\ \ | | { { \ \  } }  \ \
       { {   \ \ |  \ \  \ \ /    } }
        \ \   /\ \   \ \  /\ \   { {
         } } { { \ \  \ \/ / \ \  \ \
        / /   } }  \ \ }{ {    \ \ } }
       / /   { {     \ \{\ \    } { {
      / /     } }     } }\\ \  / / \ \
     `-'     { {     `-'\ \`-'/ /   `-'
              `-'        `-' `-'
-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<title>Meduusa tuntikirjausjärjestelmä</title>
<!-- Fontit ja stylesheetit -->
<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:400,300,600">
<link rel="stylesheet" type="text/css" href="css/normalize.css">
<link rel="stylesheet" type="text/css" href="css/skeleton.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/jquery.toast.min.css">
<!-- jQuery, leanModal-kirjasto ja jQuery toast plugin -->
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.leanModal.min.js"></script>
<script type="text/javascript" src="js/jquery.toast.min.js"></script>
<script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
<!-- jQuery toast pluginin konfiguraatio -->
<script type="text/javascript">
var ilmoitus = function(viesti, tyyppi) {
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

<!-- Container alkaa -->
<div class="container">

<!-- Div tittelille -->
<div class="row toppi">
<div class="twelve columns center">
<h1 lang="fi">Meduusa tuntikirjausjärjestelmä</h1>
</div>
</div>

<!-- Yhteenveto/lisäysrivi alkaa -->
<c:set var="yhteistunnit" value="0"></c:set>
<div class="row">
<div class="eight columns">
<h3>Tiimin jäsenet</h3>
<table class="u-full-width">
<thead>
<tr>
<th>Henkilö</th><th>Tunnit</th>
</tr>
</thead>
<tbody>
<c:forEach items="${tiimintunnit }" var="merkinta">
<c:set var="yhteistunnit" value="${yhteistunnit + merkinta.tunnit }"></c:set>
<tr>
<td>
<a href="<c:url value="/kontrolleri"><c:param name="kayttaja" value="${merkinta.kayttaja.id }"/></c:url>">
<c:out value="${merkinta.kayttaja.etunimi }"></c:out>&nbsp;<c:out value="${merkinta.kayttaja.sukunimi }"></c:out>
</a>
</td>
<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${merkinta.tunnit }"></fmt:formatNumber>h</td>
</tr>
</c:forEach>
<tr>
<td></td>
<td><strong><fmt:formatNumber type="number" maxFractionDigits="2" value="${yhteistunnit }"></fmt:formatNumber>h yhteensä</strong></td>
</tr>
</tbody>
</table>

<c:if test="${not empty naytettavat && naytettavat == 'kayttaja' }"><a class="button" href="<c:url value="/kontrolleri"/>">Näytä kaikki</a><br><br></c:if>

</div>
<!-- Div tuntien lisäys formille -->
<div class="four columns">
<h3 class="center">Lisää merkintä</h3>
<form action="kontrolleri" method="post">
<label for="nimi">Nimesi</label>
<input type="text" name="nimi" id="nimi" class="u-full-width">
<div class="row">
<div class="six columns">
<label for="tunnit">Käytetty aika</label>
<select name="tunnit" id="tunnit" class="u-full-width">
<option value="0">0 tuntia</option>
<option value="1" selected="selected">1 tunti</option>
<option value="2">2 tuntia</option>
<option value="3">3 tuntia</option>
<option value="4">4 tuntia</option>
<option value="5">5 tuntia</option>
<option value="6">6 tuntia</option>
<option value="7">7 tuntia</option>
<option value="8">8 tuntia</option>
<option value="9">9 tuntia</option>
<option value="10">10 tuntia</option>
<option value="11">11 tuntia</option>
<option value="12">12 tuntia</option>
</select>
</div>
<div class="six columns">
<label for="minuutit">&nbsp;</label>
<select name="minuutit" id="minuutit" class="u-full-width">
<option value="0">0 minuuttia</option>
<option value="15">15 minuuttia</option>
<option value="30">30 minuuttia</option>
<option value="45">45 minuuttia</option>
</select>
</div>
</div>
<label for="kuvaus">Mitä teit?</label>
<textarea name="kuvaus" id="kuvaus" class="u-full-width" rows="7"></textarea>
<input type="submit" class="button-primary" value="Tallenna">
<label>
<input type="checkbox" name="slack" value="yes" checked>
<span class="label-body">Lähetä tieto myös Slackiin</span>
</label>
</form>
</div>
</div>
<!-- Content rivi -->
<div class="row">
<!-- Div lisättyjen tuntien näytölle -->
<div class="twelve columns">
<c:choose>
<c:when test="${empty merkinnat }">
<h3>Kirjattuja tunteja ei saatu haettua</h3>
Mahdollisia syitä:<br>
<ul>
<li>Tietokantayhteyttä ei saatu avattua</li>
<li>Tietokanta on tyhjä</li>
<li>Tietokannassa on muuta vikaa</li>
</ul>
</c:when>
<c:when test="${not empty merkinnat && not empty naytettavat && naytettavat == 'kaikki' }">

<c:set var="yhteistunnit" value="0"></c:set>
<h3>Tiimin kirjaamat tunnit</h3>
<table class="u-full-width">
<thead>
<tr><th>Henkilö</th><th>Päivä</th><th>Tunnit</th><th>Kuvaus</th></tr>
</thead>
<tbody>
<c:forEach items="${merkinnat }" var="merkinta">
<tr>
<td><c:out value="${merkinta.kayttaja.etunimi }" />&nbsp;<c:out value="${merkinta.kayttaja.sukunimi }" /></td>
<td><fmt:formatDate value="${merkinta.paivamaara }" pattern="dd.MM.yyyy"/></td>
<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${merkinta.tunnit }"></fmt:formatNumber>h</td>
<td><c:out value="${merkinta.kuvaus }"></c:out></td>
</tr>
<c:set var="yhteistunnit" value="${yhteistunnit + merkinta.tunnit }"></c:set>
</c:forEach>
<tr>
<td></td>
<td></td>
<td colspan="2"><strong><fmt:formatNumber type="number" maxFractionDigits="2" value="${yhteistunnit }"></fmt:formatNumber>h yhteensä</strong></td>
</tr>
</tbody>
</table>

</c:when>
<c:when test="${not empty merkinnat && not empty naytettavat && naytettavat == 'kayttaja' }">
<c:set var="yhteistunnit" value="0"></c:set>
<h3>Henkilön <strong><c:out value="${merkinnat[0].kayttaja.etunimi }"></c:out>&nbsp;<c:out value="${merkinnat[0].kayttaja.sukunimi }"></c:out></strong> merkinnät</h3>
  <div class="row">
  <div class="twelve columns" id="plotly-div"></div>
  </div>
  <script>

   var trace1 = {
		    x: [<c:forEach items="${merkinnat }" varStatus="loop" var="merkinta">'<fmt:formatDate value="${merkinta.paivamaara }" pattern="d.M."/><c:out value="${loop.index}"/>', </c:forEach>],
		    y: [<c:forEach items="${merkinnat }" var="merkinta"><fmt:formatNumber type="number" maxFractionDigits="2" value="${merkinta.tunnit }"></fmt:formatNumber>, </c:forEach>],
		    type: 'bar',
		    hoverinfo: 'none',
		    marker: {
		    	color: '#1EAEDB'
		    }
		};

		var data = [trace1];

		var layout = {
		    showlegend: false,
		    yaxis: {
		        title: 'Tunnit',
		        fixedrange: true
		    },
	    	xaxis: {
	            autorange: 'reversed',
	            fixedrange: true
	    	},
	    	font: {
	    	    family: "Raleway, HelveticaNeue",
	    	    size: 13,
	    	  },
		};
		
		var gd = Plotly.d3.select('#plotly-div').append('div').node();
		
		Plotly.plot(gd, data, layout, {displayModeBar: false}, {displaylogo: false}, {staticPlot: true});
		
		window.onresize = function() {
		    Plotly.Plots.resize(gd);
		};
		$('.xtick text').each(function(i) {
			var initialText = $(this).text();
			$(this).text(initialText.substring(0,5));
		});
  </script>
     
<table class="u-full-width">
<thead>
<tr><th>Päivä</th><th>Tunnit</th><th>Kuvaus</th><th></th></tr>
</thead>
<tbody>
<c:forEach items="${merkinnat }" var="merkinta">
<tr>
<td><fmt:formatDate value="${merkinta.paivamaara }" pattern="dd.MM.yyyy"/></td>
<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${merkinta.tunnit }"></fmt:formatNumber>h</td>
<td><c:out value="${merkinta.kuvaus }"></c:out></td>
<td class="right-align">
<a class="button poisto" href="#modali" onClick="poisto(<c:out value="${merkinta.id }"></c:out>)">Poista</a>
</td>
</tr>
<c:set var="yhteistunnit" value="${yhteistunnit + merkinta.tunnit }"></c:set>
</c:forEach>
<tr>
<td></td>
<td colspan="2"><strong><fmt:formatNumber type="number" maxFractionDigits="2" value="${yhteistunnit }"></fmt:formatNumber>h yhteensä</strong></td>
</tr>
</tbody>
</table>

<!-- Modal poistojen vahvistukselle -->
<div id="modali" class="center">
<h4>Poistetaanko merkintä?</h4>
Merkintä poistetaan tietokannasta lopullisesti.
<br><br>
<button onClick="suljeModal()" class="button left">Säilytä</button> <a id="poistonappi" href="" class="button button-primary right">Poista</a>
</div>

<script type="text/javascript">
  $(".poisto").leanModal();
  var poisto = function(id) {
	  $("#poistonappi").attr("href", "kontrolleri?poista=" + id);
  }
  var suljeModal = function() {
	  $("#lean_overlay").trigger("click");
  }
</script>

</c:when>
</c:choose>
</div>
<!-- Content-rivi loppuu -->
</div>
<!-- Container loppuu -->
</div>

<c:if test="${not empty viesti }">
<!-- Ghetto kikkailua jotta saadaan javascriptille value JSTL:llä -->
<!-- Korvataan JSON:lla myöhemmin -->
<div id="viestidiv" style="display: none;">
<c:out value="${viesti }"></c:out>
</div>
<script type="text/javascript">
ilmoitus($("#viestidiv").html(), "ding dong");
</script>
</c:if>

<!-- Pieni javascripti estämään 0h merkkauksen frontendistä -->
<script type="text/javascript">
$("#tunnit").change(function() {
	if (this.value == 0) {
		$("#minuuutit option[value='15']").prop("selected", true);
		$("#minuutit option[value='0']").remove();
	}
	else if ($("#minuutit option[value='0']").length < 1) {
		$("#minuutit option:first").before('<option value="0">0 minuuttia</option>');
	}
});
</script>

</body>
</html>