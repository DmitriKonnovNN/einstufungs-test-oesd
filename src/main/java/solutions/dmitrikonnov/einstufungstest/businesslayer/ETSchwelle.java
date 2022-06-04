package solutions.dmitrikonnov.einstufungstest.businesslayer;

import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

public interface ETSchwelle {
    Integer getMindestSchwelle ();
    ETAufgabenNiveau getNiveau();
}
