package com.example.bookstore.web;

import com.example.bookstore.domain.Author;
import com.example.bookstore.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Author create(@Valid @RequestBody Author author) {
        return authorService.create(author);
    }

    @GetMapping("/{id}")
    public Author getById(@PathVariable Long id) {
        return authorService.getById(id);
    }

    @GetMapping
    public List<Author> getAll() {
        return authorService.getAll();
    }

    @PutMapping("/{id}")
    public Author update(@PathVariable Long id, @Valid @RequestBody Author author) {
        return authorService.update(id, author);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}