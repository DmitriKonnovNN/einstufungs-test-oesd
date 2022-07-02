package solutions.dmitrikonnov.einstufungstest.exceptions;

import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

public class ThresholdNotFoundException extends RuntimeException {
    private final static String SCHWELLE_NOT_FOUND_MSG =
            "Threshold for level %s not found!\n" +
            "Keine Schwelle fürs Niveau %s gefunden!";

    public ThresholdNotFoundException(ETAufgabenNiveau level) {
        super(String.format(SCHWELLE_NOT_FOUND_MSG,level.toString(),level.toString()));
    }
}
