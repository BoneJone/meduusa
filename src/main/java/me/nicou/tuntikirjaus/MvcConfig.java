package me.nicou.tuntikirjaus;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	
	@Override
	public void addViewControllers(ViewControllerRegistry viewController) {
		//viewController.addViewController("/").setViewName("merkinnat");
		viewController.addViewController("/login").setViewName("login");
	}

}
