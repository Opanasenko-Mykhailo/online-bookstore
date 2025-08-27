package com.example.bookstore.service;

import com.example.bookstore.domain.Author;

import java.util.List;

public interface AuthorService {
    Author create(Author author);

    Author getById(Long id);

    List<Author> getAll();

    Author update(Long id, Author author);

    void delete(Long id);
}