package com.instagram.mysql.entites;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(nullable = false,unique = true)
    private String imageUrl;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name= "user_id",nullable = false)
    private User user;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "photo",orphanRemoval = true)
    @ToString.Exclude
    private List<Comment>comments;
    @OneToMany(mappedBy = "photo",orphanRemoval = true)
    @ToString.Exclude
    private List<Like>likes;
    private String caption;
    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Tag>tags;
    @CreationTimestamp
    private LocalDateTime dateCreated;
}
