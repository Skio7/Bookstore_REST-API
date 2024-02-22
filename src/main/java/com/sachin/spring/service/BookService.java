package com.sachin.spring.service;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sachin.spring.entity.Book;
import com.sachin.spring.repository.BookRepository;

import ch.qos.logback.classic.Logger;

import com.sachin.spring.exception.BookNotFoundException;
import com.sachin.spring.exception.BookServiceException;

@Service
public class BookService {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(BookService.class);

	@Autowired
	private BookRepository repository;

	public String saveBook(Book book) {
		try {
			repository.save(book);
			logger.info("Book saved successfully!");
			return "Book saved successfully!";
		} catch (Exception e) {
			logger.error("Failed to save the book: {}", e.getMessage());
			throw new BookServiceException("Failed to save the book");
		}
	}

	public Book getBook(Long id) {
		return repository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
	}

	public List<Book> getAllBooks() {
		return repository.findAll();
	}

	public String updateBook(Book book) {
		try {
			Long id = book.getId();
			if (id == null) {
				throw new BookServiceException("Book ID cannot be null");
			}

			if (!repository.existsById(id)) {
				throw new BookNotFoundException("Book not found with id: " + id);
			}

			repository.save(book);
			logger.info("Book updated successfully!");
			return "Book updated successfully!";
		} catch (BookNotFoundException e) {
			logger.error("Failed to update the book: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Failed to update the book: {}", e.getMessage());
			throw new BookServiceException("Failed to update the book");
		}
	}

	public String deleteBook(Long id) {
	    try {
	        if (repository.existsById(id)) {
	            repository.deleteById(id);
	            logger.info("Book deleted successfully!");
	            return "Book deleted successfully!";
	        } else {
	            throw new BookNotFoundException("Book not found with id: " + id);
	        }
	    } catch (BookNotFoundException e) {
	        // Re-throw BookNotFoundException to propagate it up
	        throw e;
	    } catch (Exception e) {
	        logger.error("Failed to delete the book: {}", e.getMessage());
	        throw new BookServiceException("Failed to delete the book");
	    }
	}



}
