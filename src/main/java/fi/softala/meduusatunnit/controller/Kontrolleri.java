package fi.softala.meduusatunnit.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import fi.softala.meduusatunnit.bean.Kayttaja;
import fi.softala.meduusatunnit.bean.KayttajaImpl;
import fi.softala.meduusatunnit.bean.Merkinta;
import fi.softala.meduusatunnit.bean.MerkintaImpl;
import fi.softala.meduusatunnit.dao.MerkintaDAO;
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
    
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
	private MerkintaDAO merkintaDao = (MerkintaDAO) context.getBean("daoLuokka");

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
				int poistettava = Integer.valueOf(poistoParam);
				int rivit = merkintaDao.poistaMerkinta(poistettava);
				
				if (rivit == 1) {
					naytaSivu(request, response, "Merkintä poistettu onnistuneesti!", null);
				} else if (rivit == 0) {
					naytaSivu(request, response, "Merkintää poistaessa tapahtui virhe!", null);
				}
				else {
					naytaSivu(request, response, "Merkintää poistaessa tapahtui jotain odottamatonta!", null);
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
		
		// Kutsutaan DAO-luokkaa ja haetaan merkinnät
		List<Merkinta> merkinnat = merkintaDao.haeKaikkiMerkinnat();
		
		String naytettavat = "kaikki";
		
		// Kikkaillaan totalit omaksi listaksi, joku muu saa miettiä tähän fiksumpaa tapaa
		List<Merkinta> tiimintunnit = new ArrayList<>();
		
		for (int i = 0; i < merkinnat.size(); i++) {
			
			if (tiimintunnit.size() == 0) {
				Merkinta merkinta = new MerkintaImpl();
				merkinta.setKayttaja(merkinnat.get(i).getKayttaja());
				merkinta.setTunnit(merkinnat.get(i).getTunnit());
				tiimintunnit.add(merkinta);
			}
			else {
				boolean loytyi = false;
				for (int j = 0; j < tiimintunnit.size(); j++) {
					if (merkinnat.get(i).getKayttaja().getId() == tiimintunnit.get(j).getKayttaja().getId()) {
						tiimintunnit.get(j).setTunnit(tiimintunnit.get(j).getTunnit() + merkinnat.get(i).getTunnit());
						j = tiimintunnit.size();
						loytyi = true;
					}
				}
				if (!loytyi) {
					Merkinta merkinta = new MerkintaImpl();
					merkinta.setKayttaja(merkinnat.get(i).getKayttaja());
					merkinta.setTunnit(merkinnat.get(i).getTunnit());
					tiimintunnit.add(merkinta);
				}
			}
		}
		
		// Järjestetään tiimin tunnit suurimmasta määrästä pienimpään
		Collections.sort(tiimintunnit, new MerkintaJarjestaja());
		
		// Jos on saatu doGet-metodilta id-parametri, katsotaan löytyykö vastaavaa
		// id:tä käyttäjälistalta
		if (id != null) {
			List<Merkinta> kayttajanMerkinnat = new ArrayList<Merkinta>();
			
			for (int i = 0; i < merkinnat.size(); i++) {
				if (String.valueOf(merkinnat.get(i).getKayttaja().getId()).equals(id)) {
					kayttajanMerkinnat.add(merkinnat.get(i));
				}
			}
			
			if (kayttajanMerkinnat.size() > 0) {
				merkinnat = kayttajanMerkinnat;
				naytettavat = "kayttaja";
			}
		}
		
		request.setAttribute("merkinnat", merkinnat);
		request.setAttribute("tiimintunnit", tiimintunnit);
		request.setAttribute("naytettavat", naytettavat);
		
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
				
				// Muodostetaan olio
				Merkinta merkinta = new MerkintaImpl();
				Kayttaja kayttaja = new KayttajaImpl();
				kayttaja.setSahkoposti(nimi);
				merkinta.setKayttaja(kayttaja);
				merkinta.setTunnit(tunnitYht);
				merkinta.setKuvaus(kuvaus);

				// Lähetetään pyyntö DAO-luokkaan
				int rivit = merkintaDao.tallennaMerkinta(merkinta);
				
				// Katsotaan vastaus, näytetään sen mukaan sivulla viesti
				if (rivit == 1) {
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
					
					naytaSivu(request, response, "Merkintä tallennettu onnistuneesti!", null);
				
				} else if (rivit == 0) {
					System.out.println("Ei onnistunut - rivit = " + rivit);
					naytaSivu(request, response, "Merkintää tallentaessa tapahtui virhe!", null);
				}
				else {
					naytaSivu(request, response, "Merkintää tallentaessa tapahtui ODOTTAMATON virhe!", null);
				}
			} else {
				naytaSivu(request, response, "Virhe! Tuntien määrä oltava 1-12h", null);
			}
			
		} catch (Exception ex) {
			System.out.println("Errori: " + ex);
		}
		
	}

}
