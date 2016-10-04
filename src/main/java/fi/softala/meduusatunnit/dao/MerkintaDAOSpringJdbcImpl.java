package fi.softala.meduusatunnit.dao;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import fi.softala.meduusatunnit.bean.Merkinta;

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
}
