package br.com.catsanddogs.catsanddogs.infrastructure.service;

import br.com.catsanddogs.catsanddogs.application.properties.ApiProperties;
import br.com.catsanddogs.catsanddogs.domain.model.Dog;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DogApiServiceTest {

    private static final String dogsUrl = "http://test.dogs.com";

    @InjectMocks
    private DogApiService sut;

    @Mock
    private OkHttpClient client;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private ApiProperties apiProperties;

    @BeforeEach
    void init() {
        when(this.apiProperties.getDogs()).thenReturn(dogsUrl);
    }

    @Test
    public void given_a_request_to_dog_service_when_an_image_is_requested_asynchronously_with_success_must_return_an_object_containing_the_image_url() throws IOException {
        // arrange
        final var remoteCall = mock(Call.class);
        final var fileName = "json/valid-dog.json";
        final var classLoader = getClass().getClassLoader();
        final var resource = classLoader.getResource(fileName);
        final var file = new File(resource.getFile());
        final var content = new String(Files.readAllBytes(file.toPath()));

        when(remoteCall.execute()).thenReturn(buildSuccessfulResponseWith200StatusCode(content));
        when(this.client.newCall(any())).thenReturn(remoteCall);

        // act
        final var dog = this.sut.getDog();

        // assert
        StepVerifier.create(dog.log()).expectNext(new Dog(dogsUrl, "success")).verifyComplete();
    }

    @Test
    public void given_a_request_to_dog_service_when_an_image_is_requested_asynchronously_with_error_must_return_an_object_containing_the_image_url() throws IOException {
        // arrange
        final var remoteCall = mock(Call.class);
        final var fileName = "json/valid-dog.json";
        final var classLoader = getClass().getClassLoader();
        final var resource = classLoader.getResource(fileName);
        final var file = new File(resource.getFile());
        final var content = new String(Files.readAllBytes(file.toPath()));

        when(remoteCall.execute()).thenReturn(buildErrorResponseWith400StatusCode(content));
        when(this.client.newCall(any())).thenReturn(remoteCall);

        // act
        final var dog = this.sut.getDog();

        // assert
        StepVerifier.create(dog.log()).verifyComplete();
    }

    @Test
    public void given_a_request_to_dog_service_when_an_image_is_requested_synchronously_with_success_must_return_an_object_containing_the_image_url() throws IOException {
        // arrange
        final var remoteCall = mock(Call.class);
        final var fileName = "json/valid-dog.json";
        final var classLoader = getClass().getClassLoader();
        final var resource = classLoader.getResource(fileName);
        final var file = new File(resource.getFile());
        final var content = new String(Files.readAllBytes(file.toPath()));

        when(remoteCall.execute()).thenReturn(buildSuccessfulResponseWith200StatusCode(content));
        when(this.client.newCall(any())).thenReturn(remoteCall);

        // act
        final var dog = this.sut.getDogSync();

        // assert
        assertThat(dog).isNotNull();
        assertThat(dog.get()).isNotNull();
        assertThat(dog.get().getMessage()).isEqualTo(dogsUrl);
    }

    @Test
    public void given_a_request_to_dog_service_when_an_image_is_requested_synchronously_with_error_must_return_an_object_containing_the_image_url() throws IOException {
        // arrange
        final var remoteCall = mock(Call.class);
        final var fileName = "json/valid-dog.json";
        final var classLoader = getClass().getClassLoader();
        final var resource = classLoader.getResource(fileName);
        final var file = new File(resource.getFile());
        final var content = new String(Files.readAllBytes(file.toPath()));

        when(remoteCall.execute()).thenReturn(buildErrorResponseWith400StatusCode(content));
        when(this.client.newCall(any())).thenReturn(remoteCall);

        // act
        final var dog = this.sut.getDogSync();

        // assert
        assertThat(dog).isNotNull();
        assertThat(dog).isEqualTo(Optional.empty());
    }

    private Response buildSuccessfulResponseWith200StatusCode(final String serializedBody) {
        return new Response.Builder()
                .request(new Request.Builder().url(dogsUrl).build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("Success")
                .body(ResponseBody.create(
                        serializedBody,
                        MediaType.parse("application/json")
                ))
                .build();
    }

    private Response buildErrorResponseWith400StatusCode(final String serializedBody) {
        return new Response.Builder()
                .request(new Request.Builder().url(dogsUrl).build())
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .message("Error")
                .body(ResponseBody.create(
                        serializedBody,
                        MediaType.parse("application/json")
                ))
                .build();
    }
}