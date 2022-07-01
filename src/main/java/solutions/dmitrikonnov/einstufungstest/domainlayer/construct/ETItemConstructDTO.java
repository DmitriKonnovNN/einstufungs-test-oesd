package solutions.dmitrikonnov.einstufungstest.domainlayer.construct;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ETItemConstructDTO {

    private String itemAufgabenInhalt;
    private Set<String> moeglicheAntworten;
    private List<String> loesungen;

}
