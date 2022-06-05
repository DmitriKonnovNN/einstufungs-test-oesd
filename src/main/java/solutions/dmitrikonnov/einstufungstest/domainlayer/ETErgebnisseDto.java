package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ETErgebnisseDto {

    private  String id;
    private Integer aufgabenBogenHash;
    private ETAufgabenNiveau maxErreichtesNiveau = ETAufgabenNiveau.A0;
    private Integer zahlRichtigerAntworten = 0;
    private List<ETAufgabenNiveau> RichtigeLoesungenNachNiveau;
    private Map<Integer, Boolean> idZuRichtigkeitMap = new HashMap<>();
    private Map<ETAufgabenNiveau, Integer> niveauZurZahlRichtiger = new HashMap<>();

}
