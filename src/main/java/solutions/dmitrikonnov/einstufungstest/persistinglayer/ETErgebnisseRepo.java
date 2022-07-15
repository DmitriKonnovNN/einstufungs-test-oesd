package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETErgebnisse;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Primary
public interface ETErgebnisseRepo extends JpaRepository<ETErgebnisse, UUID>,ETErgebnisseCustomRepo,Pingable {


    List<ETErgebnisse> findAllByCreatedOnBefore(Date createdOn);


    @Query(value = "SELECT eem.et_aufg_id AS id FROM et_ergebnisse_mapping AS eem " +
            "INNER JOIN et_ergebnisse AS ee ON ee.id = eem.et_ergebnisse_id " +
            "WHERE eem.et_aufg_correctness = true AND ee.created_on < : createdOn ", nativeQuery = true)
    List<Integer> findAllIdsOfCorrectAnsweredItems(Date createdOn);

}
