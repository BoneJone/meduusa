package me.nicou.tuntikirjaus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.nicou.tuntikirjaus.bean.EtusivunMerkinta;
import me.nicou.tuntikirjaus.bean.EtusivunMerkintaImpl;

import org.springframework.jdbc.core.RowMapper;

public class EtusivunMerkintaRowMapper implements RowMapper<EtusivunMerkinta>{
	
	public EtusivunMerkinta mapRow(ResultSet rs, int rowNum) throws SQLException {
		EtusivunMerkinta merkinta = new EtusivunMerkintaImpl();
		
		merkinta.setProjektiId(rs.getInt("id"));
		merkinta.setProjektiNimi(rs.getString("nimi"));
		merkinta.setKayttajaNimi(rs.getString("etunimi") + " " + rs.getString("sukunimi"));
		merkinta.setTunnit(rs.getDouble("tunnit"));
		merkinta.setPaivamaara(rs.getDate("paivamaara"));
		merkinta.setKuvaus(rs.getString("kuvaus"));
		
		return merkinta;
	}

}
