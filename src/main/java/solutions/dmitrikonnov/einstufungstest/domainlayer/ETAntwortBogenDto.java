package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Data;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class should be used as DTO only
 * */
@Data
public class ETAntwortBogenDto {

    private Long antwortBogenId;
    private Map<Integer, ArrayList<String>> aufgabenHashZuAntwortMap;

}
