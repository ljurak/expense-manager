package com.expense.app.user.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.expense.app.user.entity.ResetPasswordTokenEntity;
import com.expense.app.user.entity.UserEntity;

@Repository
public interface ResetPasswordTokenRepo extends CrudRepository<ResetPasswordTokenEntity, Long> {
	Optional<ResetPasswordTokenEntity> findByToken(String token);
	boolean existsByUser(UserEntity user);
}
