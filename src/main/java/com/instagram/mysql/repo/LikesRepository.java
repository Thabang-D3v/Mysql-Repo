package com.instagram.mysql.repo;

import com.instagram.mysql.entites.LikeId;
import com.instagram.mysql.entites.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, LikeId> {
}