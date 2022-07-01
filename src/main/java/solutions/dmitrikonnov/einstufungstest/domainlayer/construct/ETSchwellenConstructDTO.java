package solutions.dmitrikonnov.einstufungstest.domainlayer.construct;

import lombok.Data;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

@Data
public class ETSchwellenConstructDTO {
    private ETAufgabenNiveau niveau;
    private Integer mindestSchwelle;
    private Integer maximumSchwelle;
}
