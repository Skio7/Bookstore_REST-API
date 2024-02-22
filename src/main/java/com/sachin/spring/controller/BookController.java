package com.sachin.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sachin.spring.entity.Book;
import com.sachin.spring.exception.BookNotFoundException;
import com.sachin.spring.exception.BookServiceException;
import com.sachin.spring.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService service;
    
    @GetMapping
    public List<Book> getAllBooks() {
        return service.getAllBooks();
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        try {
            service.saveBook(book);
            return new ResponseEntity<>("Book saved successfully!", HttpStatus.CREATED);	
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable("id") Long id) {
        try {
            Book book = service.getBook(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>("Book not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable("id") Long id, @RequestBody Book book) {
        try {
            book.setId(id);
            String message = service.updateBook(book);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>("Book not found with id: " + id, HttpStatus.NOT_FOUND);
        } catch (BookServiceException e) {
            return new ResponseEntity<>("Error updating the book: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") Long id) {
        try {
            String message = service.deleteBook(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>("Book not found with id: " + id, HttpStatus.NOT_FOUND);
        } catch (BookServiceException e) {
            return new ResponseEntity<>("Error deleting the book: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

