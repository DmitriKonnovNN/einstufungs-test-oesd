package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
public class ETAufgabenBogen {

    @Id
    @SequenceGenerator(name = "et_aufgabenbogen_seq",
            sequenceName = "et_aufgabenbogen_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "et_aufgabenbogen_seq")
    private final Long aufgabenBogenId;
    private final Integer aufgabenBogenHash;
    private final List<ETAufgabeDto> aufgabenListe;
    private long cachedAt;
    private final Map<Integer, List<String>> itemZuLoesungen;
    private final Map<Integer,ETAufgabenNiveau>itemZuNiveau;


}
