package com.Ecommerce.tubes_PBO.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.Ecommerce.tubes_PBO.model.User;
import java.util.List;


@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>findByUsername(String username);
    boolean existsByUsername(String username);
}
