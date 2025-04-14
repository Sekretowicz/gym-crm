package com.sekretowicz.gym_crm.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "trainer")
@Data
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "specialization_id", nullable = false)
    private TrainingType specialization;

    @ManyToMany(mappedBy = "trainers")
    private Set<Trainee> trainees;
}