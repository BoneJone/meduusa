package fi.softala.meduusatunnit.dao;

import java.util.List;

import fi.softala.meduusatunnit.bean.Merkinta;

public interface MerkintaDAO {

	public abstract int tallennaMerkinta(Merkinta merkinta);

	public abstract List<Merkinta> haeKaikkiMerkinnat();
	
	public abstract List<Merkinta> haeYhdenKayttajanMerkinnat(int kayttajaId);
	
	public abstract int poistaMerkinta(int merkintaId);
	
	public abstract List<Merkinta> haeTunnitYhteensa();
	
}
