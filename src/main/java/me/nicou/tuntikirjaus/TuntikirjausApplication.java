package me.nicou.tuntikirjaus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class TuntikirjausApplication extends SpringBootServletInitializer {

	/*
	 * Tää pitää ottaa pois kommenteista
	 * sillon kun buildtaan .war tomcattia varten
	 * 
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TuntikirjausApplication.class);
	}
	*/
	
	public static void main(String[] args) {
		SpringApplication.run(TuntikirjausApplication.class, args);
	}
}
