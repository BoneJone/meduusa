package me.nicou.tuntikirjaus.bean;

import java.util.Date;

public interface Projekti {
	
	public abstract int getId();
	public abstract void setId(int id);
	
	public abstract Date getLuontipaiva();
	public abstract void setLuontipaiva(Date luontipaiva);
	
	public abstract String getNimi();
	public abstract void setNimi(String nimi);
	
	public abstract String getKuvaus();
	public abstract void setKuvaus(String kuvaus);
	
	public abstract MerkintaLista getMerkintaLista();
	public abstract void setMerkintaLista(MerkintaLista merkintaLista);

}
