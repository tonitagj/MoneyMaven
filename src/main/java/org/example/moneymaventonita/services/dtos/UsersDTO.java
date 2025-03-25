package org.example.moneymaventonita.services.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private Date birthday;
    private String country;
    private String nationality;
    private String phoneNumber;
    private String occupation;

    public UsersDTO(String name, String lastname, String email, Date birthday, String country, String nationality, String phoneNumber, String occupation) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.birthday = birthday;
        this.country = country;
        this.nationality = nationality;
        this.phoneNumber = phoneNumber;
        this.occupation = occupation;

    }
}

