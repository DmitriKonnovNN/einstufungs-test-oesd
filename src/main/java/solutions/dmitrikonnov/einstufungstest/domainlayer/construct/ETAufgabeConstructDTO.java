package solutions.dmitrikonnov.einstufungstest.domainlayer.construct;

import lombok.Data;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenFrontEndType;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenTyp;

import java.util.Set;

/**
 * this class is suposed to be sent from client to set up the task (ETAufgabe)
 * */

@Data
public class ETAufgabeConstructDTO {

    private String aufgabenStellung;
    private Set<ETItemConstructDTO> items;
    private String aufgabenInhalt;
    private ETAufgabenTyp aufgabenTyp;
    private ETAufgabenNiveau aufgabenNiveau;
    private ETAufgabenFrontEndType frontEndType;
    private Integer gewichtung;

}
