package com.edwardlee.library.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置
 * @author EdwardLee
 * @date 2020/12/15
 */

@Configuration
@EnableWebSecurity  // Add Filter chain
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        String userName = "admin";
        String password = encoder().encode("admin");
        logger.info("加密后：\t" + password);
        auth.inMemoryAuthentication()
                .withUser(userName).password(password)
                .authorities("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // HttpBasic
        http.httpBasic()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
        ;
    }
}
