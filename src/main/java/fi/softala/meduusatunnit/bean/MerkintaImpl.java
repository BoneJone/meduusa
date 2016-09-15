package fi.softala.meduusatunnit.bean;

import java.util.Date;

public class MerkintaImpl implements Merkinta{

	int id;
	Kayttaja kayttaja;
	Date paivamaara;
	double tunnit;
	String kuvaus;

	public MerkintaImpl() {
		super();
	}

	public MerkintaImpl(int id, Kayttaja kayttaja, Date paivamaara,
			double tunnit, String kuvaus) {
		super();
		this.id = id;
		this.kayttaja = kayttaja;
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
	
	public Kayttaja getKayttaja() {
		return kayttaja;
	}

	public void setKayttaja(Kayttaja kayttaja) {
		this.kayttaja = kayttaja;
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
		return "MerkintaImpl [id=" + id + ", kayttaja=" + kayttaja
				+ ", paivamaara=" + paivamaara + ", tunnit=" + tunnit
				+ ", kuvaus=" + kuvaus + "]";
	}

}
