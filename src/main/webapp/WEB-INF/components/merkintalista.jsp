<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
					<div class="column is-8">
						<div class="box tunnit">
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
								<small style="float:right;"><span style="color: #CCC;"><a href="#!" onClick="poistaMerkinta(<c:out value="${merkinta.id }"></c:out>)">Poista</a> | </span><fmt:formatDate value="${merkinta.paivamaara }" pattern="dd.MM.yyyy"/></small>
								<br>
								<c:out value="${merkinta.kayttaja.etunimi }" />&nbsp;<c:out value="${merkinta.kayttaja.sukunimi }" />
								</p>
								</div>
								</div>
							</article>
						</c:forEach>
						</div>
						
						<!-- Paginaatio -->
						<jsp:include page="merkintalista_paginaatio.jsp" />
						
					</div>