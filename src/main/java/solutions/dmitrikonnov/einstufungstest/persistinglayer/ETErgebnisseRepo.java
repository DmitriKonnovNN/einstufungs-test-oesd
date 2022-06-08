package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisse;

import java.util.List;
import java.util.UUID;

public interface ETErgebnisseRepo extends JpaRepository<ETErgebnisse, UUID> {


}
