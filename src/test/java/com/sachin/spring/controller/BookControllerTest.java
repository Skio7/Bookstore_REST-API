package com.sachin.spring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sachin.spring.entity.Book;
import com.sachin.spring.exception.BookNotFoundException;
import com.sachin.spring.exception.BookServiceException;
import com.sachin.spring.service.BookService;

public class BookControllerTest {

    @Mock
    private BookService service;

    @InjectMocks
    private BookController controller;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Test Book", "Test Author", "Test Genre", true));

        when(service.getAllBooks()).thenReturn(books);

        List<Book> result = controller.getAllBooks();

        assertEquals(books, result);
        verify(service, times(1)).getAllBooks();
    }

    @Test
    void testAddBook_Success() {
        Book book = new Book("Test Book", "Test Author", "Test Genre", true);

        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Book saved successfully!", HttpStatus.CREATED);

        when(service.saveBook(any(Book.class))).thenReturn("Book saved successfully!");

        ResponseEntity<String> result = controller.addBook(book);

        assertEquals(expectedResponse, result);
        verify(service, times(1)).saveBook(book);
    }

    @Test
    void testAddBook_Failure() {
        Book book = new Book("Test Book", "Test Author", "Test Genre", true);

        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Error: Test Exception", HttpStatus.INTERNAL_SERVER_ERROR);

        when(service.saveBook(any(Book.class))).thenThrow(new RuntimeException("Test Exception"));

        ResponseEntity<String> result = controller.addBook(book);

        assertEquals(expectedResponse, result);
        verify(service, times(1)).saveBook(book);
    }

    // Similarly, you can write tests for getBook, updateBook, and deleteBook methods
    // Make sure to cover success and failure scenarios.

    // ...
    @Test
    void testGetBook_Success() {
        Long bookId = 1L;
        Book book = new Book("Test Book", "Test Author", "Test Genre", true);

        when(service.getBook(bookId)).thenReturn(book);

        ResponseEntity<?> result = controller.getBook(bookId);

        assertEquals(new ResponseEntity<>(book, HttpStatus.OK), result);
        verify(service, times(1)).getBook(bookId);
    }

    @Test
    void testGetBook_NotFound() {
        Long bookId = 1L;

        when(service.getBook(bookId)).thenThrow(new BookNotFoundException("Book not found with id: " + bookId));

        ResponseEntity<?> result = controller.getBook(bookId);

        assertEquals(new ResponseEntity<>("Book not found with id: " + bookId, HttpStatus.NOT_FOUND), result);
        verify(service, times(1)).getBook(bookId);
    }

    @Test
    void testUpdateBook_Success() {
        Long bookId = 1L;
        Book book = new Book("Test Book", "Test Author", "Test Genre", true);

        when(service.updateBook(any(Book.class))).thenReturn("Book updated successfully!");

        ResponseEntity<?> result = controller.updateBook(bookId, book);

        assertEquals(new ResponseEntity<>("Book updated successfully!", HttpStatus.OK), result);
        verify(service, times(1)).updateBook(book);
    }

    @Test
    void testUpdateBook_NotFound() {
        Long bookId = 1L;
        Book book = new Book("Test Book", "Test Author", "Test Genre", true);

        when(service.updateBook(any(Book.class))).thenThrow(new BookNotFoundException("Book not found with id: " + bookId));

        ResponseEntity<?> result = controller.updateBook(bookId, book);

        assertEquals(new ResponseEntity<>("Book not found with id: " + bookId, HttpStatus.NOT_FOUND), result);
        verify(service, times(1)).updateBook(book);
    }

    @Test
    void testUpdateBook_Failure() {
        Long bookId = 1L;
        Book book = new Book("Test Book", "Test Author", "Test Genre", true);

        when(service.updateBook(any(Book.class))).thenThrow(new BookServiceException("Error updating the book"));

        ResponseEntity<?> result = controller.updateBook(bookId, book);

        assertEquals(new ResponseEntity<>("Error updating the book: Error updating the book", HttpStatus.INTERNAL_SERVER_ERROR), result);
        verify(service, times(1)).updateBook(book);
    }

    @Test
    void testDeleteBook_Success() {
        Long bookId = 1L;

        when(service.deleteBook(bookId)).thenReturn("Book deleted successfully!");

        ResponseEntity<?> result = controller.deleteBook(bookId);

        assertEquals(new ResponseEntity<>("Book deleted successfully!", HttpStatus.OK), result);
        verify(service, times(1)).deleteBook(bookId);
    }

    @Test
    void testDeleteBook_NotFound() {
        Long bookId = 1L;

        when(service.deleteBook(bookId)).thenThrow(new BookNotFoundException("Book not found with id: " + bookId));

        ResponseEntity<?> result = controller.deleteBook(bookId);

        assertEquals(new ResponseEntity<>("Book not found with id: " + bookId, HttpStatus.NOT_FOUND), result);
        verify(service, times(1)).deleteBook(bookId);
    }

    @Test
    void testDeleteBook_Failure() {
        Long bookId = 1L;

        when(service.deleteBook(bookId)).thenThrow(new BookServiceException("Error deleting the book"));

        ResponseEntity<?> result = controller.deleteBook(bookId);

        assertEquals(new ResponseEntity<>("Error deleting the book: Error deleting the book", HttpStatus.INTERNAL_SERVER_ERROR), result);
        verify(service, times(1)).deleteBook(bookId);
    }


}
