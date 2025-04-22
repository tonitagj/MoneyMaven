package org.example.moneymaventonita.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.moneymaventonita.JwtUtil;
import org.example.moneymaventonita.persistance.entities.FinancialGoal;
import org.example.moneymaventonita.persistance.entities.FinancialProfile;
import org.example.moneymaventonita.persistance.entities.Users;
import org.example.moneymaventonita.persistance.repositories.ExpenseRepository;
import org.example.moneymaventonita.persistance.repositories.FinancialGoalRepository;
import org.example.moneymaventonita.persistance.repositories.UserRepository;
import org.example.moneymaventonita.services.FinancialGoalService;
import org.example.moneymaventonita.services.dtos.FinancialGoalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.example.moneymaventonita.persistance.entities.Expense;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FinancialGoalServiceImpl implements FinancialGoalService {

    private final FinancialGoalRepository goalRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void setSavingGoal(String token, double amount) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email).orElseThrow();

        Optional<FinancialGoal> goalOpt = goalRepository.findByUser(user);

        FinancialGoal goal = goalOpt.orElse(new FinancialGoal());
        goal.setTargetAmount(amount);
        goal.setUser(user);
        goalRepository.save(goal);
    }

    @Override
    public FinancialGoalDTO getSavingGoalStatus(String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Optional<Users> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Users user = userOpt.get();
        LocalDate now = LocalDate.now();

        // Summe der variablen Ausgaben im aktuellen Monat
        double totalExpenses = expenseRepository.findByUser(user).stream()
                .filter(e -> e.getDate().getMonthValue() == now.getMonthValue() &&
                        e.getDate().getYear() == now.getYear())
                .mapToDouble(Expense::getPrice)
                .sum();

        FinancialProfile financialProfile = user.getFinancialProfile();
        double income = financialProfile != null ? financialProfile.getMonthlyIncome() : 0;

        // NEU: Fixkosten berücksichtigen
        double fixedCosts = 0;
        if (financialProfile != null) {
            fixedCosts = financialProfile.getRent() +
                    financialProfile.getInsurance() +
                    financialProfile.getTransport() +
                    financialProfile.getSubscriptions() +
                    financialProfile.getOthers();
        }

        double available = income - fixedCosts - totalExpenses;

        FinancialGoalDTO dto = new FinancialGoalDTO();
        Optional<FinancialGoal> goalOpt = goalRepository.findByUser(user);
        goalOpt.ifPresent(goal -> {
            dto.setTargetAmount(goal.getTargetAmount());
            dto.setGoalReached(available >= goal.getTargetAmount());
        });

        dto.setAvailableAmount(available);
        return dto;
    }
}
