package com.instagram.mysql.entites;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
//ooh no

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follow {
    @EmbeddedId
    private FollowId id;
    @MapsId("follower_id")
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;
    @MapsId("followee_id")
    @ManyToOne
    @JoinColumn(name = "followee_id")
    private User followee;
    @CreationTimestamp
    private LocalDateTime dateCreated;
}
