package com.gardener.gardener.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Garden garden;

    @ManyToOne
    private PlantCulture plantCulture;

    @Column(nullable = false)
    private LocalDate year;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL)
    private List<WorkProgress> workProgresses;

}
