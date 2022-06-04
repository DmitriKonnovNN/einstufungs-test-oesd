package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Data;

import java.util.List;

/**
 * DTO only to be sent to FrontEnd
 * */
@Data
public class ETAufgabeDto {

    private final Long aufgabenBogenId;
    private final Integer aufgabenBogenHash;
    private Integer aufgabenHash; // AufgabenId*AufgabenBogenId
    private String aufgabenStellung;
    private String aufgabenInhalt;
    private List<String> moeglicheAntworten;
}
