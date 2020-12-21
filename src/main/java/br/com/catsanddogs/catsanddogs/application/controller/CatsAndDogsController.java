package br.com.catsanddogs.catsanddogs.application.controller;

import br.com.catsanddogs.catsanddogs.application.enums.Status;
import br.com.catsanddogs.catsanddogs.application.response.CatResponse;
import br.com.catsanddogs.catsanddogs.domain.model.Cat;
import br.com.catsanddogs.catsanddogs.domain.model.Dog;
import br.com.catsanddogs.catsanddogs.domain.service.CatAndDogService;
import br.com.catsanddogs.catsanddogs.domain.service.CatService;
import br.com.catsanddogs.catsanddogs.domain.service.DogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@RestController
@RequestMapping("/cats-and-dogs")
@RequiredArgsConstructor
public class CatsAndDogsController {

    @Autowired
    private final CatService catService;

    @Autowired
    private final CatAndDogService catAndDogService;

    @RequestMapping(value = "cats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Mono<CatResponse>> getCat() {
        Mono<CatResponse> cat = this.catService.getCat()
                                        .subscribeOn(Schedulers.parallel())
                                        .map(c -> new CatResponse(Status.SUCCESS, c));
        return new ResponseEntity<>(cat, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    Mono<String> getCatAndDog() {
        MDC.put("correlationId", UUID.randomUUID().toString());
        try {
            return this.catAndDogService.getCatAndDog();
        } finally {
            MDC.clear();
        }
    }
}
