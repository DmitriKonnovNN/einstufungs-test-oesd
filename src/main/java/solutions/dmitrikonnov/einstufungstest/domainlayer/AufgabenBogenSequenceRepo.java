package solutions.dmitrikonnov.einstufungstest.domainlayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AufgabenBogenSequenceRepo extends JpaRepository<AufgabenBogenPersistedIdSequence,Long> {

}
