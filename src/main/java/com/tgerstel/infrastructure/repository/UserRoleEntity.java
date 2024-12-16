package com.tgerstel.infrastructure.repository;

import com.tgerstel.domain.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_role")
@NoArgsConstructor
public class UserRoleEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;

	public UserRoleEntity(UserRole useRole) {
		this.id = useRole.getId();
		this.name = useRole.getName();
		this.description = useRole.getDescription();
	}

	public UserRole toUserRole() {
		return new UserRole(id, name, description);
	}

}
