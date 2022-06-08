package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class ETAufgabenReshuffler {
    public List<ETAufgabe> reshuffle(Set<ETAufgabe> aufgabenNotReshuffeld) {
        List<ETAufgabe> aufgaben = new ArrayList<>(aufgabenNotReshuffeld);
        Collections.shuffle(aufgaben);
        return aufgaben;
    }
}
