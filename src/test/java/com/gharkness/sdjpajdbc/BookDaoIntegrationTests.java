package com.gharkness.sdjpajdbc;

import com.gharkness.sdjpajdbc.dao.AuthorDaoImpl;
import com.gharkness.sdjpajdbc.dao.BookDao;
import com.gharkness.sdjpajdbc.dao.BookDaoImpl;
import com.gharkness.sdjpajdbc.domain.Author;
import com.gharkness.sdjpajdbc.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@Import(BookDaoImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookDaoIntegrationTests {

    @Autowired
    BookDao bookDao;

    @Test
    void testGetBookById() {
        Book book = bookDao.getById(1L);

        assertThat(book).isNotNull();
    }

    @Test
    void testFindBookByTitle() {
        Book book = bookDao.findBookByTitle("Clean Code");

        assertThat(book).isNotNull();
    }

    @Test
    void testSaveNewBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthorId(1L);
        Book saved = bookDao.saveNewBook(book);

        assertThat(saved).isNotNull();
    }

    @Test
    void testUpdateBook() {
        Book book = new Book();
        book.setTitle("Testing Book");
        book.setAuthorId(1L);

        Book saved = bookDao.saveNewBook(book);

        saved.setTitle("Updated Book");
        Book updated = bookDao.updateBook(saved);

        assertThat(updated.getTitle()).isEqualTo("Updated Book");
    }

    @Test
    void testDeleteBookById() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthorId(1L);

        Book saved = bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());

        Book deleted = bookDao.getById(saved.getId());

        assertThat(deleted).isNull();
    }
}
