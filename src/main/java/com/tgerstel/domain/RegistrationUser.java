package com.tgerstel.domain;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RegistrationUser {

    @NotBlank(message = "Enter the name")
    private String username;
    @Email(message = "Enter a valid email")
    @NotBlank(message = "Enter your email")
    private String email;
    @NotBlank(message = "Enter password")
    private String password;
    @Positive
    private Integer lumpSumTaxRate; 

    public User toUser(PasswordEncoder passEncoder) {
        return new User(null, username, email, passEncoder.encode(password), lumpSumTaxRate, new HashSet<>());
    }

	public RegistrationUser(String username, String email, String password, 
			@Nullable Integer lumpSumTaxRate) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.lumpSumTaxRate = lumpSumTaxRate;
	}    
}
