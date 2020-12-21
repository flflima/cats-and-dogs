package br.com.catsanddogs.catsanddogs.domain.service;

import br.com.catsanddogs.catsanddogs.domain.model.Cat;
import reactor.core.publisher.Mono;

public interface CatService {
    Mono<Cat> getCat();
}
