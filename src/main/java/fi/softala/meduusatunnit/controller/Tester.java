package fi.softala.meduusatunnit.controller;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import fi.softala.meduusatunnit.bean.Merkinta;
import fi.softala.meduusatunnit.dao.MerkintaDAO;
import fi.softala.meduusatunnit.dao.MerkintaDAOSpringJdbcImpl;


public class Tester {

	public static void main(String[] args) {
			
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		MerkintaDAO dao = (MerkintaDAO) context.getBean("daoLuokka");
		
		List<Merkinta> merkinnat = dao.haeKaikkiMerkinnat();
		
		for (Merkinta m : merkinnat) {
			System.out.println(m.toString());
		}

	}

}
