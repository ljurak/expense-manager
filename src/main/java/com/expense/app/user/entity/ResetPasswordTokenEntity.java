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
@Table(name = "reset_tokens")
public class ResetPasswordTokenEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true, length = 50)
	private String token;
	
	@OneToOne
	@JoinColumn(nullable = false, name = "user_id", unique = true)
	private UserEntity user;
	
	@Column(nullable = false, name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public UserEntity getUser() {
		return user;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private ResetPasswordTokenEntity resetToken = new ResetPasswordTokenEntity();
		
		private Builder() {
		}
		
		public Builder token(String token) {
			resetToken.token = token;
			return this;
		}
		
		public Builder createdAt(LocalDateTime createdAt) {
			resetToken.createdAt = createdAt;
			return this;
		}
		
		public Builder user(UserEntity user) {
			resetToken.user = user;
			return this;
		}
		
		public ResetPasswordTokenEntity build() {
			return resetToken;
		}
	}
}
