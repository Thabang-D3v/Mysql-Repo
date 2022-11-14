package com.instagram.mysql.repo;

import com.instagram.mysql.entites.Follow;
import com.instagram.mysql.entites.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
}