package solutions.dmitrikonnov.einstufungstest.businesslayer;

import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ETAufgabenReshuffler {
    public List<ETAufgabe> reshuffle(Set<ETAufgabe> aufgabenNotReshuffeld) {
        List<ETAufgabe> aufgaben = new ArrayList<>(aufgabenNotReshuffeld);
        Collections.shuffle(aufgaben);
        return aufgaben;
    }
}
