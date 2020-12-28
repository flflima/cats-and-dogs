package br.com.catsanddogs.catsanddogs.infrastructure.service;

import br.com.catsanddogs.catsanddogs.application.enums.Status;
import br.com.catsanddogs.catsanddogs.application.response.CatAndDogResponse;
import br.com.catsanddogs.catsanddogs.domain.model.Cat;
import br.com.catsanddogs.catsanddogs.domain.model.Dog;
import br.com.catsanddogs.catsanddogs.domain.service.CatService;
import br.com.catsanddogs.catsanddogs.domain.service.DogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatAndDogServiceImplTest {

    private static final String catsUrl = "http://test.cats.com";
    private static final String dogsUrl = "http://test.dogs.com";

    @InjectMocks
    private CatAndDogServiceImpl sut;

    @Mock
    private CatService catService;

    @Mock
    private DogService dogService;

    @Test
    public void given_a_request_to_get_a_dog_and_a_cat_when_the_request_is_asynchronous_and_both_services_return_with_success_must_return_an_object_containing_the_image_url() {
        // arrange
        when(catService.getCat()).thenReturn(Mono.just(new Cat(catsUrl)));
        when(dogService.getDog()).thenReturn(Mono.just(new Dog(dogsUrl, "success")));

        // act
        final var catAndDog = this.sut.getCatAndDogAsync();

        // assert
        StepVerifier.create(catAndDog.log()).expectNext(new CatAndDogResponse(Status.SUCCESS, catsUrl, dogsUrl)).verifyComplete();
    }

    @Test
    public void given_a_request_to_get_a_dog_and_a_cat_when_the_request_is_asynchronous_and_only_cat_service_returns_with_success_must_return_an_object_containing_only_the_cat_image_url() {
        // arrange
        when(catService.getCat()).thenReturn(Mono.just(new Cat(catsUrl)));
        when(dogService.getDog()).thenReturn(Mono.empty());

        // act
        final var catAndDog = this.sut.getCatAndDogAsync();

        // assert
        System.out.println(catAndDog);
        StepVerifier.create(catAndDog.log()).expectNext(new CatAndDogResponse(Status.SUCCESS, catsUrl, "")).verifyComplete();
    }

    @Test
    public void given_a_request_to_get_a_dog_and_a_cat_when_the_request_is_asynchronous_and_only_dog_service_returns_with_success_must_return_an_object_containing_only_the_dog_image_url() {
        // arrange
        when(catService.getCat()).thenReturn(Mono.empty());
        when(dogService.getDog()).thenReturn(Mono.just(new Dog(dogsUrl, "success")));

        // act
        final var catAndDog = this.sut.getCatAndDogAsync();

        // assert
        System.out.println(catAndDog);
        StepVerifier.create(catAndDog.log()).expectNext(new CatAndDogResponse(Status.SUCCESS, "", dogsUrl)).verifyComplete();
    }

    @Test
    public void given_a_request_to_get_a_dog_and_a_cat_when_the_request_is_synchronous_and_both_services_return_with_success_must_return_an_object_containing_the_image_url() {
        // arrange
        when(catService.getCatSync()).thenReturn(Optional.of(new Cat(catsUrl)));
        when(dogService.getDogSync()).thenReturn(Optional.of(new Dog(dogsUrl, "success")));

        // act
        final var catAndDog = this.sut.getCatAndDogSync();

        // assert
        assertThat(catAndDog).isNotNull();
        assertThat(catAndDog.getCatImageUrl()).isEqualTo(catsUrl);
        assertThat(catAndDog.getDogImageUrl()).isEqualTo(dogsUrl);
        assertThat(catAndDog.getStatus()).isEqualTo(Status.SUCCESS);
    }

    @Test
    public void given_a_request_to_get_a_dog_and_a_cat_when_the_request_is_synchronous_and_only_cat_service_returns_with_success_must_return_an_object_containing_only_the_cat_image_url() {
        // arrange
        when(catService.getCatSync()).thenReturn(Optional.of(new Cat(catsUrl)));
        when(dogService.getDogSync()).thenReturn(Optional.empty());

        // act
        final var catAndDog = this.sut.getCatAndDogSync();

        // assert
        assertThat(catAndDog).isNotNull();
        assertThat(catAndDog.getCatImageUrl()).isEqualTo(catsUrl);
        assertThat(catAndDog.getDogImageUrl()).isEqualTo("");
        assertThat(catAndDog.getStatus()).isEqualTo(Status.SUCCESS);
    }

    @Test
    public void given_a_request_to_get_a_dog_and_a_cat_when_the_request_is_synchronous_and_only_dog_service_returns_with_success_must_return_an_object_containing_only_the_dog_image_url() {
        // arrange
        when(catService.getCatSync()).thenReturn(Optional.empty());
        when(dogService.getDogSync()).thenReturn(Optional.of(new Dog(dogsUrl, "success")));

        // act
        final var catAndDog = this.sut.getCatAndDogSync();

        // assert
        assertThat(catAndDog).isNotNull();
        assertThat(catAndDog.getCatImageUrl()).isEqualTo("");
        assertThat(catAndDog.getDogImageUrl()).isEqualTo(dogsUrl);
        assertThat(catAndDog.getStatus()).isEqualTo(Status.SUCCESS);
    }

}