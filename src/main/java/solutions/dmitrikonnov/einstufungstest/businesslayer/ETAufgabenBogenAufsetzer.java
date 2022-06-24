package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.AufgabenBogenSequenceRepo;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.utils.ETAufgabenToDTOConverter;

import java.util.*;

@Service
@AllArgsConstructor
public class ETAufgabenBogenAufsetzer {
    private final ETAufgabenToDTOConverter aufgabeToDtoConverter;
    private final AufgabenBogenSequenceRepo sequenceRepo;

    public ETAufgabenBogen aufsetzen (List<ETAufgabe> aufgaben) {
        final Integer aufgabenBogenHash = aufgaben.hashCode();
        final Long aufgabenBogenId = sequenceRepo.nextVal();

        return ETAufgabenBogen.builder()
                .aufgabenBogenHash(aufgabenBogenHash)
                .aufgabenBogenId(aufgabenBogenId)
                .aufgabenListe(aufgabeToDtoConverter.convert(aufgaben,aufgabenBogenHash,aufgabenBogenId))
                .itemZuLoesungen(extractItems(aufgaben))
                .itemZuNiveau(extractNiveaus(aufgaben))
                .build();
    }

    private Map<Integer, List<String>> extractItems (List<ETAufgabe> aufgaben) {
        Map<Integer, List<String>> itemIdZuLoesungen = new HashMap<>();

        for (ETAufgabe aufgabe: aufgaben) {
            aufgabe.getItems().forEach(item -> {
                itemIdZuLoesungen.put(item.getItemId(), item.getLoesungen());

            });
        }
        return itemIdZuLoesungen;
    }

    private Map<Integer, ETAufgabenNiveau> extractNiveaus (List<ETAufgabe> aufgaben){

        Map<Integer,ETAufgabenNiveau> itemIdZuNiveau = new HashMap<>();
        for (ETAufgabe aufgabe: aufgaben) {
            ETAufgabenNiveau niveau = aufgabe.getAufgabenNiveau();
            aufgabe.getItems().forEach(item -> {
                itemIdZuNiveau.put(item.getItemId(), niveau);
            });
        }
        return itemIdZuNiveau;
    }

}
