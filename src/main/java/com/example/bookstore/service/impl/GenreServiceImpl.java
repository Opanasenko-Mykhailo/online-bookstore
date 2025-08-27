package com.example.bookstore.service.impl;

import com.example.bookstore.domain.Genre;
import com.example.bookstore.repository.GenreRepository;
import com.example.bookstore.service.GenreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public Genre create(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public Genre getById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + id));
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre update(Long id, Genre genre) {
        Genre existing = getById(id);
        existing.setName(genre.getName());
        return genreRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new EntityNotFoundException("Genre not found with id: " + id);
        }
        genreRepository.deleteById(id);
    }
}