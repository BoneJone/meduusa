package me.nicou.tuntikirjaus.bean;

public interface Viesti {
	
	public abstract String getOtsikko();
	public abstract void setOtsikko(String otsikko);
	
	public abstract String getViesti();
	public abstract void setViesti(String viesti);
	
	public abstract String getTyyppi();
	public abstract void setTyyppi(String tyyppi);
	
}
