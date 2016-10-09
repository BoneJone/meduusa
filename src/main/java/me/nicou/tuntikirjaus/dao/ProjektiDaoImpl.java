package me.nicou.tuntikirjaus.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import me.nicou.tuntikirjaus.bean.Merkinta;

@Repository("projektiDao")
public class ProjektiDaoImpl implements ProjektiDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public ProjektiDaoImpl() {
		super();
	}
	
	public List<Merkinta> haeKaikkiMerkinnat() {
		String sql = "SELECT Kayttajat.id AS kayttaja_id, Merkinnat.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinnat JOIN Kayttajat ON Merkinnat.kayttaja_id = Kayttajat.id ORDER BY Merkinnat.paivamaara DESC";
		RowMapper<Merkinta> mapper = new MerkintaRowMapper();
		List<Merkinta> merkinnat = jdbcTemplate.query(sql, mapper);
		return merkinnat;
	}

}
