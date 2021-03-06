package org.persistent.studentservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class PropertySourceSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf()
          .ignoringAntMatchers("/encrypt/**")
          .ignoringAntMatchers("/decrypt/**");
        super.configure(http);
    }
}
