<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="url" value="${pageContext.request.contextPath}" />
<!-- Modal merkint�jen lis�ykselle -->
<div class="modal" id="merkintamodal">
<form method="post" action="${url}/">
  <div class="modal-background"></div>
  <div class="modal-card">
    <header class="modal-card-head">
      <p class="modal-card-title center">Lis�� tuntimerkint�</p>
      <button class="delete" onClick="piilotaLisaysModal()"></button>
    </header>
    <section class="modal-card-body">

		<label class="label">Nimesi</label>
		<p class="control">
		  <input class="input is-medium" type="text" placeholder="Etunimi" name="nimi">
		</p>
		<label class="label">K�ytetty aika</label>
		<p class="control">
		  <span class="select">
		    <select name="tunnit">
		      <option value="0">0h</option>
		      <option  value="1" selected>1h</option>
		      <option value="2">2h</option>
		      <option value="3">3h</option>
		      <option value="4">4h</option>
		      <option value="5">5h</option>
		      <option value="6">6h</option>
		      <option value="7">7h</option>
		      <option value="8">8h</option>
		      <option value="9">9h</option>
		      <option value="10">10h</option>
		      <option value="11">11h</option>
		      <option value="12">12h</option>
		    </select>
		  </span>
		  <span class="select">
		    <select name="minuutit">
		      <option value="0" selected>0min</option>
		      <option value="15">15min</option>
		      <option value="30">30min</option>
		      <option value="45">45min</option>
		    </select>
		  </span>
		</p>
		<label class="label">Mit� teit?</label>
		<p class="control">
		  <textarea class="textarea" placeholder="Kuvaus" style="resize: none;" name="kuvaus"></textarea>
		</p>
		<p class="control">
		  <label class="checkbox">
		    <input type="checkbox" name="slack" checked>
		    L�het� tieto my�s Slackiin
		  </label>
		</p>


    </section>
    <footer class="modal-card-foot">
      <button type="button" class="button" onClick="piilotaLisaysModal()">Peruuta</button>
      <button type="submit" class="button is-primary">Tallenna merkint�</button>
    </footer>
  </div>
 </form>
</div>

<!-- Modal merkinn�n poistolle -->

<div class="modal" id="poistomodal">
  <div class="modal-background"></div>
  <div class="modal-card">
    <header class="modal-card-head">
      <p class="modal-card-title center">Poistetaanko merkint�?</p>
      <button class="delete" onClick="piilotaPoistoModal()"></button>
    </header>
    <section class="modal-card-body">

	<h3 class="center">Merkint� poistetaan tietokannasta pysyv�sti.</h3>

    </section>
    <footer class="modal-card-foot">
      <button type="button" class="button" onClick="piilotaPoistoModal()">Peruuta</button>
      <a href="" id="poistonappi" class="button is-primary">Poista merkint�</a>
    </footer>
  </div>
</div>