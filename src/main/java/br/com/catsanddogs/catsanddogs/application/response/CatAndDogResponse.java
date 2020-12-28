package br.com.catsanddogs.catsanddogs.application.response;

import br.com.catsanddogs.catsanddogs.application.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CatAndDogResponse {
    private Status status;
    private String catImageUrl;
    private String dogImageUrl;
}
