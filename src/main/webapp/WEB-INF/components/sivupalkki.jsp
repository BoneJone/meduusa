<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="url" value="${pageContext.request.contextPath}" />
		<aside class="column is-2 aside hero is-fullheight is-hidden-mobile ">
		<div>
			<div class="ylapalkki">
			<article class="media has-text-centered">
							<div class="media-left">
								<figure class="image is-48x48"> <img
									src="${url}/img/bulmantula.png" alt="Image"></figure>
								<br>
							</div>
				<span class="title has-text-centered">Bulmantula</span>	
				</article>
			</div>
			<div class="main">
				<a href="<c:url value="${url }" />/.." class="item active"> <span class="icon"><i
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