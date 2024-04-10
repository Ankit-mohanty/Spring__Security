package org.jt.springsecurity.config;

import lombok.RequiredArgsConstructor;
import org.jt.springsecurity.repository.CustomerRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomerRepo customerRepo;

	@Bean
	UserDetailsService userDetailsService() {

		/*var user1 = User
				.builder()
				.username("Ram")
				.password("$2y$10$bYcNm4L9PjfuWNJggsU18.BPzwOi4g1MH/6TS1W4jx5v94oZsCaMe")
				.roles("student")
				.build();
		var user2 = User
				.builder()
				.username("Rup")
				.password("{noop}dad")
				.roles("student")
				.build();*/
//		return username -> {
//			var customer = customerRepo.findByUserName(username)
//					.orElseThrow(() -> new UsernameNotFoundException(""));
		return username -> customerRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(""));

			/*if( username.equals("Ram") )
				return user1;
			if( username.equals("Rup") )
				return user2;*/
/*return User.builder()
		.username(customer.getUserName())
		.password(customer.getUserPassword())
		.build();*/
//			throw new UsernameNotFoundException("User not Found");
//		};
	}

	@Bean
	SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(request -> request
				.requestMatchers("/create", "/login")
				.permitAll()
				.anyRequest().authenticated());
		http.httpBasic(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

	@Bean
	AuthenticationManager authenticationManager() {
		var dao = new DaoAuthenticationProvider();
		dao.setPasswordEncoder(passwordEncoder());
		dao.setUserDetailsService(userDetailsService());
		return new ProviderManager(dao);
	}
}
