package com.instagram.mysql.entites;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class FollowId implements Serializable {
    @Column(name = "follower_id")
    private int followerId;
    @Column(name = "followee_id")
    private int followeeId;
}
