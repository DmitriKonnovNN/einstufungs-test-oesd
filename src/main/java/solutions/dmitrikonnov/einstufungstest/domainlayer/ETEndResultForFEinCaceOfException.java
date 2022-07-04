package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Getter;

@Getter
public class ETEndResultForFEinCaceOfException extends ETEndResultForFE{
    private final ETErgebnisseDto ergebnisseDto;
    public ETEndResultForFEinCaceOfException(String id,
                                      ETAufgabenNiveau erreichtesNiveau,
                                      String zahlRichtigerAntworten,
                                      ETErgebnisseDto ergebnisseDto) {
        super(id, erreichtesNiveau, zahlRichtigerAntworten);
        this.ergebnisseDto = ergebnisseDto;
    }
}
