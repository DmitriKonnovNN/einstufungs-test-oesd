package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
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
                        entry-> (Boolean)entry.getValue()));
    }

}
