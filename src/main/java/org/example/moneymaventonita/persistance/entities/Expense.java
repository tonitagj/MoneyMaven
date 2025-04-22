package org.example.moneymaventonita.persistance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private double price;

    @Enumerated(EnumType.STRING)
    private ExpenseType type; // NECESSITY or IMPULSE

    @Enumerated(EnumType.STRING)
    private EmotionAfterPurchase eap;

    @Enumerated(EnumType.STRING)
    private EmotionAtRegistration ear;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;
}