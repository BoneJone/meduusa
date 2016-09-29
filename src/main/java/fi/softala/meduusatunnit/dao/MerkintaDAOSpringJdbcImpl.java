package fi.softala.meduusatunnit.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import fi.softala.meduusatunnit.bean.Merkinta;
import fi.softala.meduusatunnit.controller.TuntiKontrolleri;

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

		String sql = "SELECT Kayttaja.id AS kayttaja_id, Merkinta.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinta JOIN Kayttaja ON Merkinta.kayttaja_id = Kayttaja.id ORDER BY Merkinta.paivamaara DESC";
		RowMapper<Merkinta> mapper = new MerkintaRowMapper();

		List<Merkinta> merkinnat = jdbcTemplate.query(sql, mapper);
		return merkinnat;
	}

	public int tallennaMerkinta(Merkinta merkinta) {
		String sql = "SELECT id FROM Kayttaja WHERE sahkoposti = ?";
		String kayttaja;
		try {
		kayttaja = jdbcTemplate.queryForObject(sql, new Object[] { merkinta.getKayttaja().getSahkoposti() }, String.class);
		} catch (EmptyResultDataAccessException ex) {
			return 0;
		}
		sql = "INSERT INTO Merkinta (kayttaja_id, tunnit, kuvaus) VALUES(?, ?, ?)";
		Object[] parametrit = { kayttaja, merkinta.getTunnit(), merkinta.getKuvaus() };
		int rivit = jdbcTemplate.update(sql, parametrit);
		return rivit;
	}

	public List<Merkinta> haeYhdenKayttajanMerkinnat(int kayttajaId) {
		String sql = "SELECT Kayttaja.id AS kayttaja_id, Merkinta.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinta JOIN Kayttaja ON Merkinta.kayttaja_id = Kayttaja.id WHERE kayttaja_id = ? ORDER BY Merkinta.paivamaara DESC";
		Object[] parametrit = { kayttajaId };
		RowMapper<Merkinta> mapper = new MerkintaRowMapper();

		List<Merkinta> merkinnat = jdbcTemplate.query(sql, parametrit, mapper);
		return merkinnat;
	}
	
	public List<Merkinta> haeTunnitYhteensa() {
		String sql = "SELECT 0 AS merkinta_id, null AS paivamaara, null AS kuvaus, Kayttaja.id AS kayttaja_id, sahkoposti, etunimi, sukunimi, SUM(tunnit) AS tunnit FROM Merkinta JOIN Kayttaja ON Merkinta.kayttaja_id = Kayttaja.id GROUP BY kayttaja_id ORDER BY tunnit DESC";
		RowMapper<Merkinta> mapper = new MerkintaRowMapper();
		List<Merkinta> merkinnat = jdbcTemplate.query(sql, mapper);
		return merkinnat;
	}

	public int poistaMerkinta(int merkintaId) {
		Object[] parametrit = { merkintaId };
		String sql = "SELECT kayttaja_id FROM Merkinta WHERE id = ?";
		int kayttajaId = 0;
		try {
			kayttajaId = jdbcTemplate.queryForObject(sql, parametrit, Integer.class);
			sql = "DELETE FROM Merkinta WHERE id = ?";
			jdbcTemplate.update(sql, parametrit);
		} catch (Exception ex) {
			logger.info("Yritettiin poistaa merkinta jota ei loydy (id" + merkintaId + ")");
			return 0;
		}
		return kayttajaId;
	}
}
