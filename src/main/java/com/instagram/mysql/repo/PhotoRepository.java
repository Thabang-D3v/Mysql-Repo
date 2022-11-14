package com.instagram.mysql.repo;

import com.instagram.mysql.entites.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
}