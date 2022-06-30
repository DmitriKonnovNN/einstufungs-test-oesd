package solutions.dmitrikonnov.einstufungstest.businesslayer;

import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

public interface ETSchwelle {
    Number getMindestSchwelle ();
    ETAufgabenNiveau getNiveau();
}
