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
		String sql = "SELECT id, nimi, kuvaus, luontipaiva FROM Projektit p JOIN ProjektinJasenet pj ON p.id = pj.projekti_id WHERE pj.kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?) ORDER BY id ASC";
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
		projekti.setMerkintaLista(new MerkintaListaImpl());
		projekti.getMerkintaLista().setMerkinnat(new ArrayList<Merkinta>());
		String sql = "SELECT id, nimi, kuvaus, luontipaiva FROM Projektit p JOIN ProjektinJasenet pj ON p.id = pj.projekti_id WHERE pj.kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?) AND p.id = ? ORDER BY id DESC";
		try {
			RowMapper<Projekti> mapper = new ProjektiListaRowMapper();
			projekti = jdbcTemplate.queryForObject(sql, new Object[] { sahkoposti, projektiId }, mapper);
		} catch (EmptyResultDataAccessException ex) {
			logger.debug("Käyttäjä yritti hakea projekti jota ei löytynyt tai johon ei kuulu");
			return projekti;
		}

		try {
			sql = "SELECT COUNT(*) FROM Merkinnat WHERE projekti_id = ?";
			
			int offset = MERKINNAT_PER_SIVU * sivunumero - MERKINNAT_PER_SIVU;
			MerkintaLista merkintaLista = new MerkintaListaImpl();
			merkintaLista.setNykyinenSivu(sivunumero);
			merkintaLista.setMerkintojaPerSivu(offset);
			
			// Sivujen yhteismäärän haku ja roundaus
			int sivujaYhteensa = jdbcTemplate.queryForObject(sql, new Object[] { projektiId }, Integer.class);
			sivujaYhteensa = (int) Math.ceil((double) sivujaYhteensa / (double) MERKINNAT_PER_SIVU);
			merkintaLista.setSivujaYhteensa(sivujaYhteensa);
			
			if (merkintaLista.getSivujaYhteensa() == 0 || sivunumero > merkintaLista.getSivujaYhteensa()) {
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
		String sql = "SELECT id, nimi, kuvaus, luontipaiva FROM Projektit p JOIN ProjektinJasenet pj ON p.id = pj.projekti_id WHERE pj.kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?) AND p.id = ? ORDER BY id DESC";
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
			MerkintaLista merkintaLista = new MerkintaListaImpl();
			merkintaLista.setNykyinenSivu(sivunumero);
			merkintaLista.setMerkintojaPerSivu(offset);
			
			// Sivujen yhteismäärän haku ja roundaus
			int sivujaYhteensa = jdbcTemplate.queryForObject(sql, new Object[] { projektiId, kayttajaId }, Integer.class);
			sivujaYhteensa = (int) Math.ceil((double) sivujaYhteensa / (double) MERKINNAT_PER_SIVU);
			merkintaLista.setSivujaYhteensa(sivujaYhteensa);
			
			if (merkintaLista.getSivujaYhteensa() == 0 || sivunumero > merkintaLista.getSivujaYhteensa()) {
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
		
		sql = "INSERT INTO Merkinnat (kayttaja_id, projekti_id, tunnit, kuvaus) VALUES(?, ?, ?, ?)";
		Object[] parametrit = { kayttaja, projektiId, merkinta.getTunnit(), merkinta.getKuvaus() };
		return jdbcTemplate.update(sql, parametrit);
	}
	
	public int poistaKayttajanMerkinta(int merkintaId, String sahkoposti) {
		String sql = "DELETE FROM Merkinnat WHERE id = ? AND kayttaja_id = (SELECT id FROM Kayttajat WHERE sahkoposti = ?)";
		return jdbcTemplate.update(sql, new Object[] { merkintaId, sahkoposti });
	}
	
	public List<Merkinta> haeProjektinYhteistunnit(int projektiId, String sahkoposti) {
		String sql = "SELECT 0 AS merkinta_id, null AS paivamaara, null AS kuvaus, Kayttajat.id AS kayttaja_id, sahkoposti, etunimi, sukunimi, SUM(tunnit) AS tunnit FROM Merkinnat JOIN Kayttajat ON Merkinnat.kayttaja_id = Kayttajat.id WHERE projekti_id = ? AND (SELECT 1 FROM ProjektinJasenet pj JOIN Kayttajat k ON pj.kayttaja_id = k.id WHERE projekti_id = ? AND k.sahkoposti = ?) = 1 GROUP BY kayttaja_id ORDER BY tunnit DESC";
		List<Merkinta> merkinnat = new ArrayList<>();
		try {
			RowMapper<Merkinta> mapper = new MerkintaRowMapper();
			merkinnat = jdbcTemplate.query(sql, new Object[] { projektiId, projektiId, sahkoposti }, mapper);
		} catch (EmptyResultDataAccessException ex) {
			logger.debug("Käyttäjä yritti hakea yhteistunnit projektista jolla ei ole merkintöjä");
		}
		return merkinnat;
	}
	
	public void lisaaProjekti (Projekti projekti) {
				 String sql = "INSERT INTO Projektit (nimi, kuvaus) VALUES(?, ?)";
				 Object[] parametrit = { projekti.getNimi(), projekti.getKuvaus() };
			  	try {
			  	  jdbcTemplate.update(sql, parametrit);
			  	}	catch (EmptyResultDataAccessException ex) {
			  			logger.error("Projektia lisätessä tapahtui virhe");
			  	}	
			 }

}
