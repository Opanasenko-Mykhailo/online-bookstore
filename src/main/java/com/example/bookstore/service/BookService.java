package com.example.bookstore.service;

import com.example.bookstore.domain.Book;

import java.util.List;

public interface BookService {
    Book create(Book book);

    Book getById(Long id);

    List<Book> getAll();

    Book update(Long id, Book book);

    void delete(Long id);

    List<Book> search(String title, String author, String genre);
}