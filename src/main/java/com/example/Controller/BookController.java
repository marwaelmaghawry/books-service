package com.example.Controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import io.micronaut.http.annotation.Body;

import com.example.Service.BookService;
import com.example.Model.Book;
import com.example.exception.BookNotFoundException;

import java.util.List;

@Controller("/books")
class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Get
    List<Book> retrieveAllBooks() {
        return bookService.listBooks();
    }

    @Get("/{isbn}")
    public Book findBook(String isbn) {
        return bookService.findBook(isbn);
    }

    @Post("/add")
    public Book addBook(@Body Book book) {
        return bookService.addBook(book);
    }




}
