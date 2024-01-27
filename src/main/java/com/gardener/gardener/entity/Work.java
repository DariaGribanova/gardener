package com.gardener.gardener.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter @Setter
@Table(name = "works")
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int tempStart;

    @Column(nullable = false)
    private int tempEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Precipitation role;

    @Column(nullable = false)
    private int ageStart;

    @Column(nullable = false)
    private int ageEnd;

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL)
    private List<WorkRule> workRules;

}