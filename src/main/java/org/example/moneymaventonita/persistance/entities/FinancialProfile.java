package org.example.moneymaventonita.persistance.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "financial_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double monthlyIncome;

    private Double rent;

    private Double insurance;

    private Double transport;

    private Double subscriptions;

    private Double others;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;
}
