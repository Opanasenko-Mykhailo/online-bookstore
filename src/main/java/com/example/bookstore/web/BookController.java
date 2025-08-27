package com.example.bookstore.web;

import com.example.bookstore.domain.Book;
import com.example.bookstore.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@Valid @RequestBody Book book) {
        return bookService.create(book);
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @GetMapping
    public List<Book> getAll() {
        return bookService.getAll();
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @Valid @RequestBody Book book) {
        return bookService.update(id, book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    @GetMapping("/search")
    public List<Book> search(@RequestParam(required = false) String title,
                             @RequestParam(required = false) String author,
                             @RequestParam(required = false) String genre) {
        return bookService.search(title, author, genre);
    }
}