package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETMindestschwelle;

import java.util.List;

public interface SchwellenRepo extends JpaRepository<ETMindestschwelle,Integer>,SchwellenCustomRepo {


    List<ETMindestschwelle> findAllByOrderByNiveau();


}
