package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Der Bogen wird einmal im BackEnd nach dem Reshuffle aufgesetzt. Hiervon werden einzelne Aufgaben abgerufen.
 * Die Reihenfolge der Aufgaben soll während der Session beibehalten werden. Dazu muss ein Cache implementiert sein.
 * Die Id wird dem Nutzer zuteil und abschließend in den Antwortbogen eingetragen.
 * */

@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ETAufgabenBogen {

    private final Integer aufgabenBogenHash;
    private final List<ETAufgabeDto> aufgabenListe;
    private long cachedAt;
    private final Map<Integer, List<String>> itemZuLoesungen;
    private final Map<Integer,ETAufgabenNiveau>itemZuNiveau;

}
