package fi.softala.meduusatunnit.controller;

import java.util.List;

import fi.softala.meduusatunnit.bean.Merkinta;
import fi.softala.meduusatunnit.dao.MerkintaDAO;
import fi.softala.meduusatunnit.dao.MerkintaDAOSpringJdbcImpl;


public class Tester {

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		
		MerkintaDAO dao = new MerkintaDAOSpringJdbcImpl();
		
		List<Merkinta> merkinnat = dao.haeKaikkiMerkinnat();
		
		for (Merkinta m : merkinnat) {
			System.out.println(m.toString());
		}

	}

}
