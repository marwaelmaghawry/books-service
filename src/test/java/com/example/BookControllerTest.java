package com.example;

import com.example.Model.Book;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import jakarta.inject.Inject;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@MicronautTest
@TestInstance(PER_CLASS)
class BookControllerTest {

    private static final Collection<Book> received = new ConcurrentLinkedDeque<>();

    @Inject
    AnalyticsListener analyticsListener;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testMessageIsPublishedToKafkaWhenBookFound() {
        String isbn = "1491950358";

        Optional<Book> result = retrieveGet("/books/" + isbn);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(isbn, result.get().getIsbn());

        await().atMost(5, SECONDS).until(() -> !received.isEmpty());

        assertEquals(1, received.size());
        Book bookFromKafka = received.iterator().next();
        assertNotNull(bookFromKafka);
        assertEquals(isbn, bookFromKafka.getIsbn());
    }

    @Test
    void testMessageIsNotPublishedToKafkaWhenBookNotFound() throws Exception {
        assertThrows(HttpClientResponseException.class, () -> {
            retrieveGet("/books/INVALID");
        });

        Thread.sleep(5_000);
        assertEquals(0, received.size());
    }

    @AfterEach
    void cleanup() {
        received.clear();
    }

    @KafkaListener(offsetReset = EARLIEST)
    static class AnalyticsListener {

        @Topic("analytics")
        void updateAnalytics(Book book) {
            received.add(book);
        }
    }

    private Optional<Book> retrieveGet(String url) {
        return client.toBlocking().retrieve(HttpRequest.GET(url), Argument.of(Optional.class, Book.class));
    }
}
