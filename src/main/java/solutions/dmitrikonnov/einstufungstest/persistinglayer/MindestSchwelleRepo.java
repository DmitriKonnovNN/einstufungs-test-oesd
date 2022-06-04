package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.data.repository.CrudRepository;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETMindestschwelle;

public interface MindestSchwelleRepo extends CrudRepository<ETMindestschwelle,Integer> {


    Iterable<ETMindestschwelle> findAllOrderByNiveauAsc();
}
