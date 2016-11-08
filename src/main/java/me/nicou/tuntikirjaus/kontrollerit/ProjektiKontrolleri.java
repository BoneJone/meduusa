package me.nicou.tuntikirjaus.kontrollerit;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;
import javax.inject.Inject;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import me.nicou.tuntikirjaus.bean.EtusivunMerkinta;
import me.nicou.tuntikirjaus.bean.Kayttaja;
import me.nicou.tuntikirjaus.bean.KayttajaImpl;
import me.nicou.tuntikirjaus.bean.Merkinta;
import me.nicou.tuntikirjaus.bean.MerkintaImpl;
import me.nicou.tuntikirjaus.bean.Projekti;
import me.nicou.tuntikirjaus.bean.ProjektiImpl;
import me.nicou.tuntikirjaus.dao.KayttajaDaoImpl;
import me.nicou.tuntikirjaus.dao.ProjektiDaoImpl;
import me.nicou.tuntikirjaus.utility.Slack;

@Controller
public class ProjektiKontrolleri {
	
	@Autowired
	@Qualifier("projektiDao")
	private ProjektiDaoImpl projektiDao;
	
	@Autowired
	@Qualifier("kayttajaDao")
	private KayttajaDaoImpl kayttajaDao;
	
	private static final Logger logger = LoggerFactory.getLogger(ProjektiKontrolleri.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String haeKayttajanProjektit(Model model, Principal principal) {
		Kayttaja kayttaja = kayttajaDao.haeKayttajaSahkopostilla(principal.getName());
		List<Projekti> projektit = projektiDao.haeKayttajanProjektit(principal.getName());
		List<EtusivunMerkinta> merkinnat = kayttajaDao.haeEtusivunMerkinnat(principal.getName());
		double kuukaudenYhteistunnit = kayttajaDao.haeKayttajanKuukaudenYhteistunnit(principal.getName());
		model.addAttribute("kayttaja", kayttaja);
		model.addAttribute("projektit", projektit);
		model.addAttribute("merkinnat", merkinnat);
		model.addAttribute("yhteistunnit", kuukaudenYhteistunnit);
		model.addAttribute("active", "yleiskatsaus");
	return "yleiskatsaus";
	}
	
	@RequestMapping(value = "/projekti/{id}", method = RequestMethod.GET)
	public String haeProjektinTiedot(
			@PathVariable Integer id,
			@RequestParam(value = "p", required = false) Integer sivunumero,
			Model model,
			Principal principal) {
		
		if (sivunumero == null) {
			sivunumero = 1;
		} else if (sivunumero < 1) {
			sivunumero = 1;
		}
		
		Projekti projekti = projektiDao.haeProjektinTiedot(id, principal.getName(), sivunumero);
		logger.debug(projekti.toString());
		if (projekti.getId() == 0) { return "redirect:/"; }
		Kayttaja kayttaja = kayttajaDao.haeKayttajaSahkopostilla(principal.getName());
		List<Merkinta> yhteistunnit = projektiDao.haeProjektinJasenet(id, principal.getName());
		model.addAttribute("kayttaja", kayttaja);
		model.addAttribute("projekti", projekti);
		model.addAttribute("yhteistunnit", yhteistunnit);
		
		// @TODO: Keksitään fiksumpi tapa, mutta nyt pitää includee nää joka hakuun
		// jotta saadaan sidebariin projektit näkyviin
		List<Projekti> projektit = projektiDao.haeKayttajanProjektit(principal.getName());
		model.addAttribute("projektit", projektit);
		
		return "projekti";
	}
	
	@RequestMapping(value = "/projekti/{projektiId}/jasen/{kayttajaId}", method = RequestMethod.GET)
	public String haeProjektinTiedotKayttajalta(
			@PathVariable Integer projektiId,
			@PathVariable Integer kayttajaId,
			@RequestParam(value = "p", required = false) Integer sivunumero,
			Model model,
			Principal principal) {
		
		if (sivunumero == null) {
			sivunumero = 1;
		} else if (sivunumero < 1) {
			sivunumero = 1;
		}
		
		Projekti projekti = projektiDao.haeProjektinTiedotKayttajalta(projektiId, principal.getName(), kayttajaId, sivunumero);
		if (projekti.getId() == 0) { return "redirect:/"; }
		Kayttaja kayttaja = kayttajaDao.haeKayttajaSahkopostilla(principal.getName());
		List<Merkinta> yhteistunnit = projektiDao.haeProjektinJasenet(projektiId, principal.getName());
		model.addAttribute("kayttaja", kayttaja);
		model.addAttribute("projekti", projekti);
		model.addAttribute("yhteistunnit", yhteistunnit);
		
		// @TODO: Keksitään fiksumpi tapa, mutta nyt pitää includee nää joka hakuun
		// jotta saadaan sidebariin projektit näkyviin
		List<Projekti> projektit = projektiDao.haeKayttajanProjektit(principal.getName());
		model.addAttribute("projektit", projektit);
		
		return "jasenen-merkinnat";
	}
	
	@RequestMapping(value = "/projekti/{projektiId}/jasen/{kayttajaId}/poista/{merkintaId}", method = RequestMethod.GET)
	public String poistaKayttajanMerkinta(
			@PathVariable Integer projektiId,
			@PathVariable Integer kayttajaId,
			@PathVariable Integer merkintaId,
			@RequestParam(value = "p", required = false) Integer sivunumero,
			Model model,
			Principal principal
			) {
		
		if (sivunumero == null) {
			sivunumero = 1;
		} else if (sivunumero < 1) {
			sivunumero = 1;
		}
		
		int rivit = projektiDao.poistaKayttajanMerkinta(merkintaId, principal.getName());
		if (rivit == 0) {
			model.addAttribute("viesti", "Merkinnän poistossa tapahtui virhe!");
		} else {
			model.addAttribute("viesti", "Merkintä poistettu!");
		}
		
		return "redirect:/projekti/" + projektiId + "/jasen/" + kayttajaId;
	}
	
	@RequestMapping(value = "projekti/{projektiId}/lisaajasen", method = RequestMethod.POST)
	public String lisaaProjektiinJasen(
			Model model,
			@PathVariable Integer projektiId,
			@RequestParam(value = "sahkoposti", required = true) String sahkoposti,
			Principal principal, @Valid Kayttaja kayttaja, BindingResult result
			) {
				
		if (result.hasErrors()) {
			logger.info("Käyttäjän " + sahkoposti + " lisäys projektiin " + projektiId + " ei onnistunut");
			return "redirect:/projekti/" + projektiId;
		} else {
			boolean success = projektiDao.lisaaJasenProjektiin(projektiId, sahkoposti.trim(), principal.getName());
			
			if (success) {
				logger.info("Käyttäjä " + sahkoposti + " lisätty projektiin " + projektiId);
			} else {
				logger.info("Käyttäjän " + sahkoposti + " lisäys projektiin " + projektiId + " ei onnistunut");
			}
			return "redirect:/projekti/" + projektiId;
		}
	}
	
	@RequestMapping(value = "/projekti/lisaamerkinta", method = RequestMethod.POST)
	public String lisaaMerkinta(
			Model model,
			@RequestParam(value = "id", required = true) Integer projektiId,
			@RequestParam(value = "tunnit", required = true) String tunnit,
			@RequestParam(value = "minuutit", required = true) String minuutit,
			@RequestParam(value = "kuvaus", required = false) String kuvaus,
			@RequestParam(value = "slack", required = false) String slack[],
			Principal principal) {
		
		logger.info("Tultiin kirjaamaan tuntimerkintää");
		
		String viesti = null;

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
				
				Kayttaja kayttaja = new KayttajaImpl();
				kayttaja.setSahkoposti(principal.getName());
				
				Merkinta merkinta = new MerkintaImpl();
				merkinta.setKayttaja(kayttaja);
				merkinta.setTunnit(tunnitYht);
				merkinta.setKuvaus(kuvaus);
				
				logger.info("Merkinnässä kaikki ok: " + merkinta.toString());
				
				int rivit = projektiDao.tallennaMerkinta(projektiId, merkinta);
				
				logger.info("Tietokantaan meni " + rivit + " riviä");
				
				if (rivit == 1) {
					logger.info("Lisättiin " + tunnitYht + "h henkilölle "
							+ principal.getName());

					// Jos Slack-checkbox valittu niin viesti Slackiin
					// @TODO: Kovakoodattu Slackbotti vaan projektille id 1
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
			
		if (viesti != null) {
			model.addAttribute("viesti", viesti);
		}	
		
		return "redirect:/projekti/" + projektiId;
		
	}
	@RequestMapping(value = "/lisaa-projekti", method = RequestMethod.GET)
	public String lisaaProjektiGet(Model model, Principal principal) {
		Kayttaja kayttaja = kayttajaDao.haeKayttajaSahkopostilla(principal.getName());
		List<Projekti> projektit = projektiDao.haeKayttajanProjektit(principal.getName());
		List<EtusivunMerkinta> merkinnat = kayttajaDao.haeEtusivunMerkinnat(principal.getName());
		model.addAttribute("kayttaja", kayttaja);
		model.addAttribute("projektit", projektit);
		model.addAttribute("merkinnat", merkinnat);
	return "projektinlisays";
	}

	@RequestMapping(value = "/lisaa-projekti", method = RequestMethod.POST)
	public String lisaaProjektiPost(
			Model model,
			@RequestParam(value = "nimi", required = true) String nimi,
			@RequestParam(value = "kuvaus", required = true) String kuvaus,
			Principal principal, @Valid ProjektiImpl projekti, BindingResult result
			) {
		
		if (result.hasErrors()) {
			logger.info("Projektin validointi epäonnistui, projektia ei lisätty tietokantaan!");
			return "redirect:/lisaa-projekti";
		} else {
			int projektiId = 0;
			Projekti projektilisays = new ProjektiImpl();
			projektilisays.setNimi(nimi);
			projektilisays.setKuvaus(kuvaus);
			projektiId = projektiDao.lisaaProjekti(projekti, principal.getName());
			
			if (projektiId > 0) {
				return "redirect:/projekti/" + projektiId;
			}
			logger.info("Projektin lisäys onnistui!");
			return lisaaProjektiGet(model, principal);
			}
		}		
			
	@RequestMapping(value = "/rekisteroidy", method = RequestMethod.POST)
	public String rekisteroidy(
			Model model,
			@RequestParam(value = "etunimi", required = true) String etunimi,
			@RequestParam(value = "sukunimi", required = true) String sukunimi,
			@RequestParam(value = "sahkoposti", required = true) String sahkoposti,
			@RequestParam(value = "salasana", required = true) String salasana,
			 @Valid KayttajaImpl kayttaja, BindingResult result ) {
		
		if (result.hasErrors()) {
			return "redirect:/kirjaudu?regfail";
		} else {
			String salasanaTiiviste = BCrypt.hashpw(salasana, BCrypt.gensalt(10));
			if (kayttajaDao.rekisteroiKayttaja(etunimi, sukunimi, sahkoposti, salasanaTiiviste)) {
				logger.info("Uusi käyttäjä rekisteröity!");
				return "redirect:/kirjaudu?regok";
			} else {
				logger.info("Käyttäjän rekisteröinti epäonnistui!");
				return "redirect:/kirjaudu?regfail";
			}
		}
	}
}
