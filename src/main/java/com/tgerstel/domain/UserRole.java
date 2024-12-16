package com.tgerstel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRole {

	private Long id;
	private String name;
	private String description;

}
