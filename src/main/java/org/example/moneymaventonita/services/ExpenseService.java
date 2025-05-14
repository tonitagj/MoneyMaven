package org.example.moneymaventonita.services;

import org.example.moneymaventonita.services.dtos.ExpenseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ExpenseService {
    void saveExpense(String token, ExpenseDTO dto);
    List<ExpenseDTO> getExpensesForDate(String token, String dateStr);
    List<ExpenseDTO> getExpensesForToday(String token);
    Map<String, Double> getImpulseVsNecessity(String token);
    Map<String, Double> getTotalExpensesPerMonth(String token);
    Map<String, Double> getDailyExpensesForMonth(String token, int month, int year);
    Map<Integer, Double> getWeeklyExpensesForMonth(String token, int month, int year);



}
