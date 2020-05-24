package com.config.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
		//since password is encoded in ldap file, so looking into https://spring.io/guides/gs/authenticating-ldap/
		//password for user ben is benspassword
		auth.ldapAuthentication()
		.userDnPatterns("uid={0},ou=people")
		.groupSearchBase("ou=groups")
		.contextSource()
		.url("ldap://localhost:8389/dc=springframework,dc=org")
		.and()
		.passwordCompare()
		.passwordEncoder(new BCryptPasswordEncoder())
		.passwordAttribute("userPassword");

	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//order of matcher should always be from most restrictive to least restrictive
		http.authorizeRequests()
		.anyRequest().fullyAuthenticated()
		.and().formLogin();
		
	}
}
