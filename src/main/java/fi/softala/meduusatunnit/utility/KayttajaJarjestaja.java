package fi.softala.meduusatunnit.utility;

import java.util.Comparator;

import fi.softala.meduusatunnit.bean.Kayttaja;

public class KayttajaJarjestaja implements Comparator<Kayttaja> {
	
	@Override
	public int compare(Kayttaja k1, Kayttaja k2) {
		if (k1.getTunnitYhteensa() > k2.getTunnitYhteensa()) {
			return -1;
		}
		else if (k1.getTunnitYhteensa() < k2.getTunnitYhteensa()) {
			return 1;
		}
		else {
			return 0;
		}
	}

}
