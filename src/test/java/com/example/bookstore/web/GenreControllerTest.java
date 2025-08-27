package com.example.bookstore.web;

import com.example.bookstore.domain.Genre;
import com.example.bookstore.repository.GenreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Genre genre;

    @BeforeEach
    void setUp() {
        genreRepository.deleteAll();
        genre = genreRepository.save(Genre.builder().name("Fantasy").build());
    }

    @Test
    void testCreateGenre() throws Exception {
        Genre newGenre = Genre.builder().name("Sci-Fi").build();

        mockMvc.perform(post("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGenre)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Sci-Fi")));
    }

    @Test
    void testGetGenreById() throws Exception {
        mockMvc.perform(get("/api/genres/{id}", genre.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(genre.getName())));
    }

    @Test
    void testGetAllGenres() throws Exception {
        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testUpdateGenre() throws Exception {
        genre.setName("Updated Genre");
        mockMvc.perform(put("/api/genres/{id}", genre.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genre)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Genre")));
    }

    @Test
    void testDeleteGenre() throws Exception {
        mockMvc.perform(delete("/api/genres/{id}", genre.getId()))
                .andExpect(status().isNoContent());
    }
}