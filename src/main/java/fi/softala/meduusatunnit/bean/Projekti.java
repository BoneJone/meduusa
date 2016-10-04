package fi.softala.meduusatunnit.bean;

import java.util.Date;

public interface Projekti {
	
	public abstract int getId();
	public abstract void setId(int id);
	
	public abstract Date getPaivamaara();
	public abstract void setPaivamaara(Date paivamaara);
	
	public abstract String getNimi();
	public abstract void setNimi(String nimi);
	
	public abstract String getKuvaus();
	public abstract void setKuvaus(String kuvaus);

}
