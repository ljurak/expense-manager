package com.expense.app.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.expense.app.user.entity.UserEntity;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, Long> {
	
	@Query("select u from UserEntity u join fetch u.roles")
	Optional<UserEntity> findByUsername(String username);
	
	boolean existsByUsername(String username);
}
