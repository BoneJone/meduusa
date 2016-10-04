package fi.softala.meduusatunnit.dao;

import java.util.List;

import fi.softala.meduusatunnit.bean.Merkinta;
import fi.softala.meduusatunnit.bean.Projekti;

public interface MerkintaDAO {

	public abstract int tallennaMerkinta(Merkinta merkinta);

	public abstract List<Merkinta> haeKaikkiMerkinnat();
	
	public abstract List<Merkinta> haeSeuraavatMerkinnat(int projektiId, int offset, int maara);
	
	public abstract List<Merkinta> haeYhdenKayttajanMerkinnat(int kayttajaId);
	
	public abstract int poistaMerkinta(int merkintaId);
	
	public abstract List<Merkinta> haeTunnitYhteensa();
	
	public abstract void lisaaProjekti (Projekti projekti);
	
}
