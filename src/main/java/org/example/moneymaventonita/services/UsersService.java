package org.example.moneymaventonita.services;

import org.example.moneymaventonita.services.dtos.UsersDTO;
import org.springframework.stereotype.Component;

@Component
public interface UsersService {

    boolean saveNewUser(UsersDTO usersDTO);

    // UsersDTO getTourById(Long id);

}
