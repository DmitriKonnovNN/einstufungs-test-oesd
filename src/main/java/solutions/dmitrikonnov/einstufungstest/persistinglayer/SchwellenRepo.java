package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETMindestschwelle;

import java.util.List;
import java.util.Optional;

public interface SchwellenRepo extends JpaRepository<ETMindestschwelle,Integer>,SchwellenCustomRepo {


    List<ETMindestschwelle> findAllByOrderByNiveau();
    Optional<ETMindestschwelle> findByNiveau (ETAufgabenNiveau niveau);


}
