package me.nicou.tuntikirjaus.dao;

import java.util.List;

import me.nicou.tuntikirjaus.bean.Merkinta;
import me.nicou.tuntikirjaus.bean.Projekti;

public interface ProjektiDao {
	
	public List<Projekti> haeKayttajanProjektit(String sahkoposti);
	
	public Projekti haeProjektinTiedot(int projektiId, String sahkoposti, int sivunumero);
	
	public Projekti haeProjektinTiedotKayttajalta(int projektiId, String sahkoposti, int kayttajaId, int sivunumero);
	
	public int tallennaMerkinta(int projektiId, Merkinta merkinta);
	
	public int poistaKayttajanMerkinta(int merkintaId, String sahkoposti);
	
	public List<Merkinta> haeProjektinJasenet(int projektiId, String sahkoposti);
	
	public abstract int lisaaProjekti(Projekti projekti, String sahkoposti);
	
	public boolean lisaaJasenProjektiin(int projektiId, String lisattavaSahkoposti, String sahkoposti);

}
