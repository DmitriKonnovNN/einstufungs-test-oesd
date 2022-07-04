package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Getter;

@Getter
public class ETEndResultForFEinCaceOfException extends ETEndResultForFE{
    private final ETErgebnisseDto ergebnisseDto;
    private final String EXCEPTION_MSG =
            "Please, save the whole result report!" +
            "\n Bitte, behalten Sie den ganzen Ergebnissebericht";

    public ETEndResultForFEinCaceOfException(String id,
                                      ETAufgabenNiveau erreichtesNiveau,
                                      String zahlRichtigerAntworten,
                                      ETErgebnisseDto ergebnisseDto) {
        super(id, erreichtesNiveau, zahlRichtigerAntworten);
        this.ergebnisseDto = ergebnisseDto;
    }
}
