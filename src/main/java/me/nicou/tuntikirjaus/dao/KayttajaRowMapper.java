package me.nicou.tuntikirjaus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import me.nicou.tuntikirjaus.bean.Kayttaja;
import me.nicou.tuntikirjaus.bean.KayttajaImpl;

public class KayttajaRowMapper implements RowMapper<Kayttaja>{
	
	public Kayttaja mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Kayttaja kayttaja = new KayttajaImpl();
		kayttaja.setId(rs.getInt("id"));
		kayttaja.setSahkoposti(rs.getString("sahkoposti"));
		kayttaja.setEtunimi(rs.getString("etunimi"));
		kayttaja.setSukunimi(rs.getString("sukunimi"));
		kayttaja.setSalasana(rs.getString("salasana"));

		return kayttaja;
				
	}

}
