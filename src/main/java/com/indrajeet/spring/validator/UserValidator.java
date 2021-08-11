package com.indrajeet.spring.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.indrajeet.spring.model.User;

@Component
public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> aclass) {
		
		return User.class.equals(aclass);
	}

	@Override
	public void validate(Object object, Errors errors) {
		
		User user = (User) object;
		
		if(user.getPassword().length() < 6) {
			   errors.rejectValue("password","Length", "Password must be at least 6 characters");
		}
		

        if(!user.getPassword().equals(user.getConfirmPassword())){
            errors.rejectValue("confirmPassword","Match", "Passwords must match");

        }

		
	}
	
	

}
