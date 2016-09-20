package fi.softala.meduusatunnit.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import fi.softala.meduusatunnit.bean.Merkinta;

public class MerkintaDAOSpringJdbcImpl implements MerkintaDAO {

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

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

	public int poistaMerkinta(int merkintaId) {
		String sql = "DELETE FROM Merkinta WHERE id = ?";
		Object[] parametrit = { merkintaId };
		int rivit = jdbcTemplate.update(sql, parametrit);
		return rivit;
	}
}
