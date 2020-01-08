package com.expense.app.user.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.expense.app.user.entity.RoleEntity;

@Repository
public interface RoleRepo extends CrudRepository<RoleEntity, Long> {
	Optional<RoleEntity> findByName(String name);
}
