package com.tgerstel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.tgerstel.repository.UserRepository;
import com.tgerstel.service.MyUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Autowired
   public UserDetailsService userService;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider provider() throws Exception {    	
    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder());
        provider.setUserDetailsService(userService);
        return provider;  
    }    
        
    @Bean
    protected SecurityFilterChain web(HttpSecurity http) throws Exception {
    		http
    			.authorizeHttpRequests(authorize ->	authorize
								.requestMatchers("/", "/login", "/api/register", "/h2-console/**").permitAll()			
								.anyRequest().authenticated()					
				)
    			.httpBasic()
    			.and()
    			.formLogin()
				.and()
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//				.and()
				.csrf().disable()
				.headers().frameOptions().disable();  							              
//                .and()
//				.formLogin()
//				.loginPage("/login")
//				.failureUrl("/login?error=true")
//				.defaultSuccessUrl("/", true)
//				.and()
//				.logout()
//				.logoutSuccessUrl("/");
               return http.build();
    }
}
