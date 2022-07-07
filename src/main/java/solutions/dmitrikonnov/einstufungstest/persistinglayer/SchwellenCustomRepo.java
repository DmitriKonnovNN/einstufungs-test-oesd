package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.cache.annotation.Cacheable;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import java.util.Map;

public interface SchwellenCustomRepo{

    @Cacheable(value = "schwelleByNiveau")
    Map<ETAufgabenNiveau,Short> findMaximumSchwellenByNiveaus();

}
