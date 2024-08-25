package com.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanagement.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
