package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AufgabenBogenCacheQualifierResolver {

    private final ApplicationContext context;

    public AufgabenBogenCache resolve(String qualifier) {
        return (AufgabenBogenCache) context.getBean(qualifier);
    }
}
