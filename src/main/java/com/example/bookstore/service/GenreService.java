package com.example.bookstore.service;

import com.example.bookstore.domain.Genre;

import java.util.List;

public interface GenreService {
    Genre create(Genre genre);

    Genre getById(Long id);

    List<Genre> getAll();

    Genre update(Long id, Genre genre);

    void delete(Long id);
}