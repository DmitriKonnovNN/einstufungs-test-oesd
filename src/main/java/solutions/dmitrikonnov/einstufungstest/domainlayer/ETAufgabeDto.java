package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO only to be sent to FrontEnd
 * */
@Data
@Builder
public class ETAufgabeDto implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;
    // private final Integer aufgabenBogenHash;
    private Integer aufgabenHash; // AufgabeId AufgabenBogenId
    private String aufgabenStellung;
    private String aufgabenInhalt;
    private ETAufgabenNiveau niveau;
    private List<ETItemDto> items;
    private ETAufgabenFrontEndType frontEndType;
}
