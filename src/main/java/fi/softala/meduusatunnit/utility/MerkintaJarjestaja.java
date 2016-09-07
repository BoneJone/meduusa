package fi.softala.meduusatunnit.utility;

import java.util.Comparator;

import fi.softala.meduusatunnit.bean.Merkinta;

public class MerkintaJarjestaja implements Comparator<Merkinta> {

	@Override
	public int compare(Merkinta m1, Merkinta m2) {
		if (m1.getPaivamaara().before(m2.getPaivamaara())) {
			return 1;
		}
		else if (m1.getPaivamaara().after(m2.getPaivamaara())) {
			return -1;
		}
		else {
			return 0;
		}
	}
}
