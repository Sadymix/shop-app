package com.example.shopapp.configuration;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    public WebSecurityConfiguration() {
        super(true);
    }

    @SneakyThrows
    @Override
    protected void configure(HttpSecurity http) {
        http
                .antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    @SneakyThrows
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }
}
