package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ETItemDto {


    private Integer itemId;

    //private ETAufgabe aufgabe;

    private Set<String> moeglicheAntworten;


}
