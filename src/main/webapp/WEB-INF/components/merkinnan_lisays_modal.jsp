<div class="modal is-active" id="merkinta-modal">
  <div class="modal-background"></div>
  <div class="modal-card">
    <header class="modal-card-head">
      <p class="modal-card-title center">Lisää tuntimerkintä</p>
      <button class="delete"></button>
    </header>
    <section class="modal-card-body">

		<label class="label">Nimesi</label>
		<p class="control">
		  <input class="input is-medium" type="text" placeholder="Etunimi">
		</p>
		<label class="label">Käytetty aika</label>
		<p class="control">
		  <span class="select">
		    <select>
		      <option>0h</option>
		      <option selected>1h</option>
		      <option>2h</option>
		      <option>3h</option>
		      <option>4h</option>
		      <option>5h</option>
		      <option>6h</option>
		      <option>7h</option>
		      <option>8h</option>
		      <option>9h</option>
		      <option>10h</option>
		      <option>11h</option>
		      <option>12h</option>
		    </select>
		  </span>
		  <span class="select">
		    <select>
		      <option selected>0min</option>
		      <option>15min</option>
		      <option>30min</option>
		      <option>45min</option>
		    </select>
		  </span>
		</p>
		<label class="label">Mitä teit?</label>
		<p class="control">
		  <textarea class="textarea" placeholder="Kuvaus" style="resize: none;"></textarea>
		</p>


    </section>
    <footer class="modal-card-foot">
      <a class="button">Peruuta</a>
      <a class="button is-primary">Tallenna merkintä</a>
    </footer>
  </div>
</div>