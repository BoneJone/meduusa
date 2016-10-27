package me.nicou.tuntikirjaus.bean;

import java.util.Date;

public class EtusivunMerkintaImpl implements EtusivunMerkinta {

	int projektiId;
	String projektiNimi;
	String kayttajaNimi;
	String kuvaus;
	Date paivamaara;
	double tunnit;

	public int getProjektiId() {
		return projektiId;
	}

	public void setProjektiId(int projektiId) {
		this.projektiId = projektiId;
	}

	public String getProjektiNimi() {
		return projektiNimi;
	}

	public void setProjektiNimi(String projektiNimi) {
		this.projektiNimi = projektiNimi;
	}

	public String getKayttajaNimi() {
		return kayttajaNimi;
	}

	public void setKayttajaNimi(String kayttajaNimi) {
		this.kayttajaNimi = kayttajaNimi;
	}

	public String getKuvaus() {
		return kuvaus;
	}

	public void setKuvaus(String kuvaus) {
		this.kuvaus = kuvaus;
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

}
