package org.example.moneymaventonita.services;

import org.example.moneymaventonita.services.dtos.ExpenseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface ExpenseService {
    void saveExpense(String token, ExpenseDTO dto);
    List<ExpenseDTO> getExpensesForDate(String token, String dateStr);
    List<ExpenseDTO> getExpensesForToday(String token);
}
