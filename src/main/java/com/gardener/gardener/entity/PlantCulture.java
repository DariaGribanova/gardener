package com.gardener.gardener.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@Table(name = "plant_cultures")
public class PlantCulture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cultureName;

    @ManyToOne
    @JoinColumn(name = "parent_culture_id")
    private PlantCulture parentCulture;

    @Column(nullable = false)
    private boolean displayRoot;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plantCulture")
    private List<WorkRule> workRules;

}
