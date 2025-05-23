// com/Ecommerce/tubes_PBO/repository/UserRepository.java
package com.Ecommerce.tubes_PBO.repository;

import com.Ecommerce.tubes_PBO.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}