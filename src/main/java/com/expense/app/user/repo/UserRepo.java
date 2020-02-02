package com.expense.app.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.expense.app.user.entity.UserEntity;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, Long> {
	
	@Query("select u from UserEntity u join fetch u.roles where u.username = :username")
	Optional<UserEntity> findByUsername(@Param("username") String username);
	
	Optional<UserEntity> findByEmail(String email);
	
	boolean existsByUsernameOrEmail(String username, String email);
}
