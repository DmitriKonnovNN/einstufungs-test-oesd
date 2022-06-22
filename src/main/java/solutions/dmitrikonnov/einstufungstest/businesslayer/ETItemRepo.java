package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETItem;

import java.util.List;

public interface ETItemRepo extends JpaRepository<ETItem,Integer> {

}
