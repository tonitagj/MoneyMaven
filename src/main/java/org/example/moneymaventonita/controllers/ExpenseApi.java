package org.example.moneymaventonita.controllers;

import lombok.RequiredArgsConstructor;
import org.example.moneymaventonita.services.ExpenseService;
import org.example.moneymaventonita.services.UsersService;
import org.example.moneymaventonita.services.dtos.ExpenseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseApi {

    private final ExpenseService expenseService;
    @Autowired
    private final UsersService usersService;


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
