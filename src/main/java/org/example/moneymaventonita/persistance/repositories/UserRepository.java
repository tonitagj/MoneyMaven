package org.example.moneymaventonita.persistance.repositories;

import org.example.moneymaventonita.persistance.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    List<Users> findAllByEmail(String email);
}
