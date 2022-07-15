package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETSchwelle;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface SchwellenRepo extends JpaRepository<ETSchwelle,Integer>,SchwellenCustomRepo {


    @Cacheable (value = "schwelle")
    List<ETSchwelle> findAllByOrderByNiveau();

    @Cacheable (value = "schwelle",key="#niveau", unless = "#a0=='zero-arg-not-cached'")
    Optional<ETSchwelle> findByNiveau (ETAufgabenNiveau niveau);

    @CachePut (value = "schwelle",key="#niveau")
    @Modifying
    @Query("update ETSchwelle e set e.maximumSchwelle = :max, e.mindestSchwelle = :min where e.niveau = :niveau ")
    void updateByNiveau(@Param("niveau")ETAufgabenNiveau niveau,
                                               @Param("min") short min,
                                               @Param("max")short max);

    @Cacheable (value = "schwelle", key="#niveau", unless = "#a0=='zero-arg-not-cached'")
    boolean existsByNiveau(ETAufgabenNiveau niveau);

}
