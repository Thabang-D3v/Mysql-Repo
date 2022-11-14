package com.instagram.mysql.entites;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class LikeId implements Serializable {
    @Column(name = "user_id")
    private int userId;
    @Column(name = "photo_id")
    private int photoId;
}
