package me.nicou.tuntikirjaus.dao;

import java.util.List;

import me.nicou.tuntikirjaus.bean.EtusivunMerkinta;
import me.nicou.tuntikirjaus.bean.Kayttaja;

public interface KayttajaDao {
	
	public Kayttaja haeKayttajaSahkopostilla(String sahkoposti);
	
	public List<EtusivunMerkinta> haeEtusivunMerkinnat(String sahkoposti);
	
	public double haeKayttajanKuukaudenYhteistunnit(String sahkoposti);

}
