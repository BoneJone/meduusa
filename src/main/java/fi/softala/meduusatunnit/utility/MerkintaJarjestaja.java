package fi.softala.meduusatunnit.utility;

import java.util.Comparator;

import fi.softala.meduusatunnit.bean.Merkinta;

public class MerkintaJarjestaja implements Comparator<Merkinta> {

	@Override
	public int compare(Merkinta m1, Merkinta m2) {
		int i = m1.getPaivamaara().compareTo(m2.getPaivamaara());
		if (i == 0) {
			i = -1;
		}
		else if (i == -1) {
			i = 0;
		}
		return i;
	}
}
