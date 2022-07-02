package solutions.dmitrikonnov.einstufungstest.domainlayer.construct;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Data
public class ETItemConstructDTO {

    private Integer aufgabeId;
    private String itemAufgabenInhalt;
    @NotBlank
    private Set<String> moeglicheAntworten;
    private List<String> loesungen;

}
