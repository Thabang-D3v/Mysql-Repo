package com.instagram.mysql.entites;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(unique = true,nullable = false)
    private String userName;
    @CreationTimestamp
    private LocalDateTime dateCreated;
    @OneToMany(mappedBy = "user",orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Photo> photos;
    @OneToMany(mappedBy = "author",orphanRemoval = true)
    @ToString.Exclude
    private List<Comment>comments;
    @OneToMany(mappedBy = "photo",orphanRemoval = true)
    @ToString.Exclude
    private List<Likes>likes;
    @OneToMany(mappedBy = "follower")
    @ToString.Exclude
    private List<Follow>follows;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
