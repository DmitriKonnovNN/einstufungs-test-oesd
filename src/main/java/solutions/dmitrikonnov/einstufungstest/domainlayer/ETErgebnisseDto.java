package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ETErgebnisseDto {

    private  String id;

    private Integer aufgabenBogenHash;



    private int A1richtig = 0;
    private int A2richtig = 0;
    private int B1richtig = 0;
    private int B2richtig = 0;
    private int C1richtig = 0;
    private int C2richtig = 0;


    private Boolean A1erreicht = false;
    private Boolean A2erreicht = false;
    private Boolean B1erreicht = false;
    private Boolean B2erreicht = false;
    private Boolean C1erreicht = false;
    private Boolean C2erreicht = false;

    private ETAufgabenNiveau maxErreichtesNiveau = ETAufgabenNiveau.A0;
    private Integer zahlRichtigerAntworten = 0;
    private List<ETAufgabenNiveau> RichtigeLoesungenNachNiveau;
    private Map<Integer, Boolean> idZuRichtigkeitMap = new HashMap<>();
    private Map<ETAufgabenNiveau, Integer> niveauZurZahlRichtiger = new HashMap<>();



}
