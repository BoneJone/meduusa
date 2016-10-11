package me.nicou.tuntikirjaus.dao;

import me.nicou.tuntikirjaus.bean.Kayttaja;

public interface KayttajaDao {
	
	public Kayttaja haeKayttajaSahkopostilla(String sahkoposti);

}
