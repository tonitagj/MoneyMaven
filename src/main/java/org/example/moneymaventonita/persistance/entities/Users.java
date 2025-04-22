package org.example.moneymaventonita.persistance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "USERS")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String lastname;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    @Temporal(TemporalType.DATE)
    private Date birthday;
    private String country;
    private String nationality;
    private String phoneNumber;
    private String occupation;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private FinancialProfile financialProfile;
    public FinancialProfile getFinancialProfile() {
        return financialProfile;
    }
    public void setFinancialProfile(FinancialProfile financialProfile) {
        this.financialProfile = financialProfile;
    }
}