package com.indrajeet.spring.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.indrajeet.spring.model.User;
import com.indrajeet.spring.payload.JwtLoginSuccessResponse;
import com.indrajeet.spring.payload.LoginRequest;
import com.indrajeet.spring.security.JwtTokenProvider;
import com.indrajeet.spring.service.UserService;
import com.indrajeet.spring.service.ValidationHandlerService;
import com.indrajeet.spring.validator.UserValidator;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
	
	
	@Autowired 
	private ValidationHandlerService validationHandlerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator userValidator;
	
    @Autowired
	private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;
   
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        ResponseEntity<?> errorMap = validationHandlerService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = "Bearer " +  tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtLoginSuccessResponse(true, jwt));
    }
    
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user , BindingResult result) {
      //	validate password match
		userValidator.validate(user, result);
		
		ResponseEntity<?> errorMap = validationHandlerService.MapValidationService(result);
		if(errorMap != null)
			return errorMap;
		
		User newUser = userService.saveUser(user);
	
		return new ResponseEntity<User>(newUser , HttpStatus.CREATED);	
	}

}