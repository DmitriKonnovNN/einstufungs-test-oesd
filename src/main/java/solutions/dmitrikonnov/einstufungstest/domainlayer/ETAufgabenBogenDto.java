package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ETAufgabenBogenDto implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;
    private final Integer aufgabenBogenId;
    private final List<ETAufgabeDto> aufgabenListe;
}
