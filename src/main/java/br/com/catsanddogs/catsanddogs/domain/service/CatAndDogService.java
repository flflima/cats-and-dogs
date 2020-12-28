package br.com.catsanddogs.catsanddogs.domain.service;

import br.com.catsanddogs.catsanddogs.application.response.CatAndDogResponse;
import reactor.core.publisher.Mono;

public interface CatAndDogService {

    Mono<CatAndDogResponse> getCatAndDogAsync();

    CatAndDogResponse getCatAndDogSync();
}
