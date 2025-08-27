package com.example.bookstore.web;

import com.example.bookstore.domain.Author;
import com.example.bookstore.repository.AuthorRepository;
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
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Author author;

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll();
        author = authorRepository.save(Author.builder().name("Author One").build());
    }

    @Test
    void testCreateAuthor() throws Exception {
        Author newAuthor = Author.builder().name("Author Two").build();

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAuthor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Author Two")));
    }

    @Test
    void testGetAuthorById() throws Exception {
        mockMvc.perform(get("/api/authors/{id}", author.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(author.getName())));
    }

    @Test
    void testGetAllAuthors() throws Exception {
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testUpdateAuthor() throws Exception {
        author.setName("Updated Name");
        mockMvc.perform(put("/api/authors/{id}", author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")));
    }

    @Test
    void testDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/api/authors/{id}", author.getId()))
                .andExpect(status().isNoContent());
    }
}