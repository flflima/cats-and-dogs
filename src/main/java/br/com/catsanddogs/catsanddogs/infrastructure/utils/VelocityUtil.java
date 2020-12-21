package br.com.catsanddogs.catsanddogs.infrastructure.utils;

import br.com.catsanddogs.catsanddogs.domain.model.Cat;
import br.com.catsanddogs.catsanddogs.domain.model.Dog;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.function.BiFunction;

@Component
@Slf4j
public class VelocityUtil {

    private static VelocityEngine engine;

    @Autowired
    public VelocityUtil(final VelocityEngine velocityEngine) {
        VelocityUtil.engine = velocityEngine;
    }

    public static BiFunction<Cat, Dog, String> loadCatAndDogTemplate() {
        return (cat, dog) -> {
            final var template = engine.getTemplate("templates/principal.html");
            final var context = new VelocityContext();
            context.put("catUrl", cat.getFile());
            context.put("dogUrl", dog.getMessage());

            final var writer = new StringWriter();
            template.merge(context, writer);

            return writer.toString();
        };
    }
}
