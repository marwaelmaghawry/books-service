package com.example.exception;

import io.micronaut.core.annotation.Introspected; // ✅ optional, for reflection
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import io.micronaut.serde.annotation.Serdeable; // ✅ Add this

@Produces
@Singleton
public class DuplicateBookExceptionHandler implements ExceptionHandler<DuplicateBookException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(io.micronaut.http.HttpRequest request, DuplicateBookException exception) {
        return HttpResponse.badRequest(new ErrorResponse(exception.getMessage()));
    }

    @Serdeable // ✅ Make this class serializable
    public static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
