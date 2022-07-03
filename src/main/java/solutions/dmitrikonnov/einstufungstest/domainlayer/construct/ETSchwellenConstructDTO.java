package solutions.dmitrikonnov.einstufungstest.domainlayer.construct;

import lombok.Data;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import javax.validation.constraints.NotNull;

@Data
public class ETSchwellenConstructDTO {
    private Integer id;
    @NotNull(message = "Niveau darf nicht leer sein")
    private ETAufgabenNiveau niveau;
    @NotNull (message = "Es muss eine Mindestschwelle explizit festgelegt werden")
    private Integer mindestSchwelle;
    @NotNull (message = "Es muss eine Maximumschwelle explizit festgelegt werden")
    private Integer maximumSchwelle;
}
