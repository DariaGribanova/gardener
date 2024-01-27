package com.gardener.gardener.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@Table(name = "regions")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullAddress;

    @Column(nullable = false)
    private String shortAddress;

    @ManyToOne
    @JoinColumn(name = "parent_region_id")
    private Region parentRegion;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    private List<WorkRule> workRules;
}
