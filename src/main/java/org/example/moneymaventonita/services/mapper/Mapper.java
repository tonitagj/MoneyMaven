package org.example.moneymaventonita.services.mapper;

public interface Mapper<S, T> {

    T mapToDto(S source);

}