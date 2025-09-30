package com.victor.BookFlix.repository;

import com.victor.BookFlix.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find books by category
    List<Book> findByCategory(String category);

    // Search books by title (case insensitive)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Search books by author (case insensitive)
    List<Book> findByAuthorContainingIgnoreCase(String author);
}


