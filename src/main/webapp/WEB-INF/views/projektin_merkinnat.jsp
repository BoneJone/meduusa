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
<link rel="stylesheet" type="text/css" href="css/bulma.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<script src="https://use.fontawesome.com/80f171c042.js"></script>
</head>
<body>
	<div class="columns">
		<aside class="column is-2 aside hero is-fullheight is-hidden-mobile ">
		<div>
			<div class="ylapalkki">
			<article class="media has-text-centered">
							<div class="media-left">
								<figure class="image is-48x48"> <img
									src="img/bulmantula.png" alt="Image"></figure>
								<br>
							</div>
				<span class="title has-text-centered">Bulmantula</span>	
				</article>
			</div>
			<div class="main">
				<a href="#" class="item active"> <span class="icon"><i
						class="fa fa-home"></i></span> <span class="name">Etusivu</span>
				</a> <a href="#" class="item"> <span class="icon"><i
						class="fa fa-clock-o"></i></span> <span class="name">Tunnit</span>
				</a> <a href="#" class="item"> <span class="icon"><i
						class="fa fa-folder"></i></span> <span class="name">Projektit</span>
				</a> <a href="#" class="item"> <span class="icon"><i
						class="fa fa-user"></i></span> <span class="name">Profiili</span>
				</a> <a href="#" class="item"> <span class="icon"><i
						class="fa fa-line-chart "></i></span> <span class="name">Stats</span>
				</a>
			</div>

		</div>
		</aside>
		<div class="content column is-10">
			<div class="nav has-shadow ylapalkki">
				<div class="nav-left">
					<p class="fa-3">Meduusa tuntikirjaus</p>
				</div>
				<div class="nav-right">
					<span class="nav-item"> 
					<a class="button is-primary lisaaNappi" href="#"> <span class="icon"> <i class="fa fa-plus-circle "></i></span> <span><strong>Lisää merkintä</strong></span>
					</a>
					</span>
				</div>
			</div>
			<div class="container sisalto">
				<div class="columns">
					<div class="column is-3">
						<div class="box profiilit">
							<p class="profiili-title">Tiimin jäsenet</p>
							<c:forEach items="${tiimintunnit }" var="merkinta">
							<article class="media">
							<div class="media-left">
								<figure class="image is-48x48"> <img
									src="img/profiili.png" alt="Image"></figure>
							</div>
							<div class="media-content">
							<p class="profiili-nimi"><strong><a href="#!"><c:out value="${merkinta.kayttaja.etunimi }"></c:out>&nbsp;<c:out value="${merkinta.kayttaja.sukunimi }"></c:out></a></strong></p>
							<p class="profiili-tunnit"><fmt:formatNumber type="number" maxFractionDigits="2" value="${merkinta.tunnit }"></fmt:formatNumber>h yhteensä</p>
							</div>
							</article>
							</c:forEach>
						</div>
					</div>
					<div class="column is-8">
						<div class="box tunnit">
						<!-- Listataan tässä demossa 10 edellisintä entryä -->
						<c:forEach items="${merkinnat }" var="merkinta" varStatus="loop">
						<c:set var="tuntiMaara" value="${merkinta.tunnit - (merkinta.tunnit % 1)}"></c:set>
						<c:set var="minuuttiMaara" value="${(merkinta.tunnit % 1) * 60}"></c:set>
							<article class="merkinta media">
								<div class="media-left">
								<div class="tuntiTulostus">
									<p>
										<strong>
											<fmt:formatNumber type="number" maxFractionDigits="0" value="${tuntiMaara }" />h
										</strong>
									</p>
								<p>
								<c:if test="${minuuttiMaara > 0 }">
									<small>
										<fmt:formatNumber type="number" maxFractionDigits="0" value="${minuuttiMaara }" />min
									</small>
								</c:if>
								</p>
								</div>
									</div>
								<div class="media-content">
								<div class="merkinnat content">
								<p>
								<strong><c:out value="${merkinta.kuvaus }"></c:out></strong>
								<small style="float:right;"><fmt:formatDate value="${merkinta.paivamaara }" pattern="dd.MM.yyyy"/></small>
								<br>
								<c:out value="${merkinta.kayttaja.etunimi }" />&nbsp;<c:out value="${merkinta.kayttaja.sukunimi }" />
								</p>
								</div>
								</div>
							</article>
						</c:forEach>
						</div>
						
						<nav class="pagination">
						  <a class="button" disabled>Edellinen sivu</a>
						  <a class="button" href="#!">Seuraava sivu</a>
						  <ul>
						    <li>
						      <a class="button" href="#!">1</a>
						    </li>
						    <li>
						      <a class="button" href="#!">2</a>
						    </li>
						    <li>
						      <a class="button" href="#!">3</a>
						    </li>
						    <li>
						      <a class="button" href="#!">4</a>
						    </li>
						    <li>
						      <a class="button" href="#!">5</a>
						    </li>
						  </ul>
						</nav>
						
					</div>
				</div>
			</div>


		</div>

	</div>

</body>
</html>