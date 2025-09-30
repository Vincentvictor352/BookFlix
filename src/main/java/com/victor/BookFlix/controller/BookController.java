package com.victor.BookFlix.controller;

import com.victor.BookFlix.dto.BookDTO;
import com.victor.BookFlix.entity.Book;
import com.victor.BookFlix.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // ====================== ADMIN ONLY ======================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody BookDTO bookDTO) {
        Book book = bookService.addBook(bookDTO);
        return ResponseEntity.ok(book);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        Book book = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(book);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }

    // ====================== USER ACCESS ======================
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // Example: filter by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Book>> filterBooksByCategory(@PathVariable String category) {
        return ResponseEntity.ok(bookService.filterBooksByCategory(category));
    }

    // Example: search by author
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String author) {
        return ResponseEntity.ok(bookService.searchBooksByAuthor(author));
    }

}
