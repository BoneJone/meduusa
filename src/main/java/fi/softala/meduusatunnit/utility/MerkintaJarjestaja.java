package fi.softala.meduusatunnit.utility;

import java.util.Comparator;

import fi.softala.meduusatunnit.bean.Merkinta;

public class MerkintaJarjestaja implements Comparator<Merkinta> {

	// Järjestää merkinnät suurimmasta tuntimäärästä pienimpään
	@Override
	public int compare(Merkinta m1, Merkinta m2) {
		if (m1.getTunnit() > m2.getTunnit()) {
			return -1;
		}
		else if (m1.getTunnit() < m2.getTunnit()) {
			return 1;
		}
		else {
			return 0;
		}
	}
}
