package me.nicou.tuntikirjaus.bean;

import java.util.List;

public class MerkintaListaImpl implements MerkintaLista {

	int nykyinenSivu;
	int sivujaYhteensa;
	int merkintojaPerSivu;
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

	@Override
	public String toString() {
		return "MerkintaListaImpl [nykyinenSivu=" + nykyinenSivu
				+ ", sivujaYhteensa=" + sivujaYhteensa + ", merkintojaPerSivu="
				+ merkintojaPerSivu + ", merkinnat=" + merkinnat + "]";
	}

}
