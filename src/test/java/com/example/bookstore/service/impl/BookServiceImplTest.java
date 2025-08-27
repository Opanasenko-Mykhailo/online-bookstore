package com.example.bookstore.service.impl;

import com.example.bookstore.domain.Author;
import com.example.bookstore.domain.Book;
import com.example.bookstore.domain.Genre;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Author author;
    private Genre genre;
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        author = Author.builder()
                .id(1L)
                .name("J.R.R. Tolkien")
                .build();

        genre = Genre.builder()
                .id(1L)
                .name("Fantasy")
                .build();

        book = Book.builder()
                .id(1L)
                .title("The Hobbit")
                .price(BigDecimal.valueOf(20))
                .quantity(10)
                .author(author)
                .genre(genre)
                .build();
    }

    @Test
    void createBook_Success() {
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(genreRepository.findById(genre.getId())).thenReturn(Optional.of(genre));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book created = bookService.create(book);

        assertNotNull(created);
        assertEquals("The Hobbit", created.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void createBook_InvalidAuthor_ThrowsException() {
        book.setAuthor(Author.builder().id(999L).build());
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.create(book));

        assertEquals("Author not found with id: 999", exception.getMessage());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void createBook_InvalidGenre_ThrowsException() {
        book.setGenre(Genre.builder().id(999L).build());
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(genreRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.create(book));

        assertEquals("Genre not found with id: 999", exception.getMessage());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void getById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book found = bookService.getById(1L);

        assertNotNull(found);
        assertEquals("The Hobbit", found.getTitle());
    }

    @Test
    void getById_NotFound_ThrowsException() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getById(2L));

        assertEquals("Book not found with id: 2", exception.getMessage());
    }

    @Test
    void updateBook_Success() {
        Book updatedBook = Book.builder()
                .title("The Hobbit: Updated")
                .price(BigDecimal.valueOf(25))
                .quantity(5)
                .author(author)
                .genre(genre)
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(genreRepository.findById(genre.getId())).thenReturn(Optional.of(genre));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.update(1L, updatedBook);

        assertEquals("The Hobbit: Updated", result.getTitle());
        assertEquals(BigDecimal.valueOf(25), result.getPrice());
        assertEquals(5, result.getQuantity());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void deleteBook_Success() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.delete(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_NotFound_ThrowsException() {
        when(bookRepository.existsById(2L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.delete(2L));

        assertEquals("Book not found with id: 2", exception.getMessage());
        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void getAllBooks_ReturnsList() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<Book> books = bookService.getAll();

        assertEquals(1, books.size());
        assertEquals("The Hobbit", books.get(0).getTitle());
    }
}