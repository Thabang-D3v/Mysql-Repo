package com.instagram.mysql.entites;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {
    @EmbeddedId
    private LikeId id;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    @MapsId("user_id")
    private User user;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "photo_id",nullable = false)
    @MapsId("photo_id")
    private Photo photo;
    @CreationTimestamp
    private LocalDateTime dateCreated;
}
