package me.nicou.tuntikirjaus.dao;

import java.util.List;

import me.nicou.tuntikirjaus.bean.Merkinta;

public interface ProjektiDao {
	
	public List<Merkinta> haeKaikkiMerkinnat();

}
