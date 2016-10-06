package fi.softala.meduusatunnit.bean;

import java.util.Date;

public class ProjektiImpl implements Projekti {

	int id;
	String nimi;
	String kuvaus;
	Date luontipaiva;
	int yhteistunnit;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNimi() {
		return nimi;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}

	public String getKuvaus() {
		return kuvaus;
	}

	public void setKuvaus(String kuvaus) {
		this.kuvaus = kuvaus;
	}

	public Date getLuontipaiva() {
		return luontipaiva;
	}

	public void setLuontipaiva(Date luontipaiva) {
		this.luontipaiva = luontipaiva;
	}

	public int getYhteistunnit() {
		return yhteistunnit;
	}

	public void setYhteistunnit(int yhteistunnit) {
		this.yhteistunnit = yhteistunnit;
	}

}
