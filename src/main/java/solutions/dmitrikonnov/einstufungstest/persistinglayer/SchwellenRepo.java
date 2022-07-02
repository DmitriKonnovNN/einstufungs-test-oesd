package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETSchwelle;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface SchwellenRepo extends JpaRepository<ETSchwelle,Integer>,SchwellenCustomRepo {


    List<ETSchwelle> findAllByOrderByNiveau();
    Optional<ETSchwelle> findByNiveau (ETAufgabenNiveau niveau);
    @Transactional
    @Modifying
    @Query("update ETSchwelle e set e.maximumSchwelle = :max, e.mindestSchwelle = :min where e.niveau = :niveau ")
    void updateByNiveau(@Param("niveau")ETAufgabenNiveau niveau,
                                               @Param("min") short min,
                                               @Param("max")short max);

    boolean existsByNiveau(ETAufgabenNiveau niveau);

}
