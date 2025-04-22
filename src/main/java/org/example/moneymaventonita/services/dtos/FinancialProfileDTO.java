package org.example.moneymaventonita.services.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialProfileDTO {
    private Double monthlyIncome;
    private Double rent;
    private Double insurance;
    private Double transport;
    private Double subscriptions;
    private Double others;
}

