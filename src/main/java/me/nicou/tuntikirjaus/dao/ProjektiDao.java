package me.nicou.tuntikirjaus.dao;

import java.util.List;

import me.nicou.tuntikirjaus.bean.Merkinta;
import me.nicou.tuntikirjaus.bean.Projekti;

public interface ProjektiDao {
	
	public List<Projekti> haeKayttajanProjektit(String sahkoposti);
	
	public Projekti haeProjektinTiedot(int projektiId, String sahkoposti);
	
	public int tallennaMerkinta(int projektiId, Merkinta merkinta);
	
	public List<Merkinta> haeProjektinYhteistunnit(int projektiId);

}
