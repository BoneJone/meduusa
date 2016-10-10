package me.nicou.tuntikirjaus.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import me.nicou.tuntikirjaus.bean.Merkinta;
import me.nicou.tuntikirjaus.bean.Projekti;
import me.nicou.tuntikirjaus.bean.ProjektiImpl;

@Repository("projektiDao")
public class ProjektiDaoImpl implements ProjektiDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public ProjektiDaoImpl() {
		super();
	}
	
	private static final Logger logger = LoggerFactory.getLogger(KayttajaDaoImpl.class);
	
	public List<Merkinta> haeKaikkiMerkinnat() {
		String sql = "SELECT Kayttajat.id AS kayttaja_id, Merkinnat.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinnat JOIN Kayttajat ON Merkinnat.kayttaja_id = Kayttajat.id ORDER BY Merkinnat.paivamaara DESC";
		RowMapper<Merkinta> mapper = new MerkintaRowMapper();
		List<Merkinta> merkinnat = jdbcTemplate.query(sql, mapper);
		return merkinnat;
	}
	
	public List<Projekti> haeKayttajanProjektit(String sahkoposti) {
		List<Projekti> projektit = new ArrayList<Projekti>();
		String sql = "SELECT id, nimi, kuvaus, luontipaiva FROM Projektit p JOIN ProjektinJasenet pj ON p.id = pj.projekti_id WHERE pj.kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?) ORDER BY id DESC";
		try {
			RowMapper<Projekti> mapper = new ProjektiListaRowMapper();
			projektit = jdbcTemplate.query(sql, new Object[] { sahkoposti }, mapper);
		} catch (EmptyResultDataAccessException ex) {
			logger.debug("Käyttäjällä ei ole yhtään projekteja");
		}
		return projektit;
	}
	
	public Projekti haeProjektinTiedot(int projektiId, String sahkoposti) {
		Projekti projekti = new ProjektiImpl();
		String sql = "SELECT id, nimi, kuvaus, luontipaiva FROM Projektit p JOIN ProjektinJasenet pj ON p.id = pj.projekti_id WHERE pj.kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?) AND p.id = ? ORDER BY id DESC";
		try {
			RowMapper<Projekti> mapper = new ProjektiListaRowMapper();
			projekti = jdbcTemplate.queryForObject(sql, new Object[] { sahkoposti, projektiId }, mapper);
		} catch (EmptyResultDataAccessException ex) {
			logger.debug("Käyttäjä yritti hakea projekti jota ei löytynyt");
			return projekti;
		}
		
		sql = "SELECT k.id AS kayttaja_id, m.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinnat m JOIN Kayttajat k ON m.kayttaja_id = k.id WHERE m.projekti_id = ? ORDER BY m.paivamaara DESC";
		RowMapper<Merkinta> mapper = new MerkintaRowMapper();
		try {
			List<Merkinta> merkinnat = jdbcTemplate.query(sql, new Object[] { projektiId }, mapper);
			projekti.setMerkinnat(merkinnat);
		} catch (EmptyResultDataAccessException ex) {
			logger.debug("Haettiin tiedot projektista jolla ei ole vielä merkintöjä");
		}
		
		return projekti;
	}
	
	public int tallennaMerkinta(int projektiId, Merkinta merkinta) {
		String sql = "SELECT kayttaja_id FROM ProjektinJasenet WHERE kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?)";
		String kayttaja;
		try {
			kayttaja = jdbcTemplate.queryForObject(sql, new Object[] { merkinta.getKayttaja().getSahkoposti() }, String.class);
		} catch (EmptyResultDataAccessException ex) {
			logger.info("Käyttäjä yritti lisätä tunteja projektiin jota ei löydy tai johon hän ei kuulu");
			return 0;
		}

		logger.info("Käyttäjä kuuluu projektiin, lisätään merkintä");
		
		sql = "INSERT INTO Merkinnat (kayttaja_id, projekti_id, tunnit, kuvaus) VALUES(?, ?, ?, ?)";
		Object[] parametrit = { kayttaja, projektiId, merkinta.getTunnit(), merkinta.getKuvaus() };
		return jdbcTemplate.update(sql, parametrit);
	}

}
