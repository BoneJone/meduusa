package fi.softala.meduusatunnit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fi.softala.meduusatunnit.bean.Merkinta;
import fi.softala.meduusatunnit.bean.MerkintaImpl;
import fi.softala.meduusatunnit.dao.MerkintaDAO;
import fi.softala.meduusatunnit.utility.MerkintaJarjestaja;

@Controller
public class TuntiKontrolleri {

	@Inject
	private MerkintaDAO dao;
	
	public MerkintaDAO getDao() {
		return dao;
	}

	public void setDao(MerkintaDAO dao) {
		this.dao = dao;
	}
	
	final static Logger logger = LoggerFactory.getLogger(TuntiKontrolleri.class);
	
	@RequestMapping (value="/", method=RequestMethod.GET)
	public String showFrontpage(Model model) {
		
		// Näytetään etusivu
		
		// Kutsutaan DAO-luokkaa ja haetaan merkinnät
				List<Merkinta> merkinnat = dao.haeKaikkiMerkinnat();
				
				String naytettavat = "kaikki";
				
				// Kikkaillaan totalit omaksi listaksi, joku muu saa miettiä tähän fiksumpaa tapaa
				List<Merkinta> tiimintunnit = new ArrayList<>();
				
				for (int i = 0; i < merkinnat.size(); i++) {
					
					if (tiimintunnit.size() == 0) {
						Merkinta merkinta = new MerkintaImpl();
						merkinta.setKayttaja(merkinnat.get(i).getKayttaja());
						merkinta.setTunnit(merkinnat.get(i).getTunnit());
						tiimintunnit.add(merkinta);
					}
					else {
						boolean loytyi = false;
						for (int j = 0; j < tiimintunnit.size(); j++) {
							if (merkinnat.get(i).getKayttaja().getId() == tiimintunnit.get(j).getKayttaja().getId()) {
								tiimintunnit.get(j).setTunnit(tiimintunnit.get(j).getTunnit() + merkinnat.get(i).getTunnit());
								j = tiimintunnit.size();
								loytyi = true;
							}
						}
						if (!loytyi) {
							Merkinta merkinta = new MerkintaImpl();
							merkinta.setKayttaja(merkinnat.get(i).getKayttaja());
							merkinta.setTunnit(merkinnat.get(i).getTunnit());
							tiimintunnit.add(merkinta);
						}
					}
				}
				
				// Järjestetään tiimin tunnit suurimmasta määrästä pienimpään
				Collections.sort(tiimintunnit, new MerkintaJarjestaja());
				model.addAttribute("merkinnat", merkinnat);
				model.addAttribute("tiimintunnit", tiimintunnit);
				model.addAttribute("naytettavat", naytettavat);
				
		return "sivu";
	}


}
