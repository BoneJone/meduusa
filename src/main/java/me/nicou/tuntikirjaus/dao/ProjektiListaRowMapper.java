package me.nicou.tuntikirjaus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import me.nicou.tuntikirjaus.bean.Projekti;
import me.nicou.tuntikirjaus.bean.ProjektiImpl;

public class ProjektiListaRowMapper implements RowMapper<Projekti>{
	
	public Projekti mapRow(ResultSet rs, int row) throws SQLException {
		Projekti projekti = new ProjektiImpl();
		projekti.setId(rs.getInt("id"));
		projekti.setNimi(rs.getString("nimi"));
		projekti.setKuvaus(rs.getString("kuvaus"));
		projekti.setLuontipaiva(rs.getTimestamp("luontipaiva"));
		projekti.setYhteistunnit(rs.getDouble(5));
		
		return projekti;
	}

}
