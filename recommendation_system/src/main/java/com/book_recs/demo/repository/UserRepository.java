package com.book_recs.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.book_recs.demo.model.User; // Import the User class

//define hte connection to hte jpa repository
public interface UserRepository extends JpaRepository<User, Long> {

	
}
