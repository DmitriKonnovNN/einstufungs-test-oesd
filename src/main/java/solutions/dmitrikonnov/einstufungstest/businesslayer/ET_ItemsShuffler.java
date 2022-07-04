package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETItem;

import java.util.*;

@Service
public class ET_ItemsShuffler {
    public List<ETAufgabe> reshuffleItems(List<ETAufgabe> aufgabenReshuffeld) {
        // TODO: refactor so the items are really sorted
        aufgabenReshuffeld.stream()
                .map(ETAufgabe::getItems)
                .map(ArrayList::new).forEach(this::schuffleItems);

        return aufgabenReshuffeld;
    }

    private Set<ETItem> schuffleItems (List<ETItem> items){
        Collections.shuffle(items);
        return new HashSet<>(items);
    }

}
