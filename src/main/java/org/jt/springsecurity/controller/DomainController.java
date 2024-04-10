package org.jt.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.jt.springsecurity.model.Customer;
import org.jt.springsecurity.repository.CustomerRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class DomainController {
	private final CustomerRepo customerRepo;
	private final PasswordEncoder encoder;
	private final AuthenticationManager authenticationManager;

	@PostMapping("/create")
	public Customer save( @RequestBody Customer customer ) {
		var bcrypt = encoder.encode(customer.getPassword());
		customer.setPassword(bcrypt);
		return customerRepo.save(customer);
	}

	@GetMapping
	public String log( Principal principal ) {
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
		return "Hello Gradle" + principal.getName();
	}

	@PostMapping("/login")
	public Customer login( @RequestBody Customer customer ) {
		var authentication = new UsernamePasswordAuthenticationToken
				(customer.getUsername(), customer.getPassword());
		authenticationManager.authenticate(authentication);
		return customer;
	}

}
