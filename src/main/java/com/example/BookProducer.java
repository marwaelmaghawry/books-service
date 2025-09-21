package com.example;

import com.example.Model.Book;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface BookProducer {

    @Topic("books") // topic name
    void sendBookEvent(Book book);
}
