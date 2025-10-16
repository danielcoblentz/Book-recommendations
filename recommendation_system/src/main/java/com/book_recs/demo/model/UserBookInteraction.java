package main.java.com.book_recs.demo.model; 
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_book_interactions")
@Getter
@Setter
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
    }}