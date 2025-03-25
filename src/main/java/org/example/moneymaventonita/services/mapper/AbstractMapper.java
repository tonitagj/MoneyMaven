package org.example.moneymaventonita.services.mapper;


import org.example.moneymaventonita.persistance.entities.Users;
import org.example.moneymaventonita.services.dtos.UsersDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractMapper <S, T> implements Mapper<S, T>{
    public List<T> mapToDto(Collection<S> source) {
        List<T> targets = new ArrayList<>();
        source.forEach(s -> {
            targets.add(mapToDto(s));
        });
        return targets;
    }

    public abstract Users toEntity(UsersDTO usersDto);
}
