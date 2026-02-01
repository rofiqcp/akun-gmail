package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/*.html").permitAll()
            .antMatchers("/index.html").permitAll()
            .antMatchers("/dashboard.html").permitAll()
            .antMatchers("/api/status").permitAll()
            .antMatchers("/api/welcome").permitAll()
            .antMatchers("/api/auth/**").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .headers().frameOptions().disable();
    }
}