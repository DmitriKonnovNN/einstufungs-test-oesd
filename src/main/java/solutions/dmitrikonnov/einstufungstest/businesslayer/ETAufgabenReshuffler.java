package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;

import java.util.Collections;
import java.util.List;

@Service
public class ETAufgabenReshuffler {
    public List<ETAufgabe> reshuffle(List<ETAufgabe> aufgabenAsIs) {
        Collections.shuffle(aufgabenAsIs);
        return aufgabenAsIs;
    }
}
