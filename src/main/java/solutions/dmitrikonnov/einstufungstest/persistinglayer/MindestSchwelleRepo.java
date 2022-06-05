package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.data.repository.CrudRepository;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETMindestschwelle;

import java.util.List;

public interface MindestSchwelleRepo extends CrudRepository<ETMindestschwelle,Integer> {


    List<ETMindestschwelle> findAllOrderByNiveauAsc();
}
