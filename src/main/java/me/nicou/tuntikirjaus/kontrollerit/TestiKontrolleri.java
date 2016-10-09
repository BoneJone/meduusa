package me.nicou.tuntikirjaus.kontrollerit;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.nicou.tuntikirjaus.bean.Merkinta;
import me.nicou.tuntikirjaus.dao.ProjektiDaoImpl;

@Controller
public class TestiKontrolleri {
	
	@Autowired
	@Qualifier("projektiDao")
	private ProjektiDaoImpl dao;
	
	private static final Logger logger = LoggerFactory.getLogger(TestiKontrolleri.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String naytaMerkinnat(Model model) {
		List<Merkinta> merkinnat = dao.haeKaikkiMerkinnat();
		model.addAttribute("merkinnat", merkinnat);
	return "merkinnat";
	}

}
