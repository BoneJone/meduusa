package me.nicou.tuntikirjaus;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecuritySessionInitializer extends AbstractSecurityWebApplicationInitializer {

		public SecuritySessionInitializer() {
			super(WebSecurityConfig.class, EmbeddedRedisConfig.class);
		}
}
