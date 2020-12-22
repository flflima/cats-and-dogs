package br.com.catsanddogs.catsanddogs.domain.service;

import br.com.catsanddogs.catsanddogs.domain.model.Cat;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface CatService {
    Mono<Cat> getCat();

    Optional<Cat> getCatSync();
}
