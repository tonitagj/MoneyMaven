package org.example.moneymaventonita.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.moneymaventonita.JwtUtil;
import org.example.moneymaventonita.persistance.entities.FinancialProfile;
import org.example.moneymaventonita.persistance.entities.Users;
import org.example.moneymaventonita.persistance.repositories.FinancialProfileRepository;
import org.example.moneymaventonita.persistance.repositories.UserRepository;
import org.example.moneymaventonita.services.FinancialProfileService;
import org.example.moneymaventonita.services.dtos.FinancialProfileDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinancialProfileServiceImpl implements FinancialProfileService {

    private final FinancialProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void saveOrUpdateProfile(String token, FinancialProfileDTO dto) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialProfile profile = profileRepository.findByUser(user)
                .orElse(FinancialProfile.builder().user(user).build());

        profile.setMonthlyIncome(dto.getMonthlyIncome());
        profile.setRent(dto.getRent());
        profile.setInsurance(dto.getInsurance());
        profile.setTransport(dto.getTransport());
        profile.setSubscriptions(dto.getSubscriptions());
        profile.setOthers(dto.getOthers());

        profileRepository.save(profile);
    }

    @Override
    public FinancialProfileDTO getProfile(String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialProfile profile = profileRepository.findByUser(user)
                .orElseGet(() -> {
                    FinancialProfile newProfile = FinancialProfile.builder()
                            .user(user)
                            .monthlyIncome(0.0)
                            .rent(0.0)
                            .insurance(0.0)
                            .transport(0.0)
                            .subscriptions(0.0)
                            .others(0.0)
                            .build();
                    return profileRepository.save(newProfile);
                });

        return FinancialProfileDTO.builder()
                .monthlyIncome(profile.getMonthlyIncome())
                .rent(profile.getRent())
                .insurance(profile.getInsurance())
                .transport(profile.getTransport())
                .subscriptions(profile.getSubscriptions())
                .others(profile.getOthers())
                .build();
    }
}
