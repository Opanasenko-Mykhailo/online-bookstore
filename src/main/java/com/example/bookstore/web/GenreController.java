package com.example.bookstore.web;

import com.example.bookstore.domain.Genre;
import com.example.bookstore.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Genre create(@Valid @RequestBody Genre genre) {
        return genreService.create(genre);
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable Long id) {
        return genreService.getById(id);
    }

    @GetMapping
    public List<Genre> getAll() {
        return genreService.getAll();
    }

    @PutMapping("/{id}")
    public Genre update(@PathVariable Long id, @Valid @RequestBody Genre genre) {
        return genreService.update(id, genre);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        genreService.delete(id);
    }
}