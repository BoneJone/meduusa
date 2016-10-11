package me.nicou.tuntikirjaus.kontrollerit;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.nicou.tuntikirjaus.bean.Kayttaja;
import me.nicou.tuntikirjaus.bean.KayttajaImpl;
import me.nicou.tuntikirjaus.bean.Merkinta;
import me.nicou.tuntikirjaus.bean.MerkintaImpl;
import me.nicou.tuntikirjaus.bean.Projekti;
import me.nicou.tuntikirjaus.dao.ProjektiDaoImpl;
import me.nicou.tuntikirjaus.utility.Slack;

@RestController
@RequestMapping("/api")
public class ProjektiRestKontrolleri {
	
	@Autowired
	private ProjektiDaoImpl projektiDao;
	
	private static final Logger logger = LoggerFactory.getLogger(ProjektiRestKontrolleri.class);
	
	@RequestMapping(value = "/projektit", method = RequestMethod.GET)
	public List<Projekti> haeKayttajanProjektit(Principal principal) {
		return projektiDao.haeKayttajanProjektit(principal.getName());
	}
	
	@RequestMapping(value = "/projektit/{projektiId}", method = RequestMethod.GET)
	public Projekti haeProjektinTiedot(@PathVariable Integer projektiId, Principal principal) {
		return projektiDao.haeProjektinTiedot(projektiId, principal.getName());
	}
	
	@RequestMapping(value = "/projektit/{projektiId}/jasen/{kayttajaId}", method = RequestMethod.GET)
	public Projekti haeProjektinTiedotKayttajalta(
			@PathVariable Integer projektiId,
			@PathVariable Integer kayttajaId,
			Principal principal) {
		return projektiDao.haeProjektinTiedotKayttajalta(projektiId, principal.getName(), kayttajaId);
	}
	
	@RequestMapping(value = "/projektit/{projektiId}/yhteistunnit", method = RequestMethod.GET)
	public List<Merkinta> haeProjektinYhteistunnit(@PathVariable Integer projektiId, Principal principal) {
		return projektiDao.haeProjektinYhteistunnit(projektiId, principal.getName());
	}
	
	@RequestMapping(value = "/projekti/{projektiId}/jasen/{kayttajaId}/poista/{merkintaId}", method = RequestMethod.GET)
	public Map<String, String> poistaKayttajanMerkinta(
			@PathVariable Integer projektiId,
			@PathVariable Integer kayttajaId,
			@PathVariable Integer merkintaId,
			Model model,
			Principal principal
			) {
		Map<String, String> vastaus = new HashMap<>();
		int rivit = projektiDao.poistaKayttajanMerkinta(merkintaId, principal.getName());
		if (rivit == 0) {
			vastaus.put("status", "error");
		} else {
			vastaus.put("status", "ok");
		}
		return vastaus;
	}
	
	@RequestMapping(value = "/projektit{projektiId}/lisaa", method = RequestMethod.POST)
	public Map<String, String> lisaaMerkinta(
			@PathVariable Integer projektiId,
			@RequestParam(value = "tunnit", required = true) String tunnit,
			@RequestParam(value = "minuutit", required = true) String minuutit,
			@RequestParam(value = "kuvaus", required = false) String kuvaus,
			@RequestParam(value = "slack", required = false) String slack[],
			Principal principal) {
		
		Map<String, String> vastaus = new HashMap<>();
		
		String viesti = null;

		try {
			int tunnitInt = Integer.parseInt(tunnit);
			int minuutitInt = Integer.parseInt(minuutit);

			if ((tunnitInt >= 0 && tunnitInt < 13 && minuutitInt >= 0 && minuutitInt < 60)
					&& tunnitInt + minuutitInt > 0) {
				double tunnitYht = Double.valueOf(tunnitInt)
						+ (Double.valueOf(minuutitInt) / 60);
				
				if (kuvaus != null) {
					kuvaus = kuvaus.trim();
				}
				
				Kayttaja kayttaja = new KayttajaImpl();
				kayttaja.setSahkoposti(principal.getName());
				
				Merkinta merkinta = new MerkintaImpl();
				merkinta.setKayttaja(kayttaja);
				merkinta.setTunnit(tunnitYht);
				merkinta.setKuvaus(kuvaus);
				
				int rivit = projektiDao.tallennaMerkinta(projektiId, merkinta);
				
				if (rivit == 1) {
					logger.info("Lisättiin " + tunnitYht + "h henkilölle "
							+ principal.getName());

					if (slack != null && slack[0] != null
							&& slack[0].equals("yes") && projektiId == 1) {
						Slack slackbot = new Slack();
						String slackviesti = principal.getName() + " kirjasti " + tunnit + "h";
						if (minuutitInt > 0) {
							slackviesti += " " + minuutit + "min";
						}
						slackviesti += " työtä (" + kuvaus + ")";
						slackbot.lahetaViesti(slackviesti);
					}

					viesti = "Merkintä tallennettu onnistuneesti!";
					vastaus.put("status", "ok");
					
				} else if (rivit == 0) {
					logger.info("Ei onnistunut - rivit = " + rivit);
					viesti = "Merkintää tallentaessa tapahtui virhe!";
				} else {
					logger.info("Merkintää tallentaessa tapahtui ODOTTAMATON virhe!");
					viesti = "Merkintää tallentaessa tapahtui ODOTTAMATON virhe!";
				}
			} else {
				viesti = "Virhe! Tuntien määrä oltava 1-12h";
			}

		} catch (Exception ex) {
			logger.debug("Errori: " + ex);
		}
		
		if (!vastaus.containsKey("status")) { vastaus.put("status", "error"); }
		vastaus.put("viesti", viesti);
		
		return vastaus;
	}

}
