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
    private Long tempStart;

    @Column(nullable = false)
    private Long tempEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Precipitation precipitation;

    @Column(nullable = false)
    private Long ageStart;

    @Column(nullable = false)
    private Long ageEnd;

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL)
    private List<WorkRule> workRules;

}