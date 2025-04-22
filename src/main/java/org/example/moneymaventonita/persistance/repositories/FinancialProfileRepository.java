package org.example.moneymaventonita.persistance.repositories;

import org.example.moneymaventonita.persistance.entities.FinancialProfile;
import org.example.moneymaventonita.persistance.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinancialProfileRepository extends JpaRepository<FinancialProfile, Long> {
    Optional<FinancialProfile> findByUser(Users user);
}
