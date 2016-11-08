package me.nicou.tuntikirjaus.bean;

import java.util.Date;

//Validaattorit
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

public class ProjektiImpl implements Projekti {

	int id;
	
	@Pattern(regexp = "[\\sA-ZÅÄÖa-zåäö-]{2,254}")
	// Hyväksyy välilyönnit, A-Z, a-z, skandit ja merkkimäärä 2-254.
	private String nimi;
	
	@Size (min = 2, max = 2000)
	private String kuvaus;
	
	Date luontipaiva;
	MerkintaLista merkintaLista;
	double yhteistunnit;

	public ProjektiImpl() {
		super();
	}

	public ProjektiImpl(int id, String nimi, String kuvaus, Date luontipaiva,
			MerkintaLista merkintaLista, double yhteistunnit) {
		super();
		this.id = id;
		this.nimi = nimi;
		this.kuvaus = kuvaus;
		this.luontipaiva = luontipaiva;
		this.merkintaLista = merkintaLista;
		this.yhteistunnit = yhteistunnit;
	}

	public double getYhteistunnit() {
		return yhteistunnit;
	}

	public void setYhteistunnit(double yhteistunnit) {
		this.yhteistunnit = yhteistunnit;
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

	public Date getLuontipaiva() {
		return luontipaiva;
	}

	public void setLuontipaiva(Date luontipaiva) {
		this.luontipaiva = luontipaiva;
	}

	public MerkintaLista getMerkintaLista() {
		return merkintaLista;
	}

	public void setMerkintaLista(MerkintaLista merkintaLista) {
		this.merkintaLista = merkintaLista;
	}

	@Override
	public String toString() {
		return "ProjektiImpl [id=" + id + ", nimi=" + nimi + ", kuvaus="
				+ kuvaus + ", luontipaiva=" + luontipaiva + ", merkintaLista="
				+ merkintaLista + ", yhteistunnit=" + yhteistunnit + "]";
	}
}
