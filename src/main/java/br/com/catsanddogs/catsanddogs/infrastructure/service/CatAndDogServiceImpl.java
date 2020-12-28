package br.com.catsanddogs.catsanddogs.infrastructure.service;

import br.com.catsanddogs.catsanddogs.application.enums.Status;
import br.com.catsanddogs.catsanddogs.application.response.CatAndDogResponse;
import br.com.catsanddogs.catsanddogs.domain.model.Cat;
import br.com.catsanddogs.catsanddogs.domain.model.Dog;
import br.com.catsanddogs.catsanddogs.domain.service.CatAndDogService;
import br.com.catsanddogs.catsanddogs.domain.service.CatService;
import br.com.catsanddogs.catsanddogs.domain.service.DogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CatAndDogServiceImpl implements CatAndDogService {

    private final CatService catService;
    private final DogService dogService;

    @Override
    public Mono<CatAndDogResponse> getCatAndDogAsync() {
        log.info("Async: preparing to search for Cats and Dogs images...");
        final var cat = this.catService.getCat()
                .subscribeOn(Schedulers.parallel())
                .defaultIfEmpty(new Cat(""));
        final var dog = this.dogService.getDog()
                .subscribeOn(Schedulers.parallel())
                .defaultIfEmpty(new Dog("", Status.FAIL.name()));

        return Mono.zip(cat, dog, (c, d) -> new CatAndDogResponse(Status.SUCCESS, c.getFile(), d.getMessage()));
    }

    @Override
    public CatAndDogResponse getCatAndDogSync() {
        log.info("Sync: preparing to search for Cats and Dogs images...");
        final var cat = this.catService.getCatSync();
        final var dog = this.dogService.getDogSync();
        return new CatAndDogResponse(Status.SUCCESS,
                cat.map(Cat::getFile).orElse(""),
                dog.map(Dog::getMessage).orElse(""));
    }
}
