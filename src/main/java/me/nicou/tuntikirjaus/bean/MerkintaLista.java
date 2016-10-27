package me.nicou.tuntikirjaus.bean;

import java.util.List;

public interface MerkintaLista {
	
	public abstract int getNykyinenSivu();
	public abstract void setNykyinenSivu(int nykyinenSivu);
	
	public abstract int getSivujaYhteensa();
	public abstract void setSivujaYhteensa(int sivujaYhteensa);
	
	public abstract int getMerkintojaPerSivu();
	public abstract void setMerkintojaPerSivu(int merkintojaPerSivu);
	
	public abstract List<Merkinta> getMerkinnat();
	public abstract void setMerkinnat(List<Merkinta> merkinnat);
	
	public abstract int getEnsimmainenSivunumero();
	public abstract void setEnsimmainenSivunumero(int ensimmainenSivunumero);

	public abstract int getViimeinenSivunumero();
	public abstract void setViimeinenSivunumero(int viimeinenSivunumero);
	
	public abstract int getNaytettavatSivunumerot();
	
	public abstract void laskeRenderoitavatSivunumerot();

}