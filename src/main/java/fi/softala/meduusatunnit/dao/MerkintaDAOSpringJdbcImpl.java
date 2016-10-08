package fi.softala.meduusatunnit.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fi.softala.meduusatunnit.bean.Merkinta;
import fi.softala.meduusatunnit.bean.Projekti;
import fi.softala.meduusatunnit.bean.ProjektiImpl;

@Repository
public class MerkintaDAOSpringJdbcImpl implements MerkintaDAO {

	@Inject
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	final static Logger logger = LoggerFactory.getLogger(MerkintaDAOSpringJdbcImpl.class);

	public List<Merkinta> haeKaikkiMerkinnat() {
		String sql = "SELECT Kayttajat.id AS kayttaja_id, Merkinnat.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinnat JOIN Kayttajat ON Merkinnat.kayttaja_id = Kayttajat.id ORDER BY Merkinnat.paivamaara DESC";
		RowMapper<Merkinta> mapper = new MerkintaRowMapper();

		List<Merkinta> merkinnat = jdbcTemplate.query(sql, mapper);
		return merkinnat;
	}
	
	public List<Merkinta> haeSeuraavatMerkinnat(int projektiId, int offset, int maara) {
		String sql = "SELECT Kayttajat.id AS kayttaja_id, Merkinnat.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinnat JOIN Kayttajat ON Merkinnat.kayttaja_id = Kayttajat.id WHERE projekti_id = ? ORDER BY Merkinnat.paivamaara DESC LIMIT ? OFFSET ?";
		Object[] parametrit = { projektiId, maara, offset };
		RowMapper<Merkinta> mapper = new MerkintaRowMapper();

		List<Merkinta> merkinnat = jdbcTemplate.query(sql, parametrit, mapper);
		return merkinnat;
	}

	public int tallennaMerkinta(Merkinta merkinta) {
		String sql = "SELECT id FROM Kayttajat WHERE sahkoposti = ?";
		String kayttaja;
		try {
		kayttaja = jdbcTemplate.queryForObject(sql, new Object[] { merkinta.getKayttaja().getSahkoposti() }, String.class);
		} catch (EmptyResultDataAccessException ex) {
			return 0;
		}
		// Kovakoodataan toistaiseksi projekti_id = 1
		sql = "INSERT INTO Merkinnat (kayttaja_id, projekti_id, tunnit, kuvaus) VALUES(?, 1, ?, ?)";
		Object[] parametrit = { kayttaja, merkinta.getTunnit(), merkinta.getKuvaus() };
		int rivit = jdbcTemplate.update(sql, parametrit);
		return rivit;
	}
	
	public List<Merkinta> haeYhdenKayttajanMerkinnat(int kayttajaId) {
		String sql = "SELECT Kayttajat.id AS kayttaja_id, Merkinnat.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinnat JOIN Kayttajat ON Merkinnat.kayttaja_id = Kayttajat.id WHERE kayttaja_id = ? ORDER BY Merkinnat.paivamaara DESC";
		Object[] parametrit = { kayttajaId };
		RowMapper<Merkinta> mapper = new MerkintaRowMapper();

		List<Merkinta> merkinnat = jdbcTemplate.query(sql, parametrit, mapper);
		return merkinnat;
	}
	
	public List<Merkinta> haeTunnitYhteensa() {
		String sql = "SELECT 0 AS merkinta_id, null AS paivamaara, null AS kuvaus, Kayttajat.id AS kayttaja_id, sahkoposti, etunimi, sukunimi, SUM(tunnit) AS tunnit FROM Merkinnat JOIN Kayttajat ON Merkinnat.kayttaja_id = Kayttajat.id GROUP BY kayttaja_id ORDER BY tunnit DESC";
		RowMapper<Merkinta> mapper = new MerkintaRowMapper();
		List<Merkinta> merkinnat = jdbcTemplate.query(sql, mapper);
		return merkinnat;
	}

	public int poistaMerkinta(int merkintaId) {
		Object[] parametrit = { merkintaId };
		String sql = "SELECT kayttaja_id FROM Merkinnat WHERE id = ?";
		int kayttajaId = 0;
		try {
			kayttajaId = jdbcTemplate.queryForObject(sql, parametrit, Integer.class);
			sql = "DELETE FROM Merkinnat WHERE id = ?";
			jdbcTemplate.update(sql, parametrit);
		} catch (Exception ex) {
			logger.info("Yritettiin poistaa merkinta jota ei loydy (id" + merkintaId + ")");
			return 0;
		}
		return kayttajaId;
	}
	
	// Projekteihin liittyvät
	// Katsotaan, eriytetäänkö omaan DAOon
	public List<Projekti> haeKayttajanProjektit(int kayttajaId) {
		List<Projekti> projektit = new ArrayList<Projekti>();
		String sql = "SELECT id, nimi, kuvaus, luontipaiva FROM Projektit WHERE m.kayttaja_id = ? ORDER BY id DESC";
		try {
			RowMapper<Projekti> mapper = new ProjektiListaRowMapper();
			projektit = jdbcTemplate.query(sql, mapper);
		} catch (EmptyResultDataAccessException ex) {
			logger.error("Käyttäjällä ei ole yhtään projekteja");
		}
		return projektit;
	}
	
	public Projekti haeProjektinMerkinnat(int projektiId, int kayttajaId) {
		Projekti projekti = new ProjektiImpl();
		List<Merkinta> merkinnat = new ArrayList<>();
		
		String sql = "SELECT p.id, p.nimi, p.kuvaus, p.luontipaiva FROM Projektit p JOIN ProjektinJasenet pj ON (p.id = pj.projekti_id) WHERE p.id = ? AND pj.kayttaja_id = ? ORDER BY p.id DESC";
		Object[] parametrit = { projektiId, kayttajaId };
		
		try {
			RowMapper<Projekti> projektiMapper = new ProjektiListaRowMapper();
			projekti = jdbcTemplate.queryForObject(sql, parametrit, projektiMapper);
		} catch (EmptyResultDataAccessException ex) {
			logger.error("Valittua projektia ei löytynyt");
		}
		
		if (projekti.getId() > 0) {
			Object[] parametrit2 = { projektiId };
			sql = "SELECT k.id AS kayttaja_id, m.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinnat m JOIN Kayttajat k ON m.kayttaja_id = k.id WHERE m.projekti_id = ? ORDER BY m.paivamaara DESC";
			
			try {
				RowMapper<Merkinta> merkintaMapper = new MerkintaRowMapper();
				merkinnat = jdbcTemplate.query(sql, parametrit2, merkintaMapper);
			} catch (EmptyResultDataAccessException ex) {
				logger.error("Projektilla ei ole yhtään merkintöjä");
			}
		}
		
		projekti.setMerkinnat(merkinnat);
		return projekti;
	}
	
	public int lisaaProjekti(Projekti projekti, int kayttajaId) {
		String sql = "INSERT INTO Projektit (nimi, kuvaus) VALUES(?, ?)";
		
		KeyHolder idHolder = new GeneratedKeyHolder();
		
		Object[] parametrit = { projekti.getNimi(), projekti.getKuvaus() };
	  	try {
	  	  jdbcTemplate.update(sql, parametrit, idHolder);
	  	}	catch (EmptyResultDataAccessException ex) {
	  			logger.error("Projektia lisätessä tapahtui virhe");
	  	}
	  	
	  	int projektiId = idHolder.getKey().intValue();
	  	projekti.setId(projektiId);
	  	return projektiId;
	 }
}
