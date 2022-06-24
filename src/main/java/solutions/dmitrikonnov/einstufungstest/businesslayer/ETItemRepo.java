package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETItem;


public interface ETItemRepo extends JpaRepository<ETItem,Integer> {

    @Modifying
    @Query ("update ETItem i set i.counterCorrectAnswers = i.counterCorrectAnswers+1 where i.itemId = ?1")
    void updateCounterCorrectAnswers(Integer id);
}
