package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO only to be sent to FrontEnd
 * */
@Data
public class ETAufgabeDto implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;
    private final Long aufgabenBogenId;
    private final Integer aufgabenBogenHash;
    private Integer aufgabenHash; // AufgabenId*AufgabenBogenId
    private String aufgabenStellung;
    private String aufgabenInhalt;
    private List<String> moeglicheAntworten;
}
