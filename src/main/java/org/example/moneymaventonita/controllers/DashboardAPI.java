package org.example.moneymaventonita.controllers;

import lombok.RequiredArgsConstructor;
import org.example.moneymaventonita.JwtUtil;
import org.example.moneymaventonita.persistance.entities.Expense;
import org.example.moneymaventonita.persistance.entities.ExpenseType;
import org.example.moneymaventonita.persistance.entities.Users;
import org.example.moneymaventonita.persistance.repositories.ExpenseRepository;
import org.example.moneymaventonita.persistance.repositories.UserRepository;
import org.example.moneymaventonita.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.moneymaventonita.persistance.entities.ExpenseType.IMPULSE;
import static org.example.moneymaventonita.persistance.entities.ExpenseType.NECESSITY;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardAPI {
    private final ExpenseService expenseService;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final JwtUtil jwtUtil;

    @GetMapping("/impulse-vs-necessity")
    public ResponseEntity<Map<String, Double>> getImpulseVsNecessity(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email).orElseThrow();

        List<Expense> expenses = expenseRepository.findByUser(user);

        double impulse = 0, necessity = 0;

        System.out.println("Found expenses: " + expenses.size());

        for (Expense e : expenses) {
            ExpenseType type = e.getType();

            if (type == null) {
                System.out.println("⚠️ Null type found for expense with price: " + e.getPrice());
                continue;
            }


            switch (type) {
                case IMPULSE -> impulse += e.getPrice();
                case NECESSITY -> necessity += e.getPrice();
            }
        }

        return ResponseEntity.ok(Map.of(
                "IMPULSE", impulse,
                "NECESSITY", necessity
        ));
    }

    @GetMapping("/monthly-expenses")
    public ResponseEntity<Map<String, Double>> getMonthlyExpenses(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email).orElseThrow();

        List<Expense> expenses = expenseRepository.findByUser(user);

        Map<String, Double> monthlyTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDate().getMonth().toString(),
                        Collectors.summingDouble(Expense::getPrice)
                ));

        return ResponseEntity.ok(monthlyTotals);
    }

    @GetMapping("/daily-expenses")
    public ResponseEntity<Map<String, Double>> getDailyExpenses(
            @RequestHeader("Authorization") String token,
            @RequestParam int month,
            @RequestParam int year) {
        System.out.println("🔐 Token received: " + token);
        return ResponseEntity.ok(expenseService.getDailyExpensesForMonth(token, month, year));
    }

    @GetMapping("/dashboard/weekly-expenses")
    public ResponseEntity<Map<Integer, Double>> getWeeklyExpenses(@RequestHeader("Authorization") String token, @RequestParam int month, @RequestParam int year) {

        return ResponseEntity.ok(expenseService.getWeeklyExpensesForMonth(token, month, year));

    }

    @GetMapping("/weekly-impulse-vs-necessity")
    public ResponseEntity<?> getWeeklyImpulseVsNecessity(
            @RequestHeader("Authorization") String token,
            @RequestParam int month,
            @RequestParam int year) {
        Map<String, Map<String, Double>> data = expenseService.getWeeklyImpulseVsNecessity(token, month, year);
        return ResponseEntity.ok(data);
    }
}
