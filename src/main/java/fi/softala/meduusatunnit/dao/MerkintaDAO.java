package fi.softala.meduusatunnit.dao;

import java.util.List;

import fi.softala.meduusatunnit.bean.Merkinta;

public interface MerkintaDAO {

	public abstract void tallennaMerkinta(Merkinta merkinta);

	public abstract List<Merkinta> haeKaikkiMerkinnat();
	
	public abstract List<Merkinta> haeYhdenKayttajanMerkinnat(int kayttajaId);
	
	public abstract void poistaMerkinta(int merkintaId);
	
}
