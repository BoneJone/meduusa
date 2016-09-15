package fi.softala.meduusatunnit.bean;

public class KayttajaImpl implements Kayttaja{

	int id;
	String sahkoposti;
	String etunimi;
	String sukunimi;
	double tunnitYhteensa;

	public KayttajaImpl() {
		super();
	}

	public KayttajaImpl(int id, String sahkoposti, String etunimi, String sukunimi,
			double tunnitYhteensa) {
		super();
		this.id = id;
		this.sahkoposti = sahkoposti;
		this.etunimi = etunimi;
		this.sukunimi = sukunimi;
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


	public double getTunnitYhteensa() {
		return tunnitYhteensa;
	}

	public void setTunnitYhteensa(double tunnitYhteensa) {
		this.tunnitYhteensa = tunnitYhteensa;
	}

	@Override
	public String toString() {
		return "KayttajaImpl [id=" + id + ", sahkoposti=" + sahkoposti
				+ ", etunimi=" + etunimi + ", sukunimi=" + sukunimi
				+ ", tunnitYhteensa=" + tunnitYhteensa + "]";
	}


	
}
