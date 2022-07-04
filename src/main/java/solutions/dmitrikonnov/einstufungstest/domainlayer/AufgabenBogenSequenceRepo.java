package solutions.dmitrikonnov.einstufungstest.domainlayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AufgabenBogenSequenceRepo extends JpaRepository<AufgabenBogenPersistedIdSequence,Long> {


/*

   @Modifying
   @Query(value = "UPDATE et_aufgabenbogen_seq s SET s.next_val=s.next_val+1", nativeQuery = true)
   void nextVal();
   @Query(value = "SELECT s.next_val from et_aufgabenbogen_seq s",nativeQuery = true)
   Long getNextVal();
*/

}
