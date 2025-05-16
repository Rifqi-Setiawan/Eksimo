package com.Ecommerce.tubes_PBO.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.Ecommerce.tubes_PBO.model.User;

@RepositoryRestResource
public interface UserRepo extends JpaRepository<User, Long> {

}
