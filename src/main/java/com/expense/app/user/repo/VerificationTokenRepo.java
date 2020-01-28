package com.expense.app.user.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.expense.app.user.entity.VerificationTokenEntity;

@Repository
public interface VerificationTokenRepo extends CrudRepository<VerificationTokenEntity, Long> {
	Optional<VerificationTokenEntity> findByToken(String token);
}
