package com.sachin.spring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sachin.spring.entity.Book;
import com.sachin.spring.exception.BookNotFoundException;
import com.sachin.spring.exception.BookServiceException;
import com.sachin.spring.repository.BookRepository;

public class BookServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

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
    void testSaveBook_Success() {
        Book book = new Book("1", "Test Author", "Test Genre", true);

        when(repository.save(book)).thenReturn(book);

        String result = service.saveBook(book);

        assertEquals("Book saved successfully!", result);
        verify(repository, times(1)).save(book);
    }

    @Test
    void testSaveBook_Failure() {
        Book book = new Book("Test Book", "Test Author", "Test Genre", true);

        when(repository.save(book)).thenThrow(new RuntimeException("Test Exception"));

        try {
            service.saveBook(book);
        } catch (BookServiceException e) {
            assertEquals("Failed to save the book", e.getMessage());
        }

        verify(repository, times(1)).save(book);
    }

    @Test
    void testGetBook_Success() {
        Long bookId = 1L;
        Book book = new Book("Test Book", "Test Author", "Test Genre", true);

        when(repository.findById(bookId)).thenReturn(Optional.of(book));

        Book result = service.getBook(bookId);

        assertEquals(book, result);
        verify(repository, times(1)).findById(bookId);
    }

    @Test
    void testGetBook_NotFound() {
        Long bookId = 1L;

        when(repository.findById(bookId)).thenReturn(Optional.empty());

        try {
            service.getBook(bookId);
        } catch (BookNotFoundException e) {
            assertEquals("Book not found with id: " + bookId, e.getMessage());
        }

        verify(repository, times(1)).findById(bookId);
    }
    @Test
    void testGetAllBooks_Success() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Book 1", "Author 1", "Genre 1", true));
        books.add(new Book("Book 2", "Author 2", "Genre 2", false));

        when(repository.findAll()).thenReturn(books);

        List<Book> result = service.getAllBooks();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }
    @Test
    void testUpdateBook_Success() {
        Long bookId = 1L;
        Book updatedBook = new Book("Updated Book", "Author 1", "Genre 1", true);
        updatedBook.setId(bookId);

        when(repository.existsById(bookId)).thenReturn(true);
        when(repository.save(updatedBook)).thenReturn(updatedBook);

        String result = service.updateBook(updatedBook);

        assertEquals("Book updated successfully!", result);
        verify(repository, times(1)).save(updatedBook);
    }
    
    @Test
    void testUpdateBook_NotFound() {
        Long bookId = 1L;
        Book updatedBook = new Book("Updated Book", "Author 1", "Genre 1", true);
        updatedBook.setId(bookId);

        when(repository.existsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> service.updateBook(updatedBook));

        verify(repository, never()).save(updatedBook);
    }

    @Test
    void testUpdateBook_Failure() {
        Long bookId = 1L;
        Book updatedBook = new Book("Updated Book", "Author 1", "Genre 1", true);
        updatedBook.setId(bookId);

        when(repository.existsById(bookId)).thenReturn(true);
        when(repository.save(updatedBook)).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(BookServiceException.class, () -> service.updateBook(updatedBook));

        verify(repository, times(1)).save(updatedBook);
    }

    @Test
    void testDeleteBook_Success() {
        Long bookId = 1L;

        when(repository.existsById(bookId)).thenReturn(true);

        String result = service.deleteBook(bookId);

        assertEquals("Book deleted successfully!", result);
        verify(repository, times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteBook_NotFound() {
        Long bookId = null;

        when(repository.existsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> service.deleteBook(bookId));

        verify(repository, never()).deleteById(bookId);
    }


    @Test
    void testDeleteBook_Failure() {
        Long bookId = 1L;

        when(repository.existsById(bookId)).thenReturn(true);
        doThrow(new RuntimeException("Test Exception")).when(repository).deleteById(bookId);

        try {
            service.deleteBook(bookId);
        } catch (BookServiceException e) {
            assertEquals("Failed to delete the book", e.getMessage());
        }

        verify(repository, times(1)).deleteById(bookId);
    }
}
