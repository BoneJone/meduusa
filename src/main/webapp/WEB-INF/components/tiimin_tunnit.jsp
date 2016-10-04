<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="url" value="${pageContext.request.contextPath}" />
					<div class="column is-3">
						<div class="box profiilit">
							<p class="profiili-title">Tiimin jäsenet</p>
							<c:forEach items="${tiimintunnit }" var="merkinta">
								<article class="media">
								<div class="media-left">
									<figure class="image is-48x48"> <img
										src="${url}/img/profiili.png" alt="Image"></figure>
								</div>
								<div class="media-content">
									<p class="profiili-nimi">
										<strong>
											<a href="<c:out value="${url}"></c:out>/1/henkilo/<c:out value="${merkinta.kayttaja.id }"></c:out>">
											<c:out value="${merkinta.kayttaja.etunimi }"></c:out>&nbsp;<c:out value="${merkinta.kayttaja.sukunimi }"></c:out>
											</a>
										</strong>
									</p>
								<p class="profiili-tunnit"><fmt:formatNumber type="number" maxFractionDigits="2" value="${merkinta.tunnit }"></fmt:formatNumber>h yhteensä</p>
								</div>
								</article>
							</c:forEach>
						</div>
					</div>