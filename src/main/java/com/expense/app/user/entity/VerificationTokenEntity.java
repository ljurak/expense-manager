package com.expense.app.user.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "verification_tokens")
public class VerificationTokenEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String token;
	
	@Column(nullable = false, name="created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	public Long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public UserEntity getUser() {
		return user;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private VerificationTokenEntity verificationToken = new VerificationTokenEntity();
		
		private Builder() {
		}
		
		public Builder user(UserEntity user) {
			verificationToken.user = user;
			return this;
		}
		
		public Builder token(String token) {
			verificationToken.token = token;
			return this;
		}
		
		public Builder createdAt(LocalDateTime createdAt) {
			verificationToken.createdAt = createdAt;
			return this;
		}
		
		public VerificationTokenEntity build() {
			return verificationToken;
		}
	}
}
