package fi.softala.meduusatunnit.dao;

import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fi.softala.meduusatunnit.bean.Kayttaja;
import fi.softala.meduusatunnit.bean.Merkinta;
import fi.softala.meduusatunnit.dao.Kysely;
import fi.softala.meduusatunnit.dao.Paivitys;
import fi.softala.meduusatunnit.dao.Yhteys;

public class Dao {
	
	public ArrayList<Kayttaja> haeKayttajat() {
		ArrayList<Kayttaja> kayttajat = new ArrayList<Kayttaja>();

		Yhteys yhteys = new Yhteys();
		if (yhteys.getYhteys() == null) {
			return kayttajat;
		}
		
		Kysely kysely = new Kysely(yhteys.getYhteys());
		
		String sql = "SELECT Kayttaja.id AS kayttaja_id, Merkinta.id AS merkinta_id, sahkoposti, etunimi, sukunimi, paivamaara, tunnit, kuvaus FROM Merkinta JOIN Kayttaja ON Merkinta.kayttaja_id = Kayttaja.id ORDER BY Merkinta.paivamaara DESC";

		kysely.suoritaKysely(sql);
		
		kysely.getTulokset();
		Iterator iteraattori = kysely.getTulokset().iterator();

		while (iteraattori.hasNext()) {
			HashMap mappi = (HashMap) iteraattori.next();
			
			String kayttajaIdStr = (String) mappi.get("kayttaja_id");
			String sahkoposti = (String) mappi.get("sahkoposti");
			String etunimi = (String) mappi.get("etunimi");
			String sukunimi = (String) mappi.get("sukunimi");
			
			String merkintaIdStr = (String) mappi.get("merkinta_id");
			String paivaStr = (String) mappi.get("paivamaara");
			String tunnitStr = (String) mappi.get("tunnit");
			String kuvaus = (String) mappi.get("kuvaus");

			double tunnit;
			int kayttajaId;
			int merkintaId;
			String kokonimi = etunimi + " " + sukunimi;

			try {;
				tunnit = Double.valueOf(tunnitStr);
				kayttajaId = Integer.valueOf(kayttajaIdStr);
				merkintaId = Integer.valueOf(merkintaIdStr);
				Date paivamaara = new Date(Timestamp.valueOf(paivaStr).getTime());
				
				Merkinta merkinta = new Merkinta(merkintaId, kokonimi, paivamaara, tunnit, kuvaus);
				
				boolean loyty = false;
				
				for (int i = 0; i < kayttajat.size(); i++) {
					if (kayttajat.get(i).getId() == kayttajaId && loyty == false) {
						kayttajat.get(i).getMerkinnat().add(merkinta);
						kayttajat.get(i).setTunnitYhteensa(kayttajat.get(i).getTunnitYhteensa() + merkinta.getTunnit());
						loyty = true;
						i = kayttajat.size();
					}
				}
				
				if (!loyty) {
					ArrayList<Merkinta> kayttajanMerkinnat = new ArrayList<Merkinta>();
					kayttajanMerkinnat.add(merkinta);
					Kayttaja kayttaja = new Kayttaja(kayttajaId, sahkoposti, etunimi, sukunimi, kayttajanMerkinnat, merkinta.getTunnit());
					kayttajat.add(kayttaja);
				}
				
			} catch (Exception ex) {
				System.out.println("Virhe tietokantahakua parsiessa!");
				return kayttajat;
			}
		}

		// Yhteyden sulkeminen
		yhteys.suljeYhteys();
		
		return kayttajat;
	}
	
	// Merkinnän poistaminen
	public HashMap<String, String> poistaMerkinta(String id) {
		HashMap<String, String> vastaus = new HashMap<>();
		Yhteys yhteys = new Yhteys();
		if (yhteys.getYhteys() == null) {
			String virhe = "Tietokantayhteyttä ei saatu avattua";
			vastaus.put("virhe", virhe);
			return vastaus;
		}
		Paivitys paivitys = new Paivitys(yhteys.getYhteys());

		ArrayList<String> parametrit = new ArrayList<String>();
		parametrit.add(id);
		
		String sql = "DELETE FROM Merkinta WHERE id = ?";
		
		int onnistui = paivitys.suoritaSqlLauseParametreilla(sql, parametrit);

		if (onnistui > 0) {
			String success = "Merkintä poistettu tietokannasta!";
			vastaus.put("success", success);
			yhteys.suljeYhteys();
			return vastaus;
		} else {
			String virhe = "Merkinnän poistossa tapahtui virhe!";
			vastaus.put("virhe", virhe);
			yhteys.suljeYhteys();
			return vastaus;
		}
	}
	
	public HashMap<String, String> lisaaMerkinta(String kayttaja, double tunnit, String kuvaus) {
		HashMap<String, String> vastaus = new HashMap<>();
		Yhteys yhteys = new Yhteys();
		if (yhteys.getYhteys() == null) {
			String virhe = "Tietokantayhteyttä ei saatu avattua";
			vastaus.put("virhe", virhe);
			return vastaus;
		}
		Paivitys paivitys = new Paivitys(yhteys.getYhteys());

		ArrayList<String> parametrit = new ArrayList<String>();
		parametrit.add(kayttaja);
		
		String sql = "SELECT id FROM Kayttaja WHERE sahkoposti = ?";
		
		Kysely kysely = new Kysely(yhteys.getYhteys());
		
		if (kysely.montaRivia(sql, parametrit) != 1) {
			String virhe = "Et kuulu Meduusan ryhmään!";
			vastaus.put("virhe", virhe);
			yhteys.suljeYhteys();
			return vastaus;
		}
		
		sql = "INSERT INTO Merkinta (kayttaja_id, tunnit, kuvaus) VALUES((SELECT id FROM Kayttaja WHERE sahkoposti = ?), ?, ?)";
		parametrit.add(String.valueOf(tunnit));
		parametrit.add(kuvaus);

		int onnistui = paivitys.suoritaSqlLauseParametreilla(sql, parametrit);

		if (onnistui > 0) {
			String success = "Merkintä tallennettu tietokantaan!";
			vastaus.put("success", success);
			yhteys.suljeYhteys();
			return vastaus;
		} else {
			String virhe = "Tietokantavirhe merkintää tallentaessa!";
			vastaus.put("virhe", virhe);
			yhteys.suljeYhteys();
			return vastaus;
		}

	}

}