package br.com.catsanddogs.catsanddogs.infrastructure.service;

import br.com.catsanddogs.catsanddogs.domain.service.CatAndDogService;
import br.com.catsanddogs.catsanddogs.domain.service.CatService;
import br.com.catsanddogs.catsanddogs.domain.service.DogService;
import br.com.catsanddogs.catsanddogs.infrastructure.utils.VelocityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CatAndDogServiceImpl implements CatAndDogService {

    private final CatService catService;
    private final DogService dogService;

    @Override
    public Mono<String> getCatAndDogAsync() {
        log.info("Preparing to search for Cats and Dogs images...");
        final var cat = this.catService.getCat().subscribeOn(Schedulers.parallel());
        final var dog = this.dogService.getDog().subscribeOn(Schedulers.parallel());

        return Mono.zip(cat, dog, VelocityUtil.loadCatAndDogTemplate());
    }

    @Override
    public String getCatAndDogSync() {
        log.info("Sync: preparing to search for Cats and Dogs images...");
        final var cat = this.catService.getCatSync();
        final var dog = this.dogService.getDogSync();
        
        return VelocityUtil.loadCatAndDogTemplate().apply(cat.get(), dog.get());
    }
}
