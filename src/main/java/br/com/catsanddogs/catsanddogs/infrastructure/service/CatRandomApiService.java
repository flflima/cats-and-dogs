package br.com.catsanddogs.catsanddogs.infrastructure.service;

import br.com.catsanddogs.catsanddogs.application.properties.ApiProperties;
import br.com.catsanddogs.catsanddogs.domain.model.Cat;
import br.com.catsanddogs.catsanddogs.domain.service.CatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CatRandomApiService implements CatService {

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final ApiProperties apiProperties;

    @Override
    public Mono<Cat> getCat() {
        final var request = new Request.Builder()
                .url(this.apiProperties.getCats())
                .get()
                .build();

        log.info("Async: searching a Cat Image...");

        try (final var response = this.client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Unexpected code " + response);
            }

            log.info("Cat Image successfully found!");

            final var body = response.body();
            final var responseAsString = body != null ? body.string() : "";

            log.debug(responseAsString);

            return Mono.just(this.objectMapper.readValue(responseAsString, Cat.class));
        } catch (Exception e) {
            log.error("Error on searching a cat image: ", e);
            e.printStackTrace();
        }

        return Mono.empty();
    }

    @Override
    public Optional<Cat> getCatSync() {
        final var request = new Request.Builder()
                .url(this.apiProperties.getCats())
                .get()
                .build();

        log.info("Sync: searching a Cat Image...");

        try (final var response = this.client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Unexpected code " + response);
            }

            log.info("Cat Image successfully found!");

            final var body = response.body();
            final var responseAsString = body != null ? body.string() : "";

            log.debug(responseAsString);

            return Optional.of(this.objectMapper.readValue(responseAsString, Cat.class));
        } catch (Exception e) {
            log.error("Error on searching a cat image: ", e);
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
