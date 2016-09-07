package fi.softala.meduusatunnit.bean;

import java.util.Date;

public class Merkinta {

	int id;
	String kayttajanNimi;
	Date paivamaara;
	double tunnit;
	String kuvaus;

	public Merkinta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Merkinta(int id, String kayttajanNimi, Date paivamaara, double tunnit, String kuvaus) {
		super();
		this.id = id;
		this.kayttajanNimi = kayttajanNimi;
		this.paivamaara = paivamaara;
		this.tunnit = tunnit;
		this.kuvaus = kuvaus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKayttajanNimi() {
		return kayttajanNimi;
	}

	public void setKayttajanNimi(String kayttajanNimi) {
		this.kayttajanNimi = kayttajanNimi;
	}

	public Date getPaivamaara() {
		return paivamaara;
	}

	public void setPaivamaara(Date paivamaara) {
		this.paivamaara = paivamaara;
	}

	public double getTunnit() {
		return tunnit;
	}

	public void setTunnit(double tunnit) {
		this.tunnit = tunnit;
	}

	public String getKuvaus() {
		return kuvaus;
	}

	public void setKuvaus(String kuvaus) {
		this.kuvaus = kuvaus;
	}

	@Override
	public String toString() {
		return "Merkinta [id=" + id + ", kayttajanNimi=" + kayttajanNimi + ", paivamaara=" + paivamaara + ", tunnit="
				+ tunnit + ", kuvaus=" + kuvaus + "]";
	}

}
