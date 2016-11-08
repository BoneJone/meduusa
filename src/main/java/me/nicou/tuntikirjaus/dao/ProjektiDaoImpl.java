package me.nicou.tuntikirjaus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import me.nicou.tuntikirjaus.bean.Merkinta;
import me.nicou.tuntikirjaus.bean.MerkintaLista;
import me.nicou.tuntikirjaus.bean.MerkintaListaImpl;
import me.nicou.tuntikirjaus.bean.Projekti;
import me.nicou.tuntikirjaus.bean.ProjektiImpl;

@Repository("projektiDao")
public class ProjektiDaoImpl implements ProjektiDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public ProjektiDaoImpl() {
		super();
	}
	
	private static final int MERKINNAT_PER_SIVU = 8;
	
	private static final Logger logger = LoggerFactory.getLogger(KayttajaDaoImpl.class);
	
	public List<Projekti> haeKayttajanProjektit(String sahkoposti) {
		List<Projekti> projektit = new ArrayList<Projekti>();
		String sql = "SELECT id, nimi, kuvaus, luontipaiva, (SELECT 0) AS yhteistunnit FROM Projektit p JOIN ProjektinJasenet pj ON p.id = pj.projekti_id WHERE pj.kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?) ORDER BY id ASC";
		try {
			RowMapper<Projekti> mapper = new ProjektiListaRowMapper();
			projektit = jdbcTemplate.query(sql, new Object[] { sahkoposti }, mapper);
		} catch (EmptyResultDataAccessException ex) {
			logger.debug("Käyttäjällä ei ole yhtään projekteja");
		}
		return projektit;
	}
	
	public Projekti haeProjektinTiedot(int projektiId, String sahkoposti, int sivunumero) {
		Projekti projekti = new ProjektiImpl();
		MerkintaLista merkintaLista = new MerkintaListaImpl();
		merkintaLista.setMerkinnat(new ArrayList<Merkinta>());
		projekti.setMerkintaLista(new MerkintaListaImpl());
		projekti.getMerkintaLista().setMerkinnat(new ArrayList<Merkinta>());
		String sql = "SELECT p.id, p.nimi, p.kuvaus, p.luontipaiva, SUM(m.tunnit) AS yhteistunnit FROM Projektit p JOIN ProjektinJasenet pj ON p.id = pj.projekti_id JOIN Merkinnat m ON p.id = m.projekti_id WHERE pj.kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?) AND p.id = ? ORDER BY id DESC";
		try {
			RowMapper<Projekti> mapper = new ProjektiListaRowMapper();
			projekti = jdbcTemplate.queryForObject(sql, new Object[] { sahkoposti, projektiId }, mapper);
		} catch (EmptyResultDataAccessException ex) {
			logger.debug("Käyttäjä yritti hakea projektia jota ei löytynyt tai johon ei kuulu");
			return projekti;
		}

		try {
			sql = "SELECT COUNT(*) FROM Merkinnat WHERE projekti_id = ?";
			
			int offset = MERKINNAT_PER_SIVU * sivunumero - MERKINNAT_PER_SIVU;
			merkintaLista.setNykyinenSivu(sivunumero);
			merkintaLista.setMerkintojaPerSivu(offset);
			
			// Sivujen yhteismäärän haku ja roundaus
			int sivujaYhteensa = jdbcTemplate.queryForObject(sql, new Object[] { projektiId }, Integer.class);
			sivujaYhteensa = (int) Math.ceil((double) sivujaYhteensa / (double) MERKINNAT_PER_SIVU);
			merkintaLista.setSivujaYhteensa(sivujaYhteensa);
			
			if (merkintaLista.getSivujaYhteensa() == 0 || sivunumero > merkintaLista.getSivujaYhteensa()) {
				projekti.setMerkintaLista(merkintaLista);
				return projekti;
			}
			
			sql = "SELECT k.id AS kayttaja_id, m.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinnat m JOIN Kayttajat k ON m.kayttaja_id = k.id WHERE m.projekti_id = ? ORDER BY m.paivamaara DESC LIMIT ? OFFSET ?";
			
			RowMapper<Merkinta> mapper = new MerkintaRowMapper();
			merkintaLista.setMerkinnat(jdbcTemplate.query(sql, new Object[] { projektiId, MERKINNAT_PER_SIVU, offset }, mapper));
			merkintaLista.laskeRenderoitavatSivunumerot();
			projekti.setMerkintaLista(merkintaLista);
		} catch (EmptyResultDataAccessException ex) {
			logger.debug("Haettiin tiedot projektista jolla ei ole vielä merkintöjä");
		}
		
		return projekti;
	}
	
	public Projekti haeProjektinTiedotKayttajalta(int projektiId, String sahkoposti, int kayttajaId, int sivunumero) {
		Projekti projekti = new ProjektiImpl();
		MerkintaLista merkintaLista = new MerkintaListaImpl();
		merkintaLista.setMerkinnat(new ArrayList<Merkinta>());
		String sql = "SELECT p.id, p.nimi, p.kuvaus, p.luontipaiva, SUM(m.tunnit) AS yhteistunnit FROM Projektit p JOIN ProjektinJasenet pj ON p.id = pj.projekti_id JOIN Merkinnat m ON p.id = m.projekti_id WHERE pj.kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?) AND p.id = ? ORDER BY id DESC";
		try {
			RowMapper<Projekti> mapper = new ProjektiListaRowMapper();
			projekti = jdbcTemplate.queryForObject(sql, new Object[] { sahkoposti, projektiId }, mapper);
		} catch (EmptyResultDataAccessException ex) {
			logger.debug("Käyttäjä yritti hakea projekti jota ei löytynyt tai johon ei kuulu");
			return projekti;
		}

		try {
			
			sql = "SELECT COUNT(*) FROM Merkinnat WHERE projekti_id = ? AND kayttaja_id = ?";
			
			int offset = MERKINNAT_PER_SIVU * sivunumero - MERKINNAT_PER_SIVU;
			merkintaLista.setNykyinenSivu(sivunumero);
			merkintaLista.setMerkintojaPerSivu(offset);
			
			// Sivujen yhteismäärän haku ja roundaus
			int sivujaYhteensa = jdbcTemplate.queryForObject(sql, new Object[] { projektiId, kayttajaId }, Integer.class);
			sivujaYhteensa = (int) Math.ceil((double) sivujaYhteensa / (double) MERKINNAT_PER_SIVU);
			merkintaLista.setSivujaYhteensa(sivujaYhteensa);
			
			if (merkintaLista.getSivujaYhteensa() == 0 || sivunumero > merkintaLista.getSivujaYhteensa()) {
				projekti.setMerkintaLista(merkintaLista);
				return projekti;
			}
			
			RowMapper<Merkinta> mapper = new MerkintaRowMapper();
			sql = "SELECT k.id AS kayttaja_id, m.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinnat m JOIN Kayttajat k ON m.kayttaja_id = k.id WHERE m.projekti_id = ? AND m.kayttaja_id = ? ORDER BY m.paivamaara DESC LIMIT ? OFFSET ?";
			merkintaLista.setMerkinnat(jdbcTemplate.query(sql, new Object[] { projektiId, kayttajaId, MERKINNAT_PER_SIVU, offset }, mapper));
			merkintaLista.laskeRenderoitavatSivunumerot();
			projekti.setMerkintaLista(merkintaLista);
		} catch (EmptyResultDataAccessException ex) {
			logger.debug("Haettiin tiedot projektista jolla ei ole vielä merkintöjä");
		}
		
		return projekti;
	}
	
	public int tallennaMerkinta(int projektiId, Merkinta merkinta) {
		String sql = "SELECT kayttaja_id FROM ProjektinJasenet WHERE kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?) AND projekti_id = ?";
		String kayttaja;
		try {
			kayttaja = jdbcTemplate.queryForObject(sql, new Object[] { merkinta.getKayttaja().getSahkoposti(), projektiId }, String.class);
		} catch (EmptyResultDataAccessException ex) {
			logger.debug("Käyttäjä yritti lisätä tunteja projektiin jota ei löydy tai johon hän ei kuulu");
			return 0;
		}

		logger.info("Käyttäjä kuuluu projektiin, lisätään merkintä");
		
		sql = "INSERT INTO Merkinnat (kayttaja_id, projekti_id, paivamaara, tunnit, kuvaus) VALUES(?, ?, ?, ?, ?)";
		Object[] parametrit = { kayttaja, projektiId, merkinta.getPaivamaara(), merkinta.getTunnit(), merkinta.getKuvaus() };
		return jdbcTemplate.update(sql, parametrit);
	}
	
	public int poistaKayttajanMerkinta(int merkintaId, String sahkoposti) {
		String sql = "DELETE FROM Merkinnat WHERE id = ? AND kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?)";
		return jdbcTemplate.update(sql, new Object[] { merkintaId, sahkoposti });
	}
	
	public List<Merkinta> haeProjektinJasenet(int projektiId, String sahkoposti) {
		String sql = "SELECT 0 AS merkinta_id, null AS paivamaara, null AS kuvaus, k.id AS kayttaja_id, sahkoposti, etunimi, sukunimi, (SELECT SUM(tunnit) FROM Merkinnat WHERE kayttaja_id = pj.kayttaja_id AND projekti_id = pj.projekti_id) AS tunnit FROM ProjektinJasenet pj JOIN Kayttajat k ON pj.kayttaja_id = k.id WHERE pj.projekti_id = ? AND (SELECT 1 FROM ProjektinJasenet pj JOIN Kayttajat k ON pj.kayttaja_id = k.id WHERE projekti_id = ? AND k.sahkoposti = ?) = 1 ORDER BY tunnit DESC";
		List<Merkinta> merkinnat = new ArrayList<>();
		try {
			RowMapper<Merkinta> mapper = new MerkintaRowMapper();
			merkinnat = jdbcTemplate.query(sql, new Object[] { projektiId, projektiId, sahkoposti }, mapper);
		} catch (EmptyResultDataAccessException ex) {
			logger.debug("Käyttäjä yritti hakea yhteistunnit projektista jolla ei ole merkintöjä");
		}
		return merkinnat;
	}
	
	public int lisaaProjekti (Projekti projekti, String sahkoposti) {
		 final String projektiSql = "INSERT INTO Projektit (nimi, kuvaus) VALUES(?, ?)";
		 Object[] parametrit = { projekti.getNimi(), projekti.getKuvaus() };
		 KeyHolder keyholder = new GeneratedKeyHolder();
		 int projektiId = 0;
	  	try {
	  	  jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(projektiSql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, projekti.getNimi());
				ps.setString(2, projekti.getKuvaus());
				return ps;
			}
		}, keyholder);
	  	  projektiId = keyholder.getKey().intValue();
	  	  logger.info("Luotiin projekti ID:llä " + projektiId);
	  	  
	  	  String sql = "INSERT INTO ProjektinJasenet (kayttaja_id, projekti_id, rooli_id, status_id) VALUES ((SELECT id FROM Kayttajat WHERE sahkoposti = ?), ?, 2, 2)";
	  	  if (jdbcTemplate.update(sql, new Object[] { sahkoposti, projektiId }) == 1) {
	  		  return projektiId;
	  	  } else {
	  		  return 0;
	  	  }
	  	} catch (EmptyResultDataAccessException ex) {
	  			logger.error("Projektia lisätessä tapahtui virhe");
	  	}
	  	return 0;
	 }
	
	public boolean lisaaJasenProjektiin(int projektiId, String lisattavaSahkoposti, String sahkoposti) {
		// Validoidaan että kutsuva henkilö kuuluu projektiin roolilla siteadmin tai projectadmin
		String sql = "SELECT COUNT(*) FROM ProjektinJasenet WHERE kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?) AND projekti_id = ? AND rooli_id IN (1, 2)";
		try {
			if (jdbcTemplate.queryForObject(sql, new Object[] { sahkoposti, projektiId }, Integer.class) == 0) {
				logger.debug("Käyttäjä yritti lisätä jäsentä projektiin johon ei itse kuulu adminina");
				return false;
			};
			// Validoidaan että kutsuttava jäsen ei jo kuulu projektiin
			sql = "SELECT COUNT(*) FROM ProjektinJasenet WHERE kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?) AND projekti_id = ?";
			if (jdbcTemplate.queryForObject(sql, new Object[] { lisattavaSahkoposti, projektiId }, Integer.class) != 0) {
				logger.debug("Käyttäjä yritettiin lisätä projektiin johon hän jo kuuluu");
				return false;
			}
			// @TODO: Kovakoodattu rooli 'projectuser' ja status 'active'
			sql = "INSERT INTO ProjektinJasenet (kayttaja_id, projekti_id, rooli_id, status_id) VALUES ((SELECT id FROM Kayttajat WHERE sahkoposti = ?), ?, 3, 2)";
			return jdbcTemplate.update(sql, new Object[] { lisattavaSahkoposti, projektiId }) == 1;
		} catch (Exception ex) {
			logger.error("Projektin jäsentä lisätessä tapahtui virhe: " + ex);
		}
		return false;
	}

}
