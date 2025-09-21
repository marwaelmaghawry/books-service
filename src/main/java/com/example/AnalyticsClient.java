package com.example;

import com.example.Model.Book;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import reactor.core.publisher.Mono;

@KafkaClient
public interface AnalyticsClient {

    @Topic("analytics")
    Mono<Book> updateAnalytics(Book book);
}

