package me.nicou.tuntikirjaus.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import me.nicou.tuntikirjaus.bean.EtusivunMerkinta;
import me.nicou.tuntikirjaus.bean.Kayttaja;
import me.nicou.tuntikirjaus.bean.KayttajaImpl;

@Repository("kayttajaDao")
public class KayttajaDaoImpl implements KayttajaDao {
	
	private static final Logger logger = LoggerFactory.getLogger(KayttajaDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public KayttajaDaoImpl() {
		super();
	}
	
	public Kayttaja haeKayttajaSahkopostilla(String sahkoposti) {
		String sql = "SELECT id, etunimi, sukunimi, sahkoposti, salasana FROM Kayttajat WHERE sahkoposti = ?";
		Kayttaja kayttaja = new KayttajaImpl();
		try {
			RowMapper<Kayttaja> mapper = new KayttajaRowMapper();
			kayttaja = jdbcTemplate.queryForObject(sql, new Object[] { sahkoposti }, mapper);
		} catch (Exception ex) {
			logger.error("Käyttäjää " + sahkoposti + " ei löytynyt kannasta");
		}
		return kayttaja;
	}
	
	public List<EtusivunMerkinta> haeEtusivunMerkinnat(String sahkoposti) {
		String sql = "SELECT k.etunimi, k.sukunimi, paivamaara, p.id, p.nimi, m.tunnit, m.kuvaus FROM Merkinnat m RIGHT JOIN ProjektinJasenet pj USING (projekti_id) JOIN Kayttajat k ON m.kayttaja_id = k.id JOIN Projektit p ON m.projekti_id = p.id WHERE pj.kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?) ORDER BY m.paivamaara DESC LIMIT 10;";
		List<EtusivunMerkinta> merkinnat = new ArrayList<>();
		try {
			RowMapper<EtusivunMerkinta> mapper = new EtusivunMerkintaRowMapper();
			merkinnat = jdbcTemplate.query(sql, new Object[] { sahkoposti }, mapper);
		} catch (Exception ex) {
			logger.error("Käyttäjän " + sahkoposti + " missään projektissa ei ole merkintöjä");
		}
		return merkinnat;
	}

}
