package fi.softala.meduusatunnit.bean;

import java.util.Date;

public interface Merkinta {

	public abstract int getId();
	public abstract void setId(int id);
	
	public abstract Date getPaivamaara();
	public abstract void setPaivamaara(Date paivamaara);
	
	public abstract double getTunnit();
	public abstract void setTunnit(double tunnit);
	
	public abstract String getKuvaus();
	public abstract void setKuvaus(String kuvaus);
	
	public abstract Kayttaja getKayttaja();
	public abstract void setKayttaja(Kayttaja kayttaja);
		
}
