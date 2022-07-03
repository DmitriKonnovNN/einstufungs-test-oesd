package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;

import java.util.List;
import java.util.Set;


public interface ETAufgabenRepo extends JpaRepository<ETAufgabe, Integer> {

    Set<ETAufgabe> findAllByOrderByAufgabenNiveauAsc();
    List<ETAufgabe> findAllByAufgabenNiveau(ETAufgabenNiveau niveau);
    List<ETAufgabe> findAll();
}
