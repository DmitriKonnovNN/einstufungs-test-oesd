package solutions.dmitrikonnov.einstufungstest.businesslayer;

import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import java.util.List;
import java.util.Map;

public interface ETAufgabenRestricter {

    public List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled, Map<ETAufgabenNiveau,Integer> niveauToMax);
    public List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled);
}
