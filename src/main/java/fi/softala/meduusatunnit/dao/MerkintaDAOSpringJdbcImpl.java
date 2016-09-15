package fi.softala.meduusatunnit.dao;

import java.util.List;

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

	public void tallennaMerkinta(Merkinta merkinta) {
		String sql = "INSERT INTO Merkinta (kayttaja_id, tunnit, kuvaus) VALUES((SELECT id FROM Kayttaja WHERE sahkoposti = ?), ?, ?)";
		Object[] parametrit = { merkinta.getKayttaja().getId(), merkinta.getTunnit(), merkinta.getKuvaus() };
		jdbcTemplate.update(sql, parametrit);
	}

	public List<Merkinta> haeYhdenKayttajanMerkinnat(int kayttajaId) {
		String sql = "SELECT Kayttaja.id AS kayttaja_id, Merkinta.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinta JOIN Kayttaja ON Merkinta.kayttaja_id = Kayttaja.id WHERE kayttaja_id = ? ORDER BY Merkinta.paivamaara DESC";
		Object[] parametrit = { kayttajaId };
		RowMapper<Merkinta> mapper = new MerkintaRowMapper();

		List<Merkinta> merkinnat = jdbcTemplate.query(sql, mapper);
		return merkinnat;
	}

	public void poistaMerkinta(int merkintaId) {
		String sql = "DELETE FROM Merkinta WHERE id = ?";
		Object[] parametrit = { merkintaId };
		jdbcTemplate.update(sql, parametrit);
	}
}
