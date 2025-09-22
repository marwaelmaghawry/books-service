package com.example.exception;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import com.example.exception.BookNotFoundException;

@Produces
@Singleton
public class BookNotFoundExceptionHandler implements ExceptionHandler<BookNotFoundException, HttpResponse<JsonError>> {

    @Override
    public HttpResponse<JsonError> handle(io.micronaut.http.HttpRequest request, BookNotFoundException exception) {
        JsonError error = new JsonError(exception.getMessage());
        return HttpResponse.notFound(error);
    }
}
