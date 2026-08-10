package com.inventoryx.service;

import com.inventoryx.dto.LoginResponse;
import com.inventoryx.dto.RegisterRequest;
import com.inventoryx.security.JwtService;
import com.inventoryx.entity.User;
import com.inventoryx.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	public LoginResponse login(String email, String password) {

	    authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                    email,
	                    password
	            )
	    );

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new RuntimeException("User not found"));

	    String token =
	            jwtService.generateToken(
	                    user.getEmail(),
	                    user.getRole()
	            );

	    return new LoginResponse(
	            "Login successful",
	            user.getEmail(),
	            user.getRole(),
	            token
	    );
	}

	public User register(RegisterRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already registered");
		}

		User user = new User();

		user.setName(request.getName());
		user.setEmail(request.getEmail());

		String hashedPassword = passwordEncoder.encode(request.getPassword());

		user.setPassword(hashedPassword);

		user.setRole("USER");

		return userRepository.save(user);
	}
}