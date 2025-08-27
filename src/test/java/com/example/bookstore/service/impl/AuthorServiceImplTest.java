package com.example.bookstore.service.impl;

import com.example.bookstore.domain.Author;
import com.example.bookstore.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        author = Author.builder().id(1L).name("Author Name").build();
    }

    @Test
    void createAuthor_Success() {
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        Author created = authorService.create(author);
        assertEquals("Author Name", created.getName());
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void getById_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        Author found = authorService.getById(1L);
        assertEquals(author.getName(), found.getName());
    }

    @Test
    void getById_NotFound_ThrowsException() {
        when(authorRepository.findById(2L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> authorService.getById(2L));
        assertEquals("Author not found with id: 2", ex.getMessage());
    }

    @Test
    void getAllAuthors() {
        when(authorRepository.findAll()).thenReturn(List.of(author));
        List<Author> list = authorService.getAll();
        assertEquals(1, list.size());
    }

    @Test
    void updateAuthor_Success() {
        Author updated = Author.builder().name("Updated Name").build();
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(updated);

        Author result = authorService.update(1L, updated);
        assertEquals("Updated Name", result.getName());
    }

    @Test
    void deleteAuthor_Success() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        authorService.delete(1L);
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAuthor_NotFound_ThrowsException() {
        when(authorRepository.existsById(2L)).thenReturn(false);
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> authorService.delete(2L));
        assertEquals("Author not found with id: 2", ex.getMessage());
    }
}