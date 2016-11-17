package me.nicou.tuntikirjaus.bean;

import java.util.Date;


//Validaattorit
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;

public class MerkintaImpl implements Merkinta {

	int id;
	Date paivamaara;
	
	@DecimalMin ("0.25")
	double tunnit;
	
	@Size (max = 5000)
	String kuvaus;
	
	@NotNull
	Kayttaja kayttaja;

	public MerkintaImpl() {
		super();
	}

	public MerkintaImpl(int id, Date paivamaara, double tunnit, String kuvaus, Kayttaja kayttaja) {
		super();
		this.id = id;
		this.paivamaara = paivamaara;
		this.tunnit = tunnit;
		this.kuvaus = kuvaus;
		this.kayttaja = kayttaja;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Kayttaja getKayttaja() {
		return kayttaja;
	}

	public void setKayttaja(Kayttaja kayttaja) {
		this.kayttaja = kayttaja;
	}

	@Override
	public String toString() {
		return "MerkintaImpl [id=" + id + ", paivamaara=" + paivamaara + ", tunnit=" + tunnit + ", kuvaus=" + kuvaus
				+ ", kayttaja=" + kayttaja + "]";
	}

}
