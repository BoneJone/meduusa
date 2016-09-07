package fi.softala.meduusatunnit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

// Luokka tietokannan insert / update / delete toiminnoille
// Hyvin pitkälti suora kopio Jukka Juslinin ja Jukka Harjun painonhallintaohjelman esimerkistä

public class Paivitys {
	private Connection yhteys;

	public Paivitys(Connection avattuYhteys) {
		yhteys = avattuYhteys;
	}

	public int suoritaSqlLauseParametreilla(String sqlLause, ArrayList<String> parametrit) {
		int tulokset = 0;

		try {
			PreparedStatement valmisteltuLause = yhteys.prepareStatement(sqlLause);
			for (int i = 0; i < parametrit.size(); i++) {
				valmisteltuLause.setObject(i + 1, parametrit.get(i));
			}

			System.out.println(valmisteltuLause);
			tulokset = valmisteltuLause.executeUpdate();
		} catch (SQLException ex) {
			System.out.println("Virhe kyselyn suorittamisessa - " + ex);
		}
		return tulokset;
	}
	
	public int suoritaSqlParamPalautaAvaimet(String sqlLause, ArrayList<String> parametrit) {
		int avain = -1;

		try {
			PreparedStatement valmisteltuLause = yhteys.prepareStatement(sqlLause, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < parametrit.size(); i++) {
				valmisteltuLause.setObject(i + 1, parametrit.get(i));
			}

			System.out.println(valmisteltuLause);
			
			valmisteltuLause.executeUpdate();
			
			ResultSet rs = valmisteltuLause.getGeneratedKeys();
			rs.next();
			avain = rs.getInt(1);
			
		} catch (SQLException ex) {
			System.out.println("Virhe kyselyn suorittamisessa - " + ex);
		}
		return avain;
	}

	public boolean suoritaSqlLause(String sqlLause) {
		boolean suoritusOk = true;
		Statement statement = null;

		try {
			statement = yhteys.createStatement();
			statement.executeUpdate(sqlLause);
		} catch (SQLException ex) {
			System.out.println("Virhe kyselyn suorittamisessa - " + ex);
			suoritusOk = false;
		} finally {
			try {
				statement.close();
			} catch (SQLException ex) {
				System.out.println("Virhe kyselyn suorittamisessa - " + ex);
			}
		}

		return suoritusOk;
	}
}
