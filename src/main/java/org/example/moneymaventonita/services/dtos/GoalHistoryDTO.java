package org.example.moneymaventonita.services.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoalHistoryDTO {
    private String yearMonth;
    private Double targetAmount;
    private Double actualSaved;
    private Boolean achieved;
}
