package org.example.moneymaventonita.persistance.repositories;


import org.example.moneymaventonita.persistance.entities.FinancialGoal;
import org.example.moneymaventonita.persistance.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {
    Optional<FinancialGoal> findByUser(Users user);
}
