package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.Map;
import java.util.stream.Collectors;

public class SchwellenCustomRepoImpl implements SchwellenCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Map<ETAufgabenNiveau, Integer> findMaximumSchwellenByNiveaus() {
        return  entityManager
                .createQuery("select ETMindestschwelle.niveau AS niveau," +
                        " ETMindestschwelle.maximumSchwelle as maxschwelle " +
                        " from ETMindestschwelle", Tuple.class)
                .getResultStream()
                .collect(Collectors.toMap(
                        tuple -> (ETAufgabenNiveau) tuple.get("niveau"),
                        tuple -> (Integer) tuple.get("maxschwelle")
                ));
    }
}
