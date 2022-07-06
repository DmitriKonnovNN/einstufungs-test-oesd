package solutions.dmitrikonnov.einstufungstest.persistinglayer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Transactional(readOnly = true)
@Slf4j
public class PingableImpl<T> implements Pingable <T>{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public int ping(Class<T> clazz) {

        var name = clazz.getName();

        log.debug(clazz.toString());
        log.debug(name);
        String queryString = String.format("SELECT 1 FROM %s",name );
        TypedQuery<Integer> query = entityManager.createQuery(queryString,Integer.class);
        return query.getSingleResult();
    }


}
