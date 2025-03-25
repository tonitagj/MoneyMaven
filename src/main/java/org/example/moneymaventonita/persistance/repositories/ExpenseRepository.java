package org.example.moneymaventonita.persistance.repositories;

import org.example.moneymaventonita.persistance.entities.Expense;
import org.example.moneymaventonita.persistance.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserAndDate(Users user, LocalDate date);
}
