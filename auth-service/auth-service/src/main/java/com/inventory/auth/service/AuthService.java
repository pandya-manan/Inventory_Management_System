package com.inventory.auth.service;

import com.inventory.auth.dto.LoginRequest;
import com.inventory.auth.dto.SignupRequest;

public interface AuthService {
	
	String signup(SignupRequest signupRequest);
	
	String login(LoginRequest loginRequest);

}
