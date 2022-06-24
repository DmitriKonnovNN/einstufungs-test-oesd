package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class should be used as DTO only
 * */
@Data
public class ETAntwortBogenDto implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;
    private final Long antwortBogenId;
    private final Map<Integer, ArrayList<String>> itemHashZuAntwortMap;
}
