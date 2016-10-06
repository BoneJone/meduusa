package fi.softala.meduusatunnit.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fi.softala.meduusatunnit.bean.Projekti;
import fi.softala.meduusatunnit.bean.ProjektiImpl;

public class ProjektiListaRowMapper implements RowMapper<Projekti>{
	
	public Projekti mapRow(ResultSet rs, int row) throws SQLException {
		Projekti projekti = new ProjektiImpl();
		projekti.setId(rs.getInt("id"));
		projekti.setNimi(rs.getString("nimi"));
		projekti.setKuvaus(rs.getString("kuvaus"));
		projekti.setYhteistunnit(rs.getInt("tunnit"));
		projekti.setLuontipaiva(rs.getTimestamp("luontipaiva"));
		return projekti;
	}

}
