package org.example.moneymaventonita.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.moneymaventonita.JwtUtil;
import org.example.moneymaventonita.persistance.entities.Users;
import org.example.moneymaventonita.persistance.repositories.GoalHistoryRepository;
import org.example.moneymaventonita.persistance.repositories.UserRepository;
import org.example.moneymaventonita.services.GoalHistoryService;
import org.example.moneymaventonita.services.dtos.GoalHistoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalHistoryServiceImpl implements GoalHistoryService {
    private final GoalHistoryRepository historyRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public List<GoalHistoryDTO> getGoalHistory(String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email).orElseThrow();

        return historyRepository.findAllByUserOrderByYearMonthDesc(user).stream()
                .map(h -> GoalHistoryDTO.builder()
                        .yearMonth(h.getYearMonth())
                        .targetAmount(h.getTargetAmount())
                        .actualSaved(h.getActualSaved())
                        .achieved(h.getAchieved())
                        .build())
                .toList();
    }
}
