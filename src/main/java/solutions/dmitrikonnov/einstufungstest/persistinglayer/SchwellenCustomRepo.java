package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.cache.annotation.Cacheable;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import java.util.Map;

public interface SchwellenCustomRepo{

    @Cacheable(value = "schwelle-by-niveau")
    Map<ETAufgabenNiveau,Short> findMaximumSchwellenByNiveaus();

}
