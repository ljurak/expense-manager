package com.expense.app.user.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true, length = 50)
	private String username;
	
	@Column(nullable = false, length = 100)
	private String password;
	
	@Column(nullable = false, length = 50)
	private String firstname;
	
	@Column(nullable = false, length = 50)
	private String lastname;
	
	@Column(length = 100)
	private String email;
	
	@Column(nullable = false)
	private Boolean enabled;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "users_roles", 
		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
	)
	private Set<RoleEntity> roles = new HashSet<>();

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getEmail() {
		return email;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Set<RoleEntity> getRoles() {
		return roles;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private UserEntity user = new UserEntity();
		
		private Builder() {
		}
		
		public Builder username(String username) {
			user.username = username;
			return this;
		}
		
		public Builder password(String password) {
			user.password = password;
			return this;
		}
		
		public Builder firstname(String firstname) {
			user.firstname = firstname;
			return this;
		}
		
		public Builder lastname(String lastname) {
			user.lastname = lastname;
			return this;
		}
		
		public Builder email(String email) {
			user.email = email;
			return this;
		}
		
		public Builder enabled(boolean enabled) {
			user.enabled = enabled;
			return this;
		}
		
		public Builder role(RoleEntity role) {
			user.roles.add(role);
			return this;
		}
		
		public UserEntity build() {
			return user;
		}
	}
}
