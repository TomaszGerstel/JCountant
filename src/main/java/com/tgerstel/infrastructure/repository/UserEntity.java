package com.tgerstel.infrastructure.repository;

import com.tgerstel.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity()
@Table(name="user_base")
@NoArgsConstructor
public class UserEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;   
    private String password;
    private String email;
    private Integer lumpSumTaxRate;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
    	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<UserRole> roles = new HashSet<>();

    public UserEntity(String username, String email, String password, Integer lumpSumTaxRate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.lumpSumTaxRate = lumpSumTaxRate;
    }

    public UserEntity(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.lumpSumTaxRate = user.getLumpSumTaxRate();
        this.roles = user.getRoles();
    }

    public User toUser() {
        return new User(id, username, email, password, lumpSumTaxRate, roles);
    }

}

