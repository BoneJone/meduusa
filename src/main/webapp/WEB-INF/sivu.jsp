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
<link href="//fonts.googleapis.com/css?family=Raleway:400,300,600" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="css/normalize.css">
<link rel="stylesheet" href="css/skeleton.css">
<link rel="stylesheet" href="css/style.css">
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
<c:set var="yhteistunnit" value="0"></c:set>
<c:forEach items="${kayttajat }" var="kayttaja">
<c:set var="yhteistunnit" value="${yhteistunnit + kayttaja.tunnitYhteensa }"></c:set>
<tr>
<td>
<a href="<c:url value="/kontrolleri"><c:param name="kayttaja" value="${kayttaja.id }"/></c:url>">
<c:out value="${kayttaja.etunimi }"></c:out>&nbsp;<c:out value="${kayttaja.sukunimi }"></c:out>
</a>
</td>
<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${kayttaja.tunnitYhteensa }"></fmt:formatNumber>h</td>
</tr>
</c:forEach>
<tr>
<td></td>
<td><strong><fmt:formatNumber type="number" maxFractionDigits="2" value="${yhteistunnit }"></fmt:formatNumber>h yhteensä</strong></td>
</tr>
</tbody>
</table>

<c:if test="${not empty kayttaja }"><a class="button" href="<c:url value="/kontrolleri"/>">Näytä kaikki</a></c:if>

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
<option value="1">1 tunti</option>
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
<c:when test="${empty merkinnat && empty kayttaja }">
<h3>Kirjattuja tunteja ei saatu haettua</h3>
Mahdollisia syitä:<br>
<ul>
<li>Tietokantayhteyttä ei saatu avattua</li>
<li>Tietokanta on tyhjä</li>
<li>Tietokannassa on muuta vikaa</li>
</ul>
</c:when>
<c:when test="${not empty merkinnat }">
<h3>Tiimin kirjaamat tunnit</h3>
<table class="u-full-width">
<thead>
<tr><th>Henkilö</th><th>Päivä</th><th>Tunnit</th><th>Kuvaus</th></tr>
</thead>
<tbody>
<c:forEach items="${merkinnat }" var="merkinta">
<tr>
<td><c:out value="${merkinta.kayttajanNimi }" /></td>
<td><fmt:formatDate value="${merkinta.paivamaara }" pattern="dd.MM.yyyy"/></td>
<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${merkinta.tunnit }"></fmt:formatNumber>h</td>
<td><c:out value="${merkinta.kuvaus }"></c:out></td>
</tr>
</c:forEach>
<tr>
<td></td>
<td></td>
<td colspan="2"><strong><fmt:formatNumber type="number" maxFractionDigits="2" value="${yhteistunnit }"></fmt:formatNumber>h yhteensä</strong></td>
</tr>
</tbody>
</table>
</c:when>
<c:when test="${not empty kayttaja }">
<h3>Henkilön <strong><c:out value="${kayttaja.etunimi }"></c:out>&nbsp;<c:out value="${kayttaja.sukunimi }"></c:out></strong> merkinnät</h3>
<table class="u-full-width">
<thead>
<tr><th>Päivä</th><th>Tunnit</th><th>Kuvaus</th></tr>
</thead>
<tbody>
<c:forEach items="${kayttaja.merkinnat }" var="merkinta">
<tr>
<td><fmt:formatDate value="${merkinta.paivamaara }" pattern="dd.MM.yyyy"/></td>
<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${merkinta.tunnit }"></fmt:formatNumber>h</td>
<td><c:out value="${merkinta.kuvaus }"></c:out></td>
</tr>
</c:forEach>
<tr>
<td></td>
<td colspan="2"><strong><fmt:formatNumber type="number" maxFractionDigits="2" value="${kayttaja.tunnitYhteensa }"></fmt:formatNumber>h yhteensä</strong></td>
</tr>
</tbody>
</table>
</c:when>
</c:choose>
</div>
<!-- Content-rivi loppuu -->
</div>
<!-- Container loppuu -->
</div>

</body>
</html>