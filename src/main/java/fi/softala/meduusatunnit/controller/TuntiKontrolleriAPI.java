package fi.softala.meduusatunnit.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.softala.meduusatunnit.bean.Merkinta;
import fi.softala.meduusatunnit.dao.MerkintaDAO;

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

	@RequestMapping(value = "/api/henkilo/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Merkinta> haeHenkilonMerkinnat(@PathVariable Integer id) {
		List<Merkinta> merkinnat = dao.haeYhdenKayttajanMerkinnat(id);
		return merkinnat;
	}
	
}
