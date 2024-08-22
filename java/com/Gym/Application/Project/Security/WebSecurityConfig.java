package com.Gym.Application.Project.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
	
	  @Autowired
	   private UserDetailsService userDetailsService;
	  
	  @Autowired
	  private PasswordEncoder passwordEncoder;
	
	
	  @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(userDetailsService);
	        authProvider.setPasswordEncoder(passwordEncoder());
	        return authProvider;
	    }

	    @Autowired
	    public void configure(AuthenticationManagerBuilder auth) throws Exception {
	    	auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        HttpStatusReturningLogoutSuccessHandler logoutSuccessHandler = new HttpStatusReturningLogoutSuccessHandler();

	        http
	            .authorizeHttpRequests(authorize -> authorize
	                .requestMatchers("/resources/**").permitAll()
	                .requestMatchers("/","/home", "/login", "/register", "/about", "/profile/edit", "/logout", "/logout-success","/placeorder").permitAll()
	                .anyRequest().authenticated())
	            .formLogin(formLogin -> formLogin
	                .loginPage("/login")
	                .permitAll()
	                .failureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error")))
	            .logout(logout -> logout
	                .logoutUrl("/logout")
	                .logoutSuccessUrl("/")
	                .permitAll())
	            .csrf(csrf -> csrf.disable());  // Disable CSRF protection for debugging

	        return http.build();
	    }
	    
	    @Bean
	    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
	        StrictHttpFirewall firewall = new StrictHttpFirewall();
	        firewall.setAllowUrlEncodedSlash(true);
	        firewall.setAllowSemicolon(true);
	        firewall.setAllowUrlEncodedDoubleSlash(true); // Allow double slashes
	        return firewall;
	    }
	
	
}
