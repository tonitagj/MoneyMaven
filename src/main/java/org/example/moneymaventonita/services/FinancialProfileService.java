package org.example.moneymaventonita.services;


import org.example.moneymaventonita.services.dtos.FinancialProfileDTO;
import org.springframework.stereotype.Component;

@Component
public interface FinancialProfileService {
    void saveOrUpdateProfile(String token, FinancialProfileDTO dto);
    FinancialProfileDTO getProfile(String token);
}

