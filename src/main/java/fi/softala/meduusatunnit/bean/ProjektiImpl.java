package fi.softala.meduusatunnit.bean;

import java.util.Date;

public class ProjektiImpl {
	
	int id;
	String nimi;
	String kuvaus;
	Date paivamaara;
	
	
	public ProjektiImpl() {
		super();
		
	}

	public ProjektiImpl(int id, String nimi, String kuvaus, Date paivamaara) {
		super();
		this.id = id;
		this.nimi = nimi;
		this.kuvaus = kuvaus;
		this.paivamaara = paivamaara;
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

	public Date getPaivamaara() {
		return paivamaara;
	}

	public void setPaivamaara(Date paivamaara) {
		this.paivamaara = paivamaara;
	}

	@Override
	public String toString() {
		return "ProjektiImpl [id=" + id + ", nimi=" + nimi + ", kuvaus="
				+ kuvaus + ", paivamaara=" + paivamaara + "]";
	}

}
