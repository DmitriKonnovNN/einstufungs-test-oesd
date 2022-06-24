package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Data;

import java.util.List;

@Data
public class ETAufgabenBogenDto {

    private final Long aufgabenBogenId;
    private final List<ETAufgabeDto> aufgabenListe;
}
