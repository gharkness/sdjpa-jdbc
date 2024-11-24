package com.gharkness.sdjpajdbc.repositories;

import com.gharkness.sdjpajdbc.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
