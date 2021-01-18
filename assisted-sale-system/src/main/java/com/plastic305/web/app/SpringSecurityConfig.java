package com.plastic305.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.plastic305.web.app.services.JPAUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter 
{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder ;
	
	@Autowired
	private JPAUserDetailsService userDetailsService ;
	

	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		http.authorizeRequests().antMatchers("/css/**", "/font/**", "/js/**", "/imgs/**", "/uploads/**").permitAll()
		.anyRequest().authenticated()
		.and()
		    .formLogin()
		        .loginPage("/login")
		    .permitAll()
		.and()
			.logout()
			.permitAll()
		.and()
			.exceptionHandling().accessDeniedPage("/errors/error-403");
		http.csrf().disable();
	}
	
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception
	{
		builder.userDetailsService(userDetailsService)
			   .passwordEncoder(passwordEncoder);
	}
}
