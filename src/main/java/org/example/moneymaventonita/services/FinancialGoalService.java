package org.example.moneymaventonita.services;


import org.example.moneymaventonita.services.dtos.FinancialGoalDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public interface FinancialGoalService {
    void setSavingGoal(String token, double amount);
    FinancialGoalDTO getSavingGoalStatus(String token);
}
