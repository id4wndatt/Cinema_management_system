package com.example.cinema_back_end.services;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<T> {
    List<T> findAll();

    T getById(Integer id);

    void update(T t);

    void remove(Integer id);
}
