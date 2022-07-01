package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class ETErgebnisseCustomRepoImpl implements ETErgebnisseCustomRepo{


    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Map<Integer, Boolean> findAllMapsItemCorrectness(@Param("createdOn") Date createdOn) {


        return  entityManager.createQuery("select entry(maps) " +
                        " from ETErgebnisse e left join fetch e.idZuRichtigkeitMap maps WHERE e.createdOn<:createdOn", Map.Entry.class)
                .getResultStream()
                .collect(Collectors.toMap(
                        entry -> (Integer)entry.getKey(),
                        entry-> (Boolean)entry.getValue()
                ));

    }
}
