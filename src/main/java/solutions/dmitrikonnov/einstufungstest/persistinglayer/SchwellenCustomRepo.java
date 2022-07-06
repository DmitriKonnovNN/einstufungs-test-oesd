package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import java.util.Map;

public interface SchwellenCustomRepo{

    Map<ETAufgabenNiveau,Short> findMaximumSchwellenByNiveaus();

}
