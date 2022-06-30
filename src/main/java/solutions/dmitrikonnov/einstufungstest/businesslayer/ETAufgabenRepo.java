package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.springframework.data.repository.Repository;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import java.util.List;
import java.util.Set;


public interface ETAufgabenRepo extends Repository<ETAufgabe, Long> {

    Set<ETAufgabe>findAllByOrderByAufgabenNiveauAsc();
    List<ETAufgabe> findAllByAufgabenNiveau(ETAufgabenNiveau niveau);
    List<ETAufgabe> findAll();
}
