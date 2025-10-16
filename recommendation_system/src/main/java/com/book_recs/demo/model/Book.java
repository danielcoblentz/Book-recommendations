package com.book_recs.demo.model;

import javax.persistence.Column;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "books")
@Entity
public class Book {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id;

    @Column(nullable = false, length = 300)
    private long goodreads_book_id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(length = 1000)
    private String isbn;

    @Column(length = 500)
    private String authors;

    @Column(length = 1000)
    private int published_year;

    @Column(length = 1000)
    private String description;
   
    
    @Column(length = 1000)
    private String cover_url;


    @Column(length = 1000)
    private String language_code;
    // Getters and Setters
    // Default constructor 
    public Book() {}
    public String getId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }

    
};
