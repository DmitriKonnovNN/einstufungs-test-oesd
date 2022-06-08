package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;

import java.util.List;

@Service
public class ETAufgabenRestricter {


    public List<ETAufgabe> restrict(List<ETAufgabe> aufgabenReshuffeld) {
        return aufgabenReshuffeld;
    }

}
