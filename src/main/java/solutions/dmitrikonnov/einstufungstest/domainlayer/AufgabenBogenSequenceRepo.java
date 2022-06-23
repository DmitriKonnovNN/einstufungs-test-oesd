package solutions.dmitrikonnov.einstufungstest.domainlayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AufgabenBogenSequenceRepo extends JpaRepository<Long,Long> {


    //TODO: test all the queries down below otherwise look for another solution
    // select nextval('et_aufgabe_seq') ?? or maybe select next_val from et_aufgabe_seq ?
    @Query(value = "SELECT nextval from et_aufgabenbogen_seq", nativeQuery = true)
    Long nextVal();
}
