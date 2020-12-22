package br.com.catsanddogs.catsanddogs.domain.service;

import br.com.catsanddogs.catsanddogs.domain.model.Dog;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface DogService {
    Mono<Dog> getDog();

    Optional<Dog> getDogSync();
}
