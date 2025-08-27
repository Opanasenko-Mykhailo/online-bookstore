package com.example.bookstore.service.impl;

import com.example.bookstore.domain.Genre;
import com.example.bookstore.repository.GenreRepository;
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

class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    private Genre genre;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        genre = Genre.builder().id(1L).name("Fantasy").build();
    }

    @Test
    void createGenre_Success() {
        when(genreRepository.save(any(Genre.class))).thenReturn(genre);
        Genre created = genreService.create(genre);
        assertEquals("Fantasy", created.getName());
        verify(genreRepository, times(1)).save(genre);
    }

    @Test
    void getById_Success() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        Genre found = genreService.getById(1L);
        assertEquals("Fantasy", found.getName());
    }

    @Test
    void getById_NotFound_ThrowsException() {
        when(genreRepository.findById(2L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> genreService.getById(2L));
        assertEquals("Genre not found with id: 2", ex.getMessage());
    }

    @Test
    void getAllGenres() {
        when(genreRepository.findAll()).thenReturn(List.of(genre));
        List<Genre> list = genreService.getAll();
        assertEquals(1, list.size());
    }

    @Test
    void updateGenre_Success() {
        Genre updated = Genre.builder().name("Sci-Fi").build();
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(genreRepository.save(any(Genre.class))).thenReturn(updated);

        Genre result = genreService.update(1L, updated);
        assertEquals("Sci-Fi", result.getName());
    }

    @Test
    void deleteGenre_Success() {
        when(genreRepository.existsById(1L)).thenReturn(true);
        genreService.delete(1L);
        verify(genreRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteGenre_NotFound_ThrowsException() {
        when(genreRepository.existsById(2L)).thenReturn(false);
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> genreService.delete(2L));
        assertEquals("Genre not found with id: 2", ex.getMessage());
    }
}