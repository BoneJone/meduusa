package fi.softala.meduusatunnit.dao;

import java.sql.Connection;
import java.sql.DriverManager;

// Luokka tietokantayhteyden avaamiselle ja sulkemiselle

public class Yhteys {

	private Connection yhteys = null;
	
	public Yhteys() {
		avaaYhteys();
	}

	private void avaaYhteys() {
		
		// Asetusten haku
		String username = "a1500955";
		String password = "saGI0L84z";
		String url = "jdbc:mysql://localhost:3306/a1500955";

		try {
			// Ajurin lataus SQLite
			Class.forName("org.mariadb.jdbc.Driver");

			// Yhteyden avaus
			yhteys = DriverManager.getConnection(url, username, password);

		} catch (Exception e) {
			// Virheet
			System.out.println("Tietokantayhteyden avauksessa tapahtui virhe");
			e.printStackTrace();
		}

	}

	public void suljeYhteys() {
		try {
			if (yhteys != null && !yhteys.isClosed())
				yhteys.close();
		} catch (Exception e) {
			System.out.println("Tietokantayhteys ei jostain syystä suostu menemään kiinni.");
		}
	}
	
    public Connection getYhteys() {
        if (yhteys == null) {
            avaaYhteys();
        }

        return yhteys;
    }

}