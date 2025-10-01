package com.victor.BookFlix.service;

import com.victor.BookFlix.dto.BookDTO;
import com.victor.BookFlix.entity.Book;
import com.victor.BookFlix.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
public interface BookService {
    Book addBook(BookDTO dto);
    Book updateBook(Long id, BookDTO dto);
    List<Book> getAllBooks();
    Book getBookById(Long id);
    List<Book> searchBooksByTitle(String title);
    List<Book> searchBooksByAuthor(String author);
    List<Book> filterBooksByCategory(String category);
    void deleteBook(Long id);
    String getCoverImageUrl(Long id);
    String getFileUrl(Long id);
    Map<String, LocalDateTime> getBookTimestamps(Long id);
}