package me.nicou.tuntikirjaus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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

}
