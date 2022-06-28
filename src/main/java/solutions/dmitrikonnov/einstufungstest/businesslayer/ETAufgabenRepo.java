package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.springframework.data.repository.CrudRepository;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import java.util.Set;


public interface ETAufgabenRepo extends CrudRepository<ETAufgabe, Long> {

    Set<ETAufgabe>findAllByAufgabenNiveau(ETAufgabenNiveau aufgabenNiveau);

}
