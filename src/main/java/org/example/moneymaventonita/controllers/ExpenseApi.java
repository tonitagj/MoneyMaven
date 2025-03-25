package org.example.moneymaventonita.controllers;

import lombok.RequiredArgsConstructor;
import org.example.moneymaventonita.services.ExpenseService;
import org.example.moneymaventonita.services.dtos.ExpenseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseApi {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<?> saveExpense(@RequestHeader("Authorization") String token,
                                         @RequestBody ExpenseDTO dto) {
        expenseService.saveExpense(token, dto);
        return ResponseEntity.ok("Expense saved successfully.");
    }

    @GetMapping("/{date}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByDate(@RequestHeader("Authorization") String token,
                                                              @PathVariable String date) {
        return ResponseEntity.ok(expenseService.getExpensesForDate(token, date));
    }

    @GetMapping("/today")
    public ResponseEntity<List<ExpenseDTO>> getExpensesForToday(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(expenseService.getExpensesForToday(token));
    }
}
