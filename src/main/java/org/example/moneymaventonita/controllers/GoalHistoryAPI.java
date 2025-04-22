package org.example.moneymaventonita.controllers;

import lombok.RequiredArgsConstructor;
import org.example.moneymaventonita.services.GoalHistoryService;
import org.example.moneymaventonita.services.dtos.GoalHistoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goal-history")
@RequiredArgsConstructor
public class GoalHistoryAPI {
    private final GoalHistoryService goalHistoryService;

    @GetMapping
    public ResponseEntity<List<GoalHistoryDTO>> getHistory(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(goalHistoryService.getGoalHistory(token));
    }
}
