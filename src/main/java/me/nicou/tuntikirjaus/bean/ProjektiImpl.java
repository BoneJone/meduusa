package me.nicou.tuntikirjaus.bean;

import java.util.Date;
import java.util.List;

public class ProjektiImpl implements Projekti {

	int id;
	String nimi;
	String kuvaus;
	Date luontipaiva;
	List<Merkinta> merkinnat;

	public ProjektiImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProjektiImpl(int id, String nimi, String kuvaus, Date luontipaiva, List<Merkinta> merkinnat) {
		super();
		this.id = id;
		this.nimi = nimi;
		this.kuvaus = kuvaus;
		this.luontipaiva = luontipaiva;
		this.merkinnat = merkinnat;
	}

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

	public List<Merkinta> getMerkinnat() {
		return merkinnat;
	}

	public void setMerkinnat(List<Merkinta> merkinnat) {
		this.merkinnat = merkinnat;
	}

}
