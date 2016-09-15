package fi.softala.meduusatunnit.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fi.softala.meduusatunnit.bean.Kayttaja;
import fi.softala.meduusatunnit.bean.Merkinta;
import fi.softala.meduusatunnit.dao.Dao;
import fi.softala.meduusatunnit.utility.KayttajaJarjestaja;
import fi.softala.meduusatunnit.utility.MerkintaJarjestaja;
import fi.softala.meduusatunnit.utility.Slack;

/**
 * Servlet implementation class Kontrolleri
 */
@WebServlet(name = "kontrolleri", urlPatterns = { "/kontrolleri" })
public class Kontrolleri extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Kontrolleri() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Katsotaan onko valittu joku tietty kayttaja tai poisto
		String kayttajaParam = request.getParameter("kayttaja");
		String poistoParam = request.getParameter("poista");
		
		// Jos kayttaja-parametrissa on kokonaisluku niin lahetetaan se parametrina naytaSivulle
		// Jos ei, kutsutaan naytaSivua ilman parametria
		if (kayttajaParam != null) {
			try {
				Integer.valueOf(kayttajaParam);
				naytaSivu(request, response, null, kayttajaParam);
			} catch (Exception ex) {
				System.out.println(ex);
				naytaSivu(request, response, null, null);
			}
		}
		else if (poistoParam != null) {
			// Jos poista-parametri oli asetettu, katsotaan onko se kokonaisluku,
			// jos on, niin yritetään poistaa sillä ID:llä merkintä kannasta
			// Sen jälkeen näytetään sivu normaalisti
			try {
				Integer.valueOf(poistoParam);
				Dao dao = new Dao();
				HashMap<String, String> vastaus = dao.poistaMerkinta(poistoParam);
				if (vastaus.containsKey("success")) {
					naytaSivu(request, response, vastaus.get("success"), null);
				} else if (vastaus.containsKey("virhe")) {
					naytaSivu(request, response, vastaus.get("virhe"), null);
				}
				else {
					naytaSivu(request, response, null, null);
				}
			} catch (Exception ex) {
				System.out.println(ex);
				naytaSivu(request, response, null, null);
			}
		}
		else {
			naytaSivu(request, response, null, null);
		}
	}
	
	// Käydään läpi kaikki tietokannasta haetut käyttäjät ja palautetaan vastauksena
	// yksi ArrayList josta löytyy kaikkien käyttäjien merkinnät
	public ArrayList<Merkinta> haeMerkinnat(ArrayList<Kayttaja> kayttajat) {
		ArrayList<Merkinta> merkinnat = new ArrayList<Merkinta>();
		for (int i = 0; i < kayttajat.size(); i++) {
			try {
			for (int j = 0; j < kayttajat.get(i).getMerkinnat().size(); j++) {
				merkinnat.add(kayttajat.get(i).getMerkinnat().get(j));
			}
			} catch (Exception ex) {
				System.out.println("Virhe haeMerkinnassa: " + ex);
			}
		}
		
		// Järjestetään merkinnät päivämäärän mukaan
		Collections.sort(merkinnat, new MerkintaJarjestaja());
		return merkinnat;
	}
	
	// Kutsuu DAO-luokkaa, joka hakee tietokannasta käyttäjätiedot ja merkinnät
	public ArrayList<Kayttaja> haeKayttajat() {
		Dao dao = new Dao();
		ArrayList<Kayttaja> kayttajat = dao.haeKayttajat();
		
		// Järjestetään käyttäjät isoimman tuntimäärän mukaan
		Collections.sort(kayttajat, new KayttajaJarjestaja());
		return kayttajat;
	}
	
	// Metodi varsinaisen sivun näyttöön
	protected void naytaSivu(HttpServletRequest request, HttpServletResponse response, String viesti, String id) throws ServletException, IOException {
		// Asetetaan enkoodaus UTF-8
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		// Katsotaan, onko error/success-viestiä näytettäväksi sivulle
		// Jos on, asetetaan parametriksi
		if (viesti != null) {
			request.setAttribute("viesti", viesti);
		}
		
		// Kutsutaan DAO-luokkaa ja haetaan käyttäjät merkintöineen
		ArrayList<Kayttaja> kayttajat = haeKayttajat();
		
		// Alustetaan tyhjä kayttaja-olio
		Kayttaja kayttaja = null;
		
		// Jos on saatu doGet-metodilta id-parametri, katsotaan löytyykö vastaavaa
		// id:tä käyttäjälistalta
		if (id != null) {
			for (int i = 0; i < kayttajat.size(); i++) {
				if (String.valueOf(kayttajat.get(i).getId()).equals(id)) {
					kayttaja = kayttajat.get(i);
					request.setAttribute("kayttaja", kayttaja);
					i = kayttajat.size();
				}
			}
		}
		
		// Haetaan kaikkien käyttäjien merkinnät mikäli ID:tä ei oltu määritelty
		// tai sitä ei löytynyt
		if (kayttajat.size() > 0 && kayttaja == null) {
			ArrayList<Merkinta> merkinnat = haeMerkinnat(kayttajat);
			request.setAttribute("merkinnat", merkinnat);
		}
		
		// Attribuuttien asetus
		request.setAttribute("kayttajat", kayttajat);
		
		// Request dispatcher
		RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/sivu.jsp");
		rd.forward(request, response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Parametrien haku
		String nimi = request.getParameter("nimi");
		String tunnit = request.getParameter("tunnit");
		String minuutit = request.getParameter("minuutit");
		String kuvaus = request.getParameter("kuvaus");
		String slack[] = request.getParameterValues("slack");
		
		try {
			// Parsitaan integereiksi, voisi vetästä myös suoraan doubleksi
			int tunnitInt = Integer.parseInt(tunnit);
			int minuutitInt = Integer.parseInt(minuutit);
			
			// Tehdään pienimuotoinen validointi
			if ((tunnitInt >= 0 && tunnitInt < 13 && minuutitInt >= 0 && minuutitInt < 60) && tunnitInt + minuutitInt > 0) {
				// Lasketaan tuntimäärä tunneista ja minuuteista doubleksi
				double tunnitYht = Double.valueOf(tunnitInt) + (Double.valueOf(minuutitInt) / 60);

				// Lähetetään pyyntö DAO-luokkaan
				Dao dao = new Dao();
				HashMap<String, String> vastaus = dao.lisaaMerkinta(nimi, tunnitYht, kuvaus);
				
				// Katsotaan vastaus, näytetään sen mukaan sivulla viesti
				if (vastaus.containsKey("success")) {
					System.out.println("Onnistui!");
					
					// Jos Slack-checkbox valittu niin viesti Slackiin
					if (slack != null && slack[0] != null && slack[0].equals("yes")) {
					Slack slackbot = new Slack();
					String slackviesti = nimi + " kirjasti " + tunnit + "h";
					if (minuutitInt > 0) {
						slackviesti += " " + minuutit + "min";
					}
					slackviesti +=  " työtä (" + kuvaus + ")";
					slackbot.lahetaViesti(slackviesti);
					}
					
					naytaSivu(request, response, vastaus.get("success"), null);
				
				} else if (vastaus.containsKey("virhe")) {
					System.out.println("Ei onnistunut - " + vastaus.get("virhe"));
					naytaSivu(request, response, vastaus.get("virhe"), null);
				}
				else {
					naytaSivu(request, response, "Jotain outoa tapahtui", null);
				}
			} else {
				naytaSivu(request, response, "Tuntien määrä oltava 1-12h", null);
			}
			
		} catch (Exception ex) {
			System.out.println("Errori");
		}
		
	}

}
