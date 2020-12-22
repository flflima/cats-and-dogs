package br.com.catsanddogs.catsanddogs.domain.service;

import reactor.core.publisher.Mono;

public interface CatAndDogService {

    Mono<String> getCatAndDogAsync();

    String getCatAndDogSync();
}
