package com.gharkness.sdjpajdbc.dao;

import com.gharkness.sdjpajdbc.domain.Book;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@AllArgsConstructor
public class BookDaoImpl implements BookDao {

    private final DataSource source;

    @Override
    public Book getById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM book WHERE id = ?");
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getBookFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(connection, preparedStatement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Book findBookByTitle(String title) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM book WHERE title = ?");
            preparedStatement.setString(1, title);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getBookFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(connection, preparedStatement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Book saveNewBook(Book book) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO book (title, isbn, publisher, author_id) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getIsbn());
            preparedStatement.setString(3, book.getPublisher());
            preparedStatement.setLong(4, book.getAuthorId());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("SELECT * FROM book WHERE title = ?");
            preparedStatement.setString(1, book.getTitle());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getBookFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(connection, preparedStatement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Book updateBook(Book book) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = source.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE book SET title = ?, isbn = ?, publisher = ?, author_id = ? WHERE id = ?");
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getIsbn());
            preparedStatement.setString(3, book.getPublisher());
            preparedStatement.setLong(4, book.getAuthorId());
            preparedStatement.setLong(5, book.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(connection, preparedStatement, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = source.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM book WHERE id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(connection, preparedStatement, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeAll(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }

        if (preparedStatement != null) {
            preparedStatement.close();
        }

        if (connection != null) {
            connection.close();
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setTitle(resultSet.getString("title"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setAuthorId(resultSet.getLong("author_id"));

        return book;
    }
}
