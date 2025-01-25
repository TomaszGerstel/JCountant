package com.tgerstel.domain.service.command;

import com.tgerstel.domain.User;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

import java.util.HashSet;

@Data
public class RegistrationCommand {

    private String username;
    private String email;
    private String password;
    private Integer lumpSumTaxRate;

    public User toUser(final PasswordEncoder passEncoder) {
        return new User(null, username, email, passEncoder.encode(password), lumpSumTaxRate, new HashSet<>());
    }

	public RegistrationCommand(final String username, final String email, final String password,
							   @Nullable final Integer lumpSumTaxRate) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.lumpSumTaxRate = lumpSumTaxRate;
	}    
}
