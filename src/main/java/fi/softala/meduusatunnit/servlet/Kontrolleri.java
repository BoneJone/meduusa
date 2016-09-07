package fi.softala.meduusatunnit.servlet;

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
		String kayttajaParam = request.getParameter("kayttaja");
		if (kayttajaParam != null) {
			try {
				int kayttajaId = Integer.valueOf(kayttajaParam);
				naytaSivu(request, response, null, kayttajaParam);
			} catch (Exception ex) {
				System.out.println(ex);
				naytaSivu(request, response, null, null);
			}
		}
		else {
			naytaSivu(request, response, null, null);
		}
	}
	
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
		
		// Järjestetään päivämäärän mukaan
		Collections.sort(merkinnat, new MerkintaJarjestaja());
		return merkinnat;
	}
	
	public ArrayList<Kayttaja> haeKayttajat() {
		Dao dao = new Dao();
		ArrayList<Kayttaja> kayttajat = dao.haeKayttajat();
		return kayttajat;
	}
	
	protected void naytaSivu(HttpServletRequest request, HttpServletResponse response, String viesti, String id) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		if (viesti != null) {
			request.setAttribute("viesti", viesti);
		}
		ArrayList<Kayttaja> kayttajat = haeKayttajat();
		if (id != null) {
			for (int i = 0; i < kayttajat.size(); i++) {
				if (String.valueOf(kayttajat.get(i).getId()).equals(id)) {
					request.setAttribute("kayttaja", kayttajat.get(i));
					i = kayttajat.size();
				}
			}
		}
		else {
			ArrayList<Merkinta> merkinnat = haeMerkinnat(kayttajat);
			request.setAttribute("merkinnat", merkinnat);
		}
		request.setAttribute("kayttajat", kayttajat);
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
			int tunnitInt = Integer.parseInt(tunnit);
			int minuutitInt = Integer.parseInt(minuutit);
			
			if (tunnitInt > 0 && tunnitInt < 13 && minuutitInt >= 0 && minuutitInt < 60) {
				double tunnitYht = Double.valueOf(tunnitInt) + (Double.valueOf(minuutitInt) / 60);
				Dao dao = new Dao();
				HashMap<String, String> vastaus = dao.lisaaMerkinta(nimi, tunnitYht, kuvaus);
				
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
