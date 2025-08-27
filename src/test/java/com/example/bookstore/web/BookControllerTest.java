package com.example.bookstore.web;

import com.example.bookstore.domain.Author;
import com.example.bookstore.domain.Book;
import com.example.bookstore.domain.Genre;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.GenreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

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
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    private Author author;
    private Genre genre;
    private Book book;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        genreRepository.deleteAll();

        author = authorRepository.save(Author.builder().name("J.K. Rowling").build());
        genre = genreRepository.save(Genre.builder().name("Fantasy").build());

        book = Book.builder()
                .title("Harry Potter and the Philosopher's Stone")
                .price(BigDecimal.valueOf(30))
                .quantity(50)
                .author(author)
                .genre(genre)
                .build();
        book = bookRepository.save(book);
    }

    @Test
    void testCreateBook() throws Exception {
        Book newBook = Book.builder()
                .title("New Book")
                .price(BigDecimal.valueOf(20))
                .quantity(10)
                .author(author)
                .genre(genre)
                .build();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Book")))
                .andExpect(jsonPath("$.author.id", is(author.getId().intValue())))
                .andExpect(jsonPath("$.genre.id", is(genre.getId().intValue())));
    }

    @Test
    void testGetBookById() throws Exception {
        mockMvc.perform(get("/api/books/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(book.getTitle())));
    }

    @Test
    void testGetBookById_NotFound() throws Exception {
        mockMvc.perform(get("/api/books/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testUpdateBook() throws Exception {
        book.setTitle("Updated Title");

        mockMvc.perform(put("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")));
    }

    @Test
    void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", book.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testSearchBooksByTitle() throws Exception {
        mockMvc.perform(get("/api/books/search")
                        .param("title", "Harry Potter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(book.getTitle())));
    }

    @Test
    void testSearchBooksByAuthor() throws Exception {
        mockMvc.perform(get("/api/books/search")
                        .param("author", "Rowling"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author.name", is(author.getName())));
    }

    @Test
    void testSearchBooksByGenre() throws Exception {
        mockMvc.perform(get("/api/books/search")
                        .param("genre", "Fantasy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].genre.name", is(genre.getName())));
    }
}