package org.example.moneymaventonita.controllers;

import lombok.RequiredArgsConstructor;
import org.example.moneymaventonita.services.FinancialGoalService;
import org.example.moneymaventonita.services.dtos.FinancialGoalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/financial-goal")
@RequiredArgsConstructor
public class FinancialGoal {
    @Autowired
    @Qualifier("financialGoalServiceImpl")
    private final FinancialGoalService goalService;


    @PostMapping
    public ResponseEntity<?> setGoal(@RequestBody FinancialGoalDTO dto,
                                     @RequestHeader("Authorization") String token) {
        goalService.setSavingGoal(token, dto.getTargetAmount());
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<FinancialGoalDTO> getGoalStatus(@RequestHeader("Authorization") String token) {
        FinancialGoalDTO dto = goalService.getSavingGoalStatus(token);
        return ResponseEntity.ok(dto);
    }
}
