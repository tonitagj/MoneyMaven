package org.example.moneymaventonita.controllers;

import lombok.RequiredArgsConstructor;
import org.example.moneymaventonita.services.FinancialProfileService;
import org.example.moneymaventonita.services.dtos.FinancialProfileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/financial-profile")
@RequiredArgsConstructor
public class FinancialProfileApi {
    private final FinancialProfileService financialProfileService;

    @PutMapping
    public ResponseEntity<?> saveOrUpdate(@RequestHeader("Authorization") String token,
                                          @RequestBody FinancialProfileDTO dto) {
        financialProfileService.saveOrUpdateProfile(token, dto);
        return ResponseEntity.ok("Financial profile saved or updated.");
    }

    @GetMapping
    public ResponseEntity<FinancialProfileDTO> getProfile(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(financialProfileService.getProfile(token));
    }
}
