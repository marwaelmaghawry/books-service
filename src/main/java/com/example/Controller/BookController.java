package com.example.Controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import com.example.Service.BookService;
import com.example.Model.Book;
import com.example.BookProducer;

import java.util.List;
import java.util.Optional;

@Controller("/books")
class BookController {

    private final BookService bookService;
    private final BookProducer bookProducer; // ✅ Inject Kafka producer

    BookController(BookService bookService, BookProducer bookProducer) {
        this.bookService = bookService;
        this.bookProducer = bookProducer;
    }

    @Get
    List<Book> listAll() {
        return bookService.listAll();
    }

    @Get("/{isbn}")
    Optional<Book> findBook(String isbn) {
        Optional<Book> book = bookService.findByIsbn(isbn);

        // ✅ If found, send event to Kafka
        book.ifPresent(bookProducer::sendBookEvent);

        return book;
    }
}
