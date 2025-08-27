package com.example.bookstore.service.impl;

import com.example.bookstore.domain.Author;
import com.example.bookstore.domain.Book;
import com.example.bookstore.domain.Genre;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.GenreRepository;
import com.example.bookstore.service.BookService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final EntityManager entityManager;

    @Override
    public Book create(Book book) {
        validateAuthorAndGenre(book);
        return bookRepository.save(book);
    }

    @Override
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book update(Long id, Book book) {
        Book existing = getById(id);
        validateAuthorAndGenre(book);

        existing.setTitle(book.getTitle());
        existing.setPrice(book.getPrice());
        existing.setQuantity(book.getQuantity());
        existing.setAuthor(book.getAuthor());
        existing.setGenre(book.getGenre());

        return bookRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> search(String title, String author, String genre) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);

        Predicate predicate = cb.conjunction();

        if (title != null && !title.isBlank()) {
            predicate = cb.and(predicate,
                    cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }
        if (author != null && !author.isBlank()) {
            predicate = cb.and(predicate,
                    cb.like(cb.lower(root.join("author").get("name")), "%" + author.toLowerCase() + "%"));
        }
        if (genre != null && !genre.isBlank()) {
            predicate = cb.and(predicate,
                    cb.like(cb.lower(root.join("genre").get("name")), "%" + genre.toLowerCase() + "%"));
        }

        query.where(predicate);

        TypedQuery<Book> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    private void validateAuthorAndGenre(Book book) {
        if (book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new IllegalArgumentException("Book must have a valid author ID");
        }
        if (book.getGenre() == null || book.getGenre().getId() == null) {
            throw new IllegalArgumentException("Book must have a valid genre ID");
        }

        // Check if Author exists
        authorRepository.findById(book.getAuthor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + book.getAuthor().getId()));

        // Check if Genre exists
        genreRepository.findById(book.getGenre().getId())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + book.getGenre().getId()));
    }
}