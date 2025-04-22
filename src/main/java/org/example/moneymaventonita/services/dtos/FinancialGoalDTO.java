package org.example.moneymaventonita.services.dtos;


import lombok.*;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FinancialGoalDTO {
    private double targetAmount;
    private boolean goalReached;
    private double availableAmount;

}
