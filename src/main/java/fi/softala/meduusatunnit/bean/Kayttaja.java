package fi.softala.meduusatunnit.bean;

import java.util.ArrayList;

public class Kayttaja {

	int id;
	String sahkoposti;
	String etunimi;
	String sukunimi;
	ArrayList<Merkinta> merkinnat;
	double tunnitYhteensa;

	public Kayttaja() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Kayttaja(int id, String sahkoposti, String etunimi, String sukunimi, ArrayList<Merkinta> merkinnat,
			double tunnitYhteensa) {
		super();
		this.id = id;
		this.sahkoposti = sahkoposti;
		this.etunimi = etunimi;
		this.sukunimi = sukunimi;
		this.merkinnat = merkinnat;
		this.tunnitYhteensa = tunnitYhteensa;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSahkoposti() {
		return sahkoposti;
	}

	public void setSahkoposti(String sahkoposti) {
		this.sahkoposti = sahkoposti;
	}

	public String getEtunimi() {
		return etunimi;
	}

	public void setEtunimi(String etunimi) {
		this.etunimi = etunimi;
	}

	public String getSukunimi() {
		return sukunimi;
	}

	public void setSukunimi(String sukunimi) {
		this.sukunimi = sukunimi;
	}

	public ArrayList<Merkinta> getMerkinnat() {
		return merkinnat;
	}

	public void setMerkinnat(ArrayList<Merkinta> merkinnat) {
		this.merkinnat = merkinnat;
	}

	public double getTunnitYhteensa() {
		return tunnitYhteensa;
	}

	public void setTunnitYhteensa(double tunnitYhteensa) {
		this.tunnitYhteensa = tunnitYhteensa;
	}

	@Override
	public String toString() {
		return "Kayttaja [id=" + id + ", sahkoposti=" + sahkoposti + ", etunimi=" + etunimi + ", sukunimi=" + sukunimi
				+ ", merkinnat=" + merkinnat + ", tunnitYhteensa=" + tunnitYhteensa + "]";
	}

}
