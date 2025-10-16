package com.book_recs.demo.model; 
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_book_interactions")
public class UserBookInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    @Column(nullable = false)
    private Integer rating; // 1-10 scale
    
    @Column(length = 1000)
    private String review;
    
    @Column
    private LocalDateTime ratedAt;

    // Default constructor (required by JPA)
    public UserBookInteraction() {
        this.ratedAt = LocalDateTime.now();
    }

    // Constructor
    public UserBookInteraction(User user, Book book, Integer rating) {
        this();
        this.user = user;
        this.book = book;
        this.rating = rating;
    }

    // Constructor with review
    public UserBookInteraction(User user, Book book, Integer rating, String review) {
        this(user, book, rating);
        this.review = review;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public LocalDateTime getRatedAt() {
        return ratedAt;
    }

    public void setRatedAt(LocalDateTime ratedAt) {
        this.ratedAt = ratedAt;
    }

    @Override
    public String toString() {
        return "UserBookInteraction{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : null) +
                ", book=" + (book != null ? book.getId() : null) +
                ", rating=" + rating +
                ", ratedAt=" + ratedAt +
                '}';
    }
}