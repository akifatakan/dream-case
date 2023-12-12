package com.example.dreamcase.repository;

import com.example.dreamcase.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
