package com.victor.BookFlix.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // store the author name or you can later change to a relation to an Author entity
    @Column(nullable = false)
    private String author;

    private String category; // e.g. Fiction, Health, Romance

    @Column(length = 2000)
    private String description;

    private String coverImageUrl; // URL to book cover

    private String fileUrl; // URL or path to book file (PDF, EPUB, etc.)

    @Column(precision = 10, scale = 2)
    private BigDecimal price; // price in chosen currency (optional for subscription-only)

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Book() {}

    public Book(String title, String author, String category, String description,
                String coverImageUrl, String fileUrl, BigDecimal price) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.fileUrl = fileUrl;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ---------- Getters & Setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
