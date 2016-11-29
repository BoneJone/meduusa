package me.nicou.tuntikirjaus.kontrollerit;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.nicou.tuntikirjaus.bean.EtusivunMerkinta;
import me.nicou.tuntikirjaus.bean.Kayttaja;
import me.nicou.tuntikirjaus.bean.KayttajaImpl;
import me.nicou.tuntikirjaus.bean.Merkinta;
import me.nicou.tuntikirjaus.bean.MerkintaImpl;
import me.nicou.tuntikirjaus.bean.Projekti;
import me.nicou.tuntikirjaus.bean.ProjektiImpl;
import me.nicou.tuntikirjaus.bean.Viesti;
import me.nicou.tuntikirjaus.bean.ViestiImpl;
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
			@ModelAttribute("viesti") String viesti,
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
		model.addAttribute("viesti", viesti);
		
		if (kayttaja == null) {
			return "redirect:/";
		}
		
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
		model.addAttribute("kayttajaId", kayttajaId);
		
		// @TODO: Keksitään fiksumpi tapa, mutta nyt pitää includee nää joka hakuun
		// jotta saadaan sidebariin projektit näkyviin
		List<Projekti> projektit = projektiDao.haeKayttajanProjektit(principal.getName());
		model.addAttribute("projektit", projektit);
		
		return "jasenen-merkinnat";
	}
	
	@RequestMapping(value = "/hae-rest-merkinta", method = RequestMethod.GET)
	public @ResponseBody Merkinta haeKayttajanMerkinta(
			@RequestParam(value = "id", required = true) Integer id,
			Principal principal) {
		return projektiDao.haeYksiMerkinta(id, principal.getName());
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
	
	@RequestMapping(value = "/projekti/poistu-projektista", method = RequestMethod.POST)
	public String poistuProjektista (
			Model model,
			@RequestParam(value = "projektiId", required = true) Integer projektiId,
			@RequestParam(value = "kayttajaId", required = true) Integer kayttajaId,
			Principal principal) {
		
		try {			
			Kayttaja kayttaja = new KayttajaImpl();
			kayttaja.setSahkoposti(principal.getName());
						
			if (projektiDao.poistuProjektista(projektiId, kayttajaId)) {
				logger.info("Projektista " + projektiId + " poistuminen onnistui!");
			} else {
				logger.info("Projektista " + projektiId + " poistuminen ei onnistunut!");
			}	
			
		} catch (Exception ex) {
			logger.error("Projektista poistuessa tapahtui virhe " + ex);
		}		
			return "redirect:/";	
	}
	
	@RequestMapping(value = "/projekti/muokkaa-merkintaa", method = RequestMethod.POST)
	public String muokkaaMerkintaa(
			Model model,
			@RequestParam(value = "id", required = true) Integer merkintaId,
			@RequestParam(value = "projektiId", required = true) Integer projektiId,
			@RequestParam(value = "kayttajaId", required = true) Integer kayttajaId,
			@RequestParam(value = "tunnit", required = true) String tunnit,
			@RequestParam(value = "minuutit", required = true) String minuutit,
			@RequestParam(value = "kuvaus", required = false) String kuvaus,
			@RequestParam(value = "paivamaara", required = false) String paivamaara,
			Principal principal) {
		
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
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				
				if (paivamaara == null != (paivamaara != null && !paivamaara.matches("^[\\d]{2}\\.[\\d]{2}\\.[\\d]{4}$"))) {
					Date date = new Date();
					paivamaara = sdf.format(date);
				}
				
				Date date = sdf.parse(paivamaara);
				
				logger.info("Paivamaara: " + date);
				
				Merkinta merkinta = new MerkintaImpl();
				merkinta.setId(merkintaId);
				merkinta.setKayttaja(kayttaja);
				merkinta.setTunnit(tunnitYht);
				merkinta.setKuvaus(kuvaus);
				merkinta.setPaivamaara(date);
				
				logger.info("Merkinnässä kaikki ok: " + merkinta.toString());
				
				if (projektiDao.muokkaaMerkintaa(merkinta)) {
					logger.info("Merkinnän " + merkintaId + " muokkaus onnistui!");
				} else {
					logger.info("Merkinnän " + merkintaId + " muokkaus ei onnistunut!");
				}
			}
			} catch (Exception ex) {
				logger.error("Merkintää muokatessa tapahtui virhe " + ex);
			}
		
		return "redirect:/projekti/" + projektiId + "/jasen/" + kayttajaId;
	}
	
	@RequestMapping(value = "projekti/{projektiId}/lisaajasen", method = RequestMethod.POST)
	public String lisaaProjektiinJasen(
			Model model,
			@PathVariable Integer projektiId,
			@RequestParam(value = "sahkoposti", required = true) String sahkoposti,
			Principal principal, @Valid Kayttaja kayttaja, BindingResult result,
			RedirectAttributes ra
			) {
		
		Viesti viesti = new ViestiImpl();
				
		if (result.hasErrors()) {
			logger.info("Käyttäjän " + sahkoposti + " lisäys projektiin " + projektiId + " ei onnistunut");
		} else {
			boolean success = projektiDao.lisaaJasenProjektiin(projektiId, sahkoposti.trim(), principal.getName());
			
			if (success) {
				logger.info("Käyttäjä " + sahkoposti + " lisätty projektiin " + projektiId);
				//viesti = new ViestiImpl("Onnistui!", "Käyttäjä lisätty projektiin", "success");
			} else {
				logger.info("Käyttäjän " + sahkoposti + " lisäys projektiin " + projektiId + " ei onnistunut");
				//viesti = new ViestiImpl("Virhe!", "Käyttäjän lisäyksessä tapahtui virhe!", "error");
			}
		}
		//ra.addFlashAttribute(viesti);
		return "redirect:/projekti/" + projektiId;
	}
	
	@RequestMapping(value = "/projekti/lisaamerkinta", method = RequestMethod.POST)
	public String lisaaMerkinta(
			Model model,
			@RequestParam(value = "id", required = true) Integer projektiId,
			@RequestParam(value = "tunnit", required = true) String tunnit,
			@RequestParam(value = "minuutit", required = true) String minuutit,
			@RequestParam(value = "kuvaus", required = false) String kuvaus,
			@RequestParam(value = "paivamaara", required = false) String paivamaara,
			@RequestParam(value = "slack", required = false) String slack[],
			RedirectAttributes redirectAttributes,
			Principal principal, @Valid MerkintaImpl merkintavalidointi, BindingResult result) {
		
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
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				
				if (paivamaara == null != (paivamaara != null && !paivamaara.matches("^[\\d]{2}\\.[\\d]{2}\\.[\\d]{4}$"))) {
					Date date = new Date();
					paivamaara = sdf.format(date);
				}
				
				Date date = sdf.parse(paivamaara);
				
				logger.info("Paivamaara: " + date);
				
				Merkinta merkinta = new MerkintaImpl();
				merkinta.setKayttaja(kayttaja);
				merkinta.setTunnit(tunnitYht);
				merkinta.setKuvaus(kuvaus);
				merkinta.setPaivamaara(date);
				
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
				logger.info("Virheellinen tuntimäärä");
				viesti = "Tuntivirhe";
			}

		} catch (Exception ex) {
			logger.debug("Errori: " + ex);
		}
			
		if (viesti != null) {
			redirectAttributes.addFlashAttribute("viesti", viesti);
		}	
		
		return "redirect:/projekti/" + projektiId;
		
	}
	
	@RequestMapping(value = "/lisaa-projekti", method = RequestMethod.GET)
	public String lisaaProjektiGet(Model model, Principal principal, ProjektiImpl projekti) {
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
			Principal principal, @Valid ProjektiImpl projekti, BindingResult result
			) {
		
		if (result.hasErrors()) {
			logger.info("Projektin validointi epäonnistui, projektia ei lisätty tietokantaan!");
			return lisaaProjektiGet(model, principal, projekti);
		} else {
			int projektiId = 0;
			projektiId = projektiDao.lisaaProjekti(projekti, principal.getName());
			
			if (projektiId > 0) {
				return "redirect:/projekti/" + projektiId;
			}
			logger.info("Projektin lisäys onnistui!");
			return lisaaProjektiGet(model, principal, projekti);
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
