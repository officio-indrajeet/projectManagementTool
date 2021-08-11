package com.indrajeet.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.indrajeet.spring.exceptions.UsernameAlreadyExistsException;
import com.indrajeet.spring.model.User;
import com.indrajeet.spring.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		
		try {
			
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
		    // User name has to be unique (exception)
		    newUser.setUsername(newUser.getUsername());
		    
			// password and confirm password match - it is done in userValidator class
		    
	       // we don't persist or show the confirm password
		    
		    newUser.setConfirmPassword("");

			return userRepository.save(newUser);
			
		}catch(Exception e) {
			
			throw new UsernameAlreadyExistsException("Username "+ newUser.getUsername() + " already Exists");
		}
			
	}

}
