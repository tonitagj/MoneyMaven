package org.example.moneymaventonita.services.mapper;

import org.example.moneymaventonita.persistance.entities.Users;
import org.example.moneymaventonita.services.dtos.UsersDTO;

public class UsersMapper extends AbstractMapper<Users, UsersDTO> {
    @Override
    public UsersDTO mapToDto(Users source) {
        return UsersDTO.builder()
                .id(source.getId())
                .name(source.getName())
                .lastname(source.getLastname())
                .email(source.getEmail())
                .password(source.getPassword())
                .build();
    }

    @Override
    public Users toEntity(UsersDTO usersDto) {
        Users user = new Users();
        user.setId(usersDto.getId());
        user.setName(usersDto.getName());
        user.setLastname(usersDto.getLastname());
        user.setEmail(usersDto.getEmail());
        user.setPassword(usersDto.getPassword());
        return user;
    }

}
