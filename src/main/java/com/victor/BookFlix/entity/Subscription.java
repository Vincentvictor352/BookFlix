package com.victor.BookFlix.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Subscription {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Each subscription belongs to a user
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String plan; // e.g., BASIC, PREMIUM, GOLD
    private Double price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
