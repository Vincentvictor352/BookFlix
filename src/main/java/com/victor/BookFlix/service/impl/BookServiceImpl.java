package com.victor.BookFlix.service.impl;

import com.victor.BookFlix.dto.BookDTO;
import com.victor.BookFlix.entity.Book;
import com.victor.BookFlix.repository.BookRepository;
import com.victor.BookFlix.service.BookService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Service
public class BookServiceImpl implements BookService {



    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    private Book convertToEntity(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setCategory(dto.getCategory());
        book.setDescription(dto.getDescription());
        book.setPrice(dto.getPrice() != null ? dto.getPrice() : BigDecimal.ZERO);
        book.setCoverImageUrl(dto.getCoverImageUrl());
        book.setFileUrl(dto.getFileUrl());
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        return book;
    }


    @Override
    public Book addBook(BookDTO dto) {
        Book book = convertToEntity(dto);
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, BookDTO dto) {
        return bookRepository.findById(id)
                .map(book -> {
                    if (dto.getTitle() != null) book.setTitle(dto.getTitle());
                    if (dto.getAuthor() != null) book.setAuthor(dto.getAuthor());
                    if (dto.getCategory() != null) book.setCategory(dto.getCategory());
                    if (dto.getDescription() != null) book.setDescription(dto.getDescription());
                    if (dto.getCoverImageUrl() != null) book.setCoverImageUrl(dto.getCoverImageUrl());
                    if (dto.getFileUrl() != null) book.setFileUrl(dto.getFileUrl());
                    if (dto.getPrice() != null) book.setPrice(dto.getPrice());
                    book.setUpdatedAt(LocalDateTime.now());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    @Override
    public List<Book> filterBooksByCategory(String category) {
        return bookRepository.findByCategory(category);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public String getCoverImageUrl(Long id) {
        return bookRepository.findById(id)
                .map(Book::getCoverImageUrl)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
    }

    @Override
    public String getFileUrl(Long id) {
        return bookRepository.findById(id)
                .map(Book::getFileUrl)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
    }

    @Override
    public Map<String, LocalDateTime> getBookTimestamps(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
        return Map.of(
                "createdAt", book.getCreatedAt(),
                "updatedAt", book.getUpdatedAt()
        );
    }
}
