package org.example.moneymaventonita.persistance.repositories;

import org.example.moneymaventonita.persistance.entities.GoalHistory;
import org.example.moneymaventonita.persistance.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalHistoryRepository extends JpaRepository<GoalHistory, Long> {
    List<GoalHistory> findAllByUserOrderByYearMonthDesc(Users user);
}
