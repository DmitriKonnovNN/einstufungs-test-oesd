package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisse;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ETErgebnisseRepo extends JpaRepository<ETErgebnisse, UUID> {

    List<ETErgebnisse> findAllByCreatedOnBefore(Date createdOn);


    @Query("select ETErgebnisse.idZuRichtigkeitMap as maps from ETErgebnisse where ETErgebnisse.createdOn<?1")
    Map<Integer,Boolean> findAllMapsItemCorrectness(Date createdOn);

    @Query(value="SELECT eem.et_aufg_id AS id FROM et_ergebnisse_mapping AS eem " +
            "INNER JOIN et_ergebnisse AS ee ON ee.id = eem.etergebnisse_id " +
            "WHERE eem.et_aufg_correctness = true AND ee.created_on < : createdOn ", nativeQuery = true)
    List <Integer> findAllIdsOfCorrectAnsweredItems(Date createdOn);


}
