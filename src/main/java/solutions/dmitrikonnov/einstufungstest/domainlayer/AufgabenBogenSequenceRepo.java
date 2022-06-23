package solutions.dmitrikonnov.einstufungstest.domainlayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AufgabenBogenSequenceRepo extends JpaRepository<Long,Long> {

    @Query(value = "SELECT nextval from et_aufgabenbogen_seq", nativeQuery = true)
    Long nextVal();
}
