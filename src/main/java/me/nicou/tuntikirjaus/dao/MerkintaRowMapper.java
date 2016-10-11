package me.nicou.tuntikirjaus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import me.nicou.tuntikirjaus.bean.Kayttaja;
import me.nicou.tuntikirjaus.bean.KayttajaImpl;
import me.nicou.tuntikirjaus.bean.Merkinta;
import me.nicou.tuntikirjaus.bean.MerkintaImpl;

public class MerkintaRowMapper implements RowMapper<Merkinta>{
	
	public Merkinta mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Merkinta merkinta = new MerkintaImpl();
		merkinta.setId(rs.getInt("merkinta_id"));
		merkinta.setPaivamaara(rs.getTimestamp("paivamaara"));
		merkinta.setTunnit(rs.getDouble("tunnit"));
		merkinta.setKuvaus(rs.getString("kuvaus"));

		Kayttaja kayttaja = new KayttajaImpl();
		kayttaja.setId(rs.getInt("kayttaja_id"));
		kayttaja.setSahkoposti(rs.getString("sahkoposti"));
		kayttaja.setEtunimi(rs.getString("etunimi"));
		kayttaja.setSukunimi(rs.getString("sukunimi"));

		merkinta.setKayttaja(kayttaja);
		return merkinta;
				
	}

}
