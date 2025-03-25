package org.example.moneymaventonita.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moneymaventonita.JwtUtil;
import org.example.moneymaventonita.persistance.entities.Expense;
import org.example.moneymaventonita.persistance.entities.Users;
import org.example.moneymaventonita.persistance.repositories.ExpenseRepository;
import org.example.moneymaventonita.persistance.repositories.UserRepository;
import org.example.moneymaventonita.services.ExpenseService;
import org.example.moneymaventonita.services.dtos.ExpenseDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
                        .date(exp.getDate())
                        .build())
                .collect(Collectors.toList());
    }
}