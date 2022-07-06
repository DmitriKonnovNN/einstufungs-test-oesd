package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import java.util.Date;
import java.util.Map;


public interface ETErgebnisseCustomRepo {
    Map<Integer,Boolean> findAllMapsItemCorrectness(Date createdOn);

}
