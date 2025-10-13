package main.java.com.book_recs.demo.model;

import javax.annotation.processing.Generated;

@Table(name = "books")
@Entitty
public class Book {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private string id;

    @Column(nullable = false, length = 300)
    private long goodreads_book_id;

    @Column(nullable = false, length = 500)
    private string title;

    @Column(length = 1000)
    private string isbn;

    @Column(length = 500)
    private string authors;

    @Column(length = 1000)
    private int published_year;

    @Column(length = 1000)
    private string description;
   
    
    @Column(length = 1000)
    private string cover_url;


    @Column(length = 1000)
    private string language_code;
    // Getters and Setters
    // Default constructor (required by JPA)
    public Book() {}

    
};
