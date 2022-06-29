package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ETAntwortenReshuffler {
    public List<ETAufgabe> reshuffleAntworten(List<ETAufgabe> aufgabenReshuffeld) {
        aufgabenReshuffeld.stream()
                .map(ETAufgabe::getItems)
                .map(ArrayList::new)
                .forEach(Collections::shuffle);
        return aufgabenReshuffeld;
    }

}
