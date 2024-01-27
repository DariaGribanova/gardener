package com.gardener.gardener.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "work_rules")
public class WorkRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(nullable = false)
    private PlantCulture plantCulture;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Work work;

    @Column(nullable = false)
    private LocalDate dateStart;

    @Column(nullable = false)
    private LocalDate dateEnd;

    @OneToMany(cascade = CascadeType.ALL)
    private List<WorkProgress> workProgresses;
}
