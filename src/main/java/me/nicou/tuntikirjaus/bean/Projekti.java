package me.nicou.tuntikirjaus.bean;

import java.util.Date;
import java.util.List;

public interface Projekti {
	
	public abstract int getId();
	public abstract void setId(int id);
	
	public abstract Date getLuontipaiva();
	public abstract void setLuontipaiva(Date luontipaiva);
	
	public abstract String getNimi();
	public abstract void setNimi(String nimi);
	
	public abstract String getKuvaus();
	public abstract void setKuvaus(String kuvaus);
	
	public abstract List<Merkinta> getMerkinnat();
	public abstract void setMerkinnat(List<Merkinta> merkinnat);

}
