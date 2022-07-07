package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.cache.annotation.Cacheable;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import java.util.Map;

public interface SchwellenCustomRepo{

    @Cacheable(value = "schwellen", unless = "#a0=='Foundation'")
    Map<ETAufgabenNiveau,Short> findMaximumSchwellenByNiveaus();

}
