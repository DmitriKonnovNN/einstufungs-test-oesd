package solutions.dmitrikonnov.einstufungstest.domainlayer.construct;

import lombok.Data;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenFrontEndType;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenTyp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * this class is suposed to be sent from client to set up the task (ETAufgabe)
 * */

@Data
public class ETAufgabeConstructDTO implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;


    @NotBlank
    private String aufgabenStellung;
    @NotNull
    private List<ETItemConstructDTO> items;

    private String aufgabenInhalt;

    private ETAufgabenTyp aufgabenTyp;
    @NotNull
    private ETAufgabenNiveau aufgabenNiveau;

    private ETAufgabenFrontEndType frontEndType;

    private Integer gewichtung;

}
