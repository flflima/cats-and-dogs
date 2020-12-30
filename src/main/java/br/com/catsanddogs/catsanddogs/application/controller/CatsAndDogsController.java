package br.com.catsanddogs.catsanddogs.application.controller;

import br.com.catsanddogs.catsanddogs.application.enums.Status;
import br.com.catsanddogs.catsanddogs.application.response.CatAndDogResponse;
import br.com.catsanddogs.catsanddogs.application.response.CatResponse;
import br.com.catsanddogs.catsanddogs.domain.service.CatAndDogService;
import br.com.catsanddogs.catsanddogs.domain.service.CatService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@RestController
@RequestMapping("/cats-and-dogs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CatsAndDogsController {

    @Autowired
    private CatService catService;

    @Autowired
    private CatAndDogService catAndDogService;

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    Mono<CatAndDogResponse> getCatAndDog() {
        MDC.put("correlationId", UUID.randomUUID().toString());
        try {
            return this.catAndDogService.getCatAndDogAsync();
        } finally {
            MDC.clear();
        }
    }

    @RequestMapping(value = "sync", method = RequestMethod.GET, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    CatAndDogResponse getCatAndDogSync() {
        MDC.put("correlationId", UUID.randomUUID().toString());
        try {
            return this.catAndDogService.getCatAndDogSync();
        } finally {
            MDC.clear();
        }
    }
}
