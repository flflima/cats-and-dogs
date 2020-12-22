package br.com.catsanddogs.catsanddogs.infrastructure.service;

import br.com.catsanddogs.catsanddogs.application.properties.ApiProperties;
import br.com.catsanddogs.catsanddogs.domain.model.Dog;
import br.com.catsanddogs.catsanddogs.domain.service.DogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DogApiService implements DogService {

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final ApiProperties apiProperties;

    @Override
    public Mono<Dog> getDog() {
        final var request = new Request.Builder()
                .url(this.apiProperties.getDogs())
                .get()
                .build();

        log.info("Searching a Dog Image...");
        try (final var response = this.client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            log.info("Dog Image successfully found!");
            final var responseAsString = response.body() != null ? response.body().string() : "";
            log.debug(responseAsString);
            return Mono.just(this.objectMapper.readValue(responseAsString, Dog.class));
        } catch (IOException e) {
            log.error("Error: ", e);
            e.printStackTrace();
        }

        return Mono.empty();
    }

    @Override
    public Optional<Dog> getDogSync() {
        final var request = new Request.Builder()
                .url(this.apiProperties.getDogs())
                .get()
                .build();

        log.info("Searching a Dog Image...");
        try (final var response = this.client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            log.info("Dog Image successfully found!");
            final var responseAsString = response.body() != null ? response.body().string() : "";
            log.debug(responseAsString);
            return Optional.of(this.objectMapper.readValue(responseAsString, Dog.class));
        } catch (IOException e) {
            log.error("Error: ", e);
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
