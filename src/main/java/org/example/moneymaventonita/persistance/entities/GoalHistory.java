package org.example.moneymaventonita.persistance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String yearMonth; // e.g., "2025-04"

    private Double targetAmount;
    private Double actualSaved;

    private Boolean achieved;

    @ManyToOne
    private Users user;
}
