package com.example.Service;

import com.example.Model.Book;
import com.example.event.BookProducer;
import com.example.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

import com.example.exception.BookNotFoundException;
import com.example.exception.DuplicateBookException;

import java.util.List;
import java.util.Optional;

@Singleton
public class BookService {

    private final BookRepository bookRepository;
    private final BookProducer bookProducer;

    public BookService(BookRepository bookRepository, BookProducer bookProducer) {
        this.bookRepository = bookRepository;
        this.bookProducer = bookProducer;
    }

    // Seed some books only once when DB is empty
    @PostConstruct
    void initData() {
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book("1491950358", "Building Microservices"));
            bookRepository.save(new Book("1680502395", "Release It!"));
            bookRepository.save(new Book("0321601912", "Continuous Delivery"));
        }
    }

    // Retrieve all books
    public List<Book> listBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    public Book findBook(String isbn) {
        return bookRepository.findById(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }


    public Book addBook(Book book) {
        if (bookRepository.existsById(book.getIsbn())) {
            throw new DuplicateBookException(book.getIsbn());
        }
        return bookRepository.save(book);
    }

}
