package me.nicou.tuntikirjaus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import me.nicou.tuntikirjaus.bean.Kayttaja;
import me.nicou.tuntikirjaus.dao.KayttajaDaoImpl;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	@Qualifier("kayttajaDao")
	private KayttajaDaoImpl dao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, InternalAuthenticationServiceException {
		Kayttaja kirjautuvaKayttaja = dao.haeKayttajaSahkopostilla(username);
		
		UserDetails kayttaja = new org.springframework.security.core.userdetails.User(
				username,
				kirjautuvaKayttaja.getSalasana(),
				AuthorityUtils.createAuthorityList("USER")
				);
		
		return kayttaja;
	}
	
}
