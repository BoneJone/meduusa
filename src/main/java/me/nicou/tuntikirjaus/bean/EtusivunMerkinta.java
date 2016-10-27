package me.nicou.tuntikirjaus.bean;

import java.util.Date;

public interface EtusivunMerkinta {
	
	public int getProjektiId();
	public void setProjektiId(int projektiId);
	
	public String getProjektiNimi();
	public void setProjektiNimi(String projektiNimi);
	
	public String getKayttajaNimi();
	public void setKayttajaNimi(String kayttajaNimi);
	
	public String getKuvaus();
	public void setKuvaus(String kuvaus);
	
	public Date getPaivamaara();
	public void setPaivamaara(Date paivamaara);
	
	public double getTunnit();
	public void setTunnit(double tunnit);

}
