package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class ETErgebnisseCustomRepoImpl implements ETErgebnisseCustomRepo{


    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Map<Integer, Boolean> findAllMapsItemCorrectness(Date createdOn) {

        return  entityManager
                .createQuery("select map.etergebnisse_id as id, map.et_aufg_correctness as correct " +
                        " from ETErgebnisse.idZuRichtigkeitMap map WHERE  ETErgebnisse.createdOn<?1", Tuple.class)
                .getResultStream()
                .collect(Collectors.toMap(
                        tuple -> (Integer) tuple.get("id"),
                        tuple -> (Boolean) tuple.get("correct")
                ));

    }
}
