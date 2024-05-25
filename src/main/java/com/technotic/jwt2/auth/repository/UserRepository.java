package com.technotic.jwt2.auth.repository;

import com.technotic.jwt2.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface   UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String userName);
}
