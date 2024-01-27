package com.gardener.gardener.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
@Table(name = "work_progresses")
public class WorkProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Plant plant;

    @ManyToOne
    private WorkRule workRule;

    @Column(nullable = false)
    private boolean isDone;
}
