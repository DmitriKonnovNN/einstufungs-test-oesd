package solutions.dmitrikonnov.einstufungstest.utils;

import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabeDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETItemDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AufgabenToDTOConverter converts an ETAufgabe which is Entity to a ETAufgabeDTO
 * */

@Service
public class ETAufgabenToDTOConverter {

    public List<ETAufgabeDto> convert(List<ETAufgabe> aufgaben,Integer bogenHash) {
        return aufgaben.stream()
               .map(aufgabe -> convert1(aufgabe,bogenHash))
               .collect(Collectors.toList());

    }
    private  ETAufgabeDto convert1 (ETAufgabe entity,
                                    Integer bogenHash) {

        return ETAufgabeDto.builder()
                .aufgabenStellung(entity.getAufgabenStellung())
                .aufgabenInhalt(entity.getAufgabenInhalt())
                .aufgabenHash(entity.getAufgabeId()+bogenHash)
                .frontEndType(entity.getFrontEndType())
                .niveau(entity.getAufgabenNiveau())
                .items(entity.getItems().stream().map(item-> ETItemDto.builder()
                        .itemId(item.getItemId()+bogenHash)
                        .itemAufgabenInhalt(item.getItemAufgabenInhalt())
                        .moeglicheAntworten(item.getMoeglicheAntworten())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}

