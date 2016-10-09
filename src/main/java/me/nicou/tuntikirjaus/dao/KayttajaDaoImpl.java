package me.nicou.tuntikirjaus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import me.nicou.tuntikirjaus.bean.Kayttaja;

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
		RowMapper<Kayttaja> mapper = new KayttajaRowMapper();
		Kayttaja kayttaja = jdbcTemplate.queryForObject(sql, new Object[] { sahkoposti }, mapper);
		logger.info("Haettiin kannasta käyttäjä " + kayttaja.toString());
		return kayttaja;
	}

}
