package com.instagram.mysql.repo;

import com.instagram.mysql.entites.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<Tag, Integer> {
}