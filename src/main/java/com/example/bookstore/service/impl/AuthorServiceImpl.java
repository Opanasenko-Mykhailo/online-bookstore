package com.example.bookstore.service.impl;

import com.example.bookstore.domain.Author;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Author create(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author getById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author update(Long id, Author author) {
        Author existing = getById(id);
        existing.setName(author.getName());
        return authorRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new EntityNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }
}