package solutions.dmitrikonnov.einstufungstest.domainlayer.construct;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ETItemConstructDTO {

    @NotBlank
    private String itemAufgabenInhalt;
    @NotEmpty
    private List<String> moeglicheAntworten;
    @NotEmpty
    private List<String> loesungen;

}
