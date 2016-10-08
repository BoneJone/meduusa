package fi.softala.meduusatunnit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.softala.meduusatunnit.bean.Kayttaja;
import fi.softala.meduusatunnit.bean.KayttajaImpl;
import fi.softala.meduusatunnit.bean.Merkinta;
import fi.softala.meduusatunnit.bean.MerkintaImpl;
import fi.softala.meduusatunnit.bean.Projekti;
import fi.softala.meduusatunnit.dao.MerkintaDAO;
import fi.softala.meduusatunnit.utility.Slack;

@Controller
public class TuntiKontrolleriAPI {

	@Inject
	private MerkintaDAO dao;

	public MerkintaDAO getDao() {
		return dao;
	}

	public void setDao(MerkintaDAO dao) {
		this.dao = dao;
	}

	final static Logger logger = LoggerFactory.getLogger(TuntiKontrolleriAPI.class);

	@RequestMapping(value = "/api/kaikki-merkinnat", method = RequestMethod.GET)
	public @ResponseBody List<Merkinta> haeKaikkiMerkinnat() {
		List<Merkinta> merkinnat = dao.haeKaikkiMerkinnat();
		return merkinnat;
	}
	
	@RequestMapping(value = "/api/tiimin-yhteistunnit", method = RequestMethod.GET)
	public @ResponseBody List<Merkinta> haeTiiminYhteistunnit() {
		List<Merkinta> merkinnat = dao.haeTunnitYhteensa();
		return merkinnat;
	}

	@RequestMapping(value = "/api/henkilo/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Merkinta> haeHenkilonMerkinnat(@PathVariable Integer id) {
		List<Merkinta> merkinnat = dao.haeYhdenKayttajanMerkinnat(id);
		return merkinnat;
	}
	
	// Kovakoodattu toistaiseksi käyttämään käyttjä id:tä 1
	@RequestMapping(value = "/api/projekti/{id}", method = RequestMethod.GET)
	public @ResponseBody Projekti haeProjektinMerkinnat(@PathVariable Integer id) {
		Projekti projekti = dao.haeProjektinMerkinnat(id, 1);
		return projekti;
	}
	
	// Kovakoodattu hakemaan käyttäjän id 1 projektit
	@RequestMapping(value = "/api/kayttajan-projektit", method = RequestMethod.GET)
	public @ResponseBody List<Projekti> haeKayttajanProjektit() {
		List<Projekti> projektit = dao.haeKayttajanProjektit(1);
		return projektit;
	}
	
	@RequestMapping(value = "/api/lisaa-merkinta", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> lisaaMerkinta(
			@RequestParam(value = "nimi", required = true) String nimi,
			@RequestParam(value = "tunnit", required = true) String tunnit,
			@RequestParam(value = "minuutit", required = true) String minuutit,
			@RequestParam(value = "kuvaus", required = false) String kuvaus,
			@RequestParam(value = "slack", required = false) String slack) {
		
		// Pitää tehä tälle joku yhtenäinen funktio
		// Tällä hetkellä suora copypaste toisesta kontrollerista
		
		System.out.println("Tultiin apin lisaa-merkintaan");
		
		Map<String, String> vastaus = new HashMap<>();
		
		try {
			// Parsitaan integereiksi, voisi vetästä myös suoraan doubleksi
			int tunnitInt = Integer.parseInt(tunnit);
			int minuutitInt = Integer.parseInt(minuutit);

			// Tehdään pienimuotoinen validointi
			if ((tunnitInt >= 0 && tunnitInt < 13 && minuutitInt >= 0 && minuutitInt < 60)
					&& tunnitInt + minuutitInt > 0) {
				// Lasketaan tuntimäärä tunneista ja minuuteista doubleksi
				double tunnitYht = Double.valueOf(tunnitInt)
						+ (Double.valueOf(minuutitInt) / 60);
				
				if (kuvaus != null) {
					kuvaus = kuvaus.trim();
				}

				// Muodostetaan olio
				Merkinta merkinta = new MerkintaImpl();
				Kayttaja kayttaja = new KayttajaImpl();
				kayttaja.setSahkoposti(nimi);
				merkinta.setKayttaja(kayttaja);
				merkinta.setTunnit(tunnitYht);
				merkinta.setKuvaus(kuvaus);

				// Lähetetään pyyntö DAO-luokkaan
				int rivit = dao.tallennaMerkinta(merkinta);

				// Katsotaan vastaus, näytetään sen mukaan sivulla viesti
				if (rivit == 1) {
					logger.info("Lisättiin " + tunnitYht + "h henkilölle "
							+ nimi + " API:n kautta");

					// Jos Slack-checkbox valittu niin viesti Slackiin
					if (slack != null && slack != null
							&& slack.equals("true")) {
						Slack slackbot = new Slack();
						String slackviesti = nimi + " kirjasti " + tunnit + "h";
						if (minuutitInt > 0) {
							slackviesti += " " + minuutit + "min";
						}
						slackviesti += " työtä (" + kuvaus + ")";
						slackbot.lahetaViesti(slackviesti);
					}

					vastaus.put("status", "ok");
					vastaus.put("viesti", "Merkintä tallennettu onnistuneesti!");

				} else if (rivit == 0) {
					logger.debug("Ei onnistunut - rivit = " + rivit);
					vastaus.put("status", "virhe");
					vastaus.put("viesti", "Merkintää tallentaessa tapahtui virhe!");
				} else {
					logger.debug("Merkintää tallentaessa tapahtui ODOTTAMATON virhe!");
					vastaus.put("status", "virhe");
					vastaus.put("viesti", "Merkintää tallentaessa tapahtui ODOTTAMATON virhe!");
				}
			} else {
				vastaus.put("status", "virhe");
				vastaus.put("viesti", "Virhe! Tuntien määrä oltava 1-12h");
			}

		} catch (Exception ex) {
			vastaus.put("status", "virhe");
			vastaus.put("viesti", "Tuntematon virhe");
			logger.debug("Errori: " + ex);
		}
		
		return vastaus;
	}
	
}
