package br.com.catsanddogs.catsanddogs.application.response;

import br.com.catsanddogs.catsanddogs.application.enums.Status;
import br.com.catsanddogs.catsanddogs.domain.model.Cat;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CatResponse {
    private Status status;
    private Cat data;
}
