package org.example.moneymaventonita.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moneymaventonita.JwtUtil;
import org.example.moneymaventonita.persistance.entities.*;
import org.example.moneymaventonita.persistance.repositories.ExpenseRepository;
import org.example.moneymaventonita.persistance.repositories.UserRepository;
import org.example.moneymaventonita.services.ExpenseService;
import org.example.moneymaventonita.services.dtos.ExpenseDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void saveExpense(String token, ExpenseDTO dto) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = Expense.builder()
                .itemName(dto.getItemName())
                .price(dto.getPrice())
                .type(dto.getType())
                .eap(dto.getEap())
                .ear(dto.getEar())
                .date(dto.getDate())
                .user(user)
                .build();

        expenseRepository.save(expense);
    }

    @Override
    public List<ExpenseDTO> getExpensesForDate(String token, String dateStr) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate date = LocalDate.parse(dateStr);

        return expenseRepository.findByUserAndDate(user, date).stream()
                .map(exp -> ExpenseDTO.builder()
                        .itemName(exp.getItemName())
                        .price(exp.getPrice())
                        .type(exp.getType())
                        .eap(exp.getEap())
                        .ear(exp.getEar())
                        .date(exp.getDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDTO> getExpensesForToday(String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();

        return expenseRepository.findByUserAndDate(user, today).stream()
                .map(exp -> ExpenseDTO.builder()
                        .itemName(exp.getItemName())
                        .price(exp.getPrice())
                        .type(exp.getType())
                        .eap(exp.getEap())
                        .ear(exp.getEar())
                        .date(exp.getDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 23 * * ?") // runs every day at 11 PM
    public void addZeroExpensesForInactiveUsers() {
        LocalDate today = LocalDate.now();
        List<Users> allUsers = userRepository.findAll();

        for (Users user : allUsers) {
            boolean hasEntry = expenseRepository.existsByUserAndDate(user, today);
            if (!hasEntry) {
                Expense zeroExpense = Expense.builder()
                        .itemName("No spending")
                        .price(0.0)
                        .type(ExpenseType.NECESSITY) // or create a new type like NONE
                        .eap(EmotionAfterPurchase.HAPPY)
                        .ear(EmotionAtRegistration.PROUD)
                        .date(today)
                        .user(user)
                        .build();
                expenseRepository.save(zeroExpense);
            }
        }
    }

    @Override
    public Map<String, Double> getImpulseVsNecessity(String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email).orElseThrow();

        List<Expense> expenses = expenseRepository.findByUser(user);

        double impulse = 0;
        double necessity = 0;

        for (Expense e : expenses) {
            if (e.getType() == ExpenseType.IMPULSE) impulse += e.getPrice();
            else if (e.getType() == ExpenseType.NECESSITY) necessity += e.getPrice();
        }

        return Map.of("IMPULSE", impulse, "NECESSITY", necessity);
    }

    @Override
    public Map<String, Double> getTotalExpensesPerMonth(String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email).orElseThrow();

        List<Expense> expenses = expenseRepository.findByUser(user);

        return expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDate().getMonth().toString(),
                        Collectors.summingDouble(Expense::getPrice)
                ));
    }

    @Override
    public Map<String, Double> getDailyExpensesForMonth(String token, int month, int year) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email).orElseThrow();

        List<Expense> expenses = expenseRepository.findByUser(user);

        return expenses.stream()
                .filter(e -> e.getDate() != null
                        && e.getDate().getMonthValue() == month
                        && e.getDate().getYear() == year)
                .collect(Collectors.groupingBy(
                        e -> e.getDate().toString(), // Format: 2025-04-14
                        Collectors.summingDouble(Expense::getPrice)
                ));
    }

    @Override
    public Map<Integer, Double> getWeeklyExpensesForMonth(String token, int month, int year) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email).orElseThrow();

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Expense> expenses = expenseRepository.findByUserAndDateBetween(user, startDate, endDate);

        Map<Integer, Double> weeklyTotals = new TreeMap<>();
        for (Expense expense : expenses) {
            int weekOfMonth = expense.getDate()
                    .get(WeekFields.of(Locale.getDefault()).weekOfMonth());
            weeklyTotals.merge(weekOfMonth, expense.getPrice(), Double::sum);
        }

        return weeklyTotals;
    }
}