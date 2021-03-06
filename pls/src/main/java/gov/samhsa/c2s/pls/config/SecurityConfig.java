package gov.samhsa.c2s.pls.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
public class SecurityConfig {

    private static final String RESOURCE_ID = "pls";

    @Bean
    public ResourceServerConfigurer resourceServer(SecurityProperties securityProperties) {
        return new ResourceServerConfigurerAdapter() {
            @Override
            public void configure(ResourceServerSecurityConfigurer resources) {
                resources.resourceId(RESOURCE_ID);
            }

            @Override
            public void configure(HttpSecurity http) throws Exception {

                http.authorizeRequests()
                        // Security scope for accessing management endpoint
                        .antMatchers(HttpMethod.GET, "/management/**").access("#oauth2.hasScope('pls.management')")
                        .antMatchers(HttpMethod.POST, "/management/**").access("#oauth2.hasScope('pls.management')")
                        .antMatchers(HttpMethod.GET, "/providers/**").access("#oauth2.hasScope('pls.read')")
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().denyAll();
            }
        };
    }
}