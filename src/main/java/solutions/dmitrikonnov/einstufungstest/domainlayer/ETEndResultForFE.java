package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ETEndResultForFE {
    private final String id;

    private final ETAufgabenNiveau erreichtesNiveau;

    private final String zahlRichtigerAntworten;
}
