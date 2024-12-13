package com.gharkness.sdjpajdbc.dao;

import com.gharkness.sdjpajdbc.domain.Book;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@AllArgsConstructor
public class BookDaoImpl implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Book getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM book WHERE id = ?", getBookMapper(), id);
    }

    @Override
    public Book findBookByTitle(String title) {
        return jdbcTemplate.queryForObject("SELECT * FROM book WHERE title = ?", getBookMapper(), title);
    }

    @Override
    public Book saveNewBook(Book book) {
        jdbcTemplate.update("INSERT INTO book (title, isbn, publisher) VALUES (?, ?, ?)",
                book.getTitle(), book.getIsbn(), book.getPublisher());

        Long createdId = jdbcTemplate.queryForObject("SELECT last_insert_id()", Long.class);

        return this.getById(createdId);
    }

    @Override
    public Book updateBook(Book book) {
        jdbcTemplate.update("UPDATE book SET title = ?, isbn = ?, publisher = ? WHERE id = ?",
                book.getTitle(),
                book.getIsbn(),
                book.getPublisher(),
                book.getId()
        );

        return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }

    private BookMapper getBookMapper() {
        return new BookMapper();
    }
}
