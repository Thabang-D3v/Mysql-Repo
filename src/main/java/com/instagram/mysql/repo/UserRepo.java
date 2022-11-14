package com.instagram.mysql.repo;

import com.instagram.mysql.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
}