package com.instagram.mysql.entites;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(unique = true,nullable = false)
    private String name;
    @ManyToMany(mappedBy = "tags",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Photo> photos;
}
