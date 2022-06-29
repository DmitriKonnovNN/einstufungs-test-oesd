package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.extern.slf4j.Slf4j;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import java.util.List;
import java.util.Map;

@Slf4j
public class ETAufgabenRestricterSimple implements ETAufgabenRestricter {
    private static final int aufgabenzahlProNiveau = 3;

    @Override
    public List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled, Map<ETAufgabenNiveau, Integer> niveauToMax) {
        log.error("Method is not implemented! No restriction occured!");
        try {
            throw new RuntimeException("List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled, Map<ETAufgabenNiveau, Integer> niveauToMax) ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedAndReshuffeled;
    }

    @Override
    public List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled) {
        selectedAndReshuffeled.subList(aufgabenzahlProNiveau,selectedAndReshuffeled.size()).clear();
        return selectedAndReshuffeled;
    }

}


