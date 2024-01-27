package com.gardener.gardener.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@Table(name = "gardens")
public class Garden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private User user;

    @ManyToOne
    private Region region;

    @OneToMany(mappedBy = "garden", cascade = CascadeType.ALL)
    private List<Plant> plants;
}
