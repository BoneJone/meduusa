package me.nicou.tuntikirjaus.bean;

import java.util.List;

public class MerkintaListaImpl implements MerkintaLista {
	
	final int naytettavatSivunumerot = 7;

	int nykyinenSivu;
	int sivujaYhteensa;
	int merkintojaPerSivu;
	int ensimmainenSivunumero;
	int viimeinenSivunumero;
	List<Merkinta> merkinnat;

	public MerkintaListaImpl() {
		super();
	}

	public MerkintaListaImpl(int nykyinenSivu, int sivujaYhteensa,
			int merkintojaPerSivu, List<Merkinta> merkinnat) {
		super();
		this.nykyinenSivu = nykyinenSivu;
		this.sivujaYhteensa = sivujaYhteensa;
		this.merkintojaPerSivu = merkintojaPerSivu;
		this.merkinnat = merkinnat;
	}
	
	public void laskeRenderoitavatSivunumerot() {
		int sivunumerotMolemminPuolin = (int)(((double) Math.round((this.naytettavatSivunumerot - 1) / 2)));
		int ekaSivu, vikaSivu;
		if (this.sivujaYhteensa <= this.naytettavatSivunumerot) {
			ekaSivu = 1;
			vikaSivu = this.sivujaYhteensa;
		} else if (this.nykyinenSivu + sivunumerotMolemminPuolin >= this.sivujaYhteensa) {
			ekaSivu = this.sivujaYhteensa - this.naytettavatSivunumerot + 1;
			vikaSivu = this.sivujaYhteensa;
		} else {
			if (this.nykyinenSivu - sivunumerotMolemminPuolin < 1) {
				ekaSivu = 1;
			} else {
				ekaSivu = this.nykyinenSivu - sivunumerotMolemminPuolin;
			}
			vikaSivu = ekaSivu + naytettavatSivunumerot - 1;
		}
		this.ensimmainenSivunumero = ekaSivu;
		this.viimeinenSivunumero = vikaSivu;
	}

	public int getNykyinenSivu() {
		return nykyinenSivu;
	}

	public void setNykyinenSivu(int nykyinenSivu) {
		this.nykyinenSivu = nykyinenSivu;
	}

	public int getSivujaYhteensa() {
		return sivujaYhteensa;
	}

	public void setSivujaYhteensa(int sivujaYhteensa) {
		this.sivujaYhteensa = sivujaYhteensa;
	}

	public int getMerkintojaPerSivu() {
		return merkintojaPerSivu;
	}

	public void setMerkintojaPerSivu(int merkintojaPerSivu) {
		this.merkintojaPerSivu = merkintojaPerSivu;
	}

	public List<Merkinta> getMerkinnat() {
		return merkinnat;
	}

	public void setMerkinnat(List<Merkinta> merkinnat) {
		this.merkinnat = merkinnat;
	}
	
	

	public int getEnsimmainenSivunumero() {
		return ensimmainenSivunumero;
	}

	public void setEnsimmainenSivunumero(int ensimmainenSivunumero) {
		this.ensimmainenSivunumero = ensimmainenSivunumero;
	}

	public int getViimeinenSivunumero() {
		return viimeinenSivunumero;
	}

	public void setViimeinenSivunumero(int viimeinenSivunumero) {
		this.viimeinenSivunumero = viimeinenSivunumero;
	}

	public int getNaytettavatSivunumerot() {
		return naytettavatSivunumerot;
	}

	@Override
	public String toString() {
		return "MerkintaListaImpl [nykyinenSivu=" + nykyinenSivu
				+ ", sivujaYhteensa=" + sivujaYhteensa + ", merkintojaPerSivu="
				+ merkintojaPerSivu + ", merkinnat=" + merkinnat + "]";
	}

}
