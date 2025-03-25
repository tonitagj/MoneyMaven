package org.example.moneymaventonita.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.moneymaventonita.persistance.entities.ExpenseType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDTO {
    private String itemName;
    private double price;
    private ExpenseType type;
    private LocalDate date;
}
