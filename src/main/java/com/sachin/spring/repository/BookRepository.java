package com.sachin.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sachin.spring.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
