package me.nicou.tuntikirjaus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
    @Autowired
    private UserDetailServiceImpl userDetailService;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userDetailService).passwordEncoder(new BCryptPasswordEncoder(10));
    }
    
    /* Header Session Strategy
	@Bean
	HeaderHttpSessionStrategy sessionStrategy() {
		return new HeaderHttpSessionStrategy();
	} */

	// Rest API security config
	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/api/**")
			.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			.cors()
			//.and()
			//.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.and()
			.httpBasic();
		}
	}
	
	// Webbisivun security config
    @Configuration
    public static class WebWebSecurityConfig extends WebSecurityConfigurerAdapter {
    	
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	            .authorizeRequests()
	                .antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/rekisteroidy").permitAll()
	                .anyRequest().authenticated()
	                .and()
	                .csrf().csrfTokenRepository(new CookieCsrfTokenRepository())
	                .and()
	            .formLogin()
	                .loginPage("/kirjaudu")
	                .permitAll()
	                .and()
	            .logout()
	                .permitAll();
	    }
    }
}
