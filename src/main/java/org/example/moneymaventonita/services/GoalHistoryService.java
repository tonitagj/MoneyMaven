package org.example.moneymaventonita.services;

import org.example.moneymaventonita.services.dtos.GoalHistoryDTO;

import java.util.List;

public interface GoalHistoryService {
    List<GoalHistoryDTO> getGoalHistory(String token);
}
