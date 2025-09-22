package com.example.filter;



import com.example.AnalyticsClient;
import com.example.Model.Book;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import reactor.core.publisher.Flux;
import org.reactivestreams.Publisher;

@Filter("/books/?*")
public class AnalyticsFilter implements HttpServerFilter {

    private final AnalyticsClient analyticsClient;

    public AnalyticsFilter(AnalyticsClient analyticsClient) {
        this.analyticsClient = analyticsClient;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request,
                                                      ServerFilterChain chain) {
        return Flux
                .from(chain.proceed(request))
                .flatMap(response -> {
                    Book book = response.getBody(Book.class).orElse(null);
                    if (book == null) {
                        return Flux.just(response);
                    }
                    return Flux.from(analyticsClient.updateAnalytics(book)).map(b -> response);
                });
    }
}
