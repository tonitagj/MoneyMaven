package org.example.moneymaventonita.persistance.repositories;

import org.example.moneymaventonita.persistance.entities.Expense;
import org.example.moneymaventonita.persistance.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserAndDate(Users user, LocalDate date);
    boolean existsByUserAndDate(Users user, LocalDate date);
    List<Expense> findByUser(Users user);
    @Query("SELECT e.date, SUM(e.price) FROM Expense e WHERE e.user = :user AND MONTH(e.date) = :month AND YEAR(e.date) = :year GROUP BY e.date ORDER BY e.date")
    List<Object[]> findDailySumsByMonthAndYear(@Param("user") Users user, @Param("month") int month, @Param("year") int year);
}
