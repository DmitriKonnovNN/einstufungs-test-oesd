package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.AufgabenBogenSequenceRepo;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.utils.ETAufgabenToDTOConverter;

import java.util.List;

@Service
@AllArgsConstructor
public class ETAufgabenBogenAufsetzer {
    private final ETAufgabenToDTOConverter aufgabeToDtoConverter;
    private final AufgabenBogenSequenceRepo sequenceRepo;

    public ETAufgabenBogen aufsetzen (List<ETAufgabe> aufgaben) {
        final Integer aufgabenBogenHash = aufgaben.hashCode();
        final Long aufgabenBogenId = sequenceRepo.nextVal();
        return ETAufgabenBogen.builder()
                .aufgabenBogenHash(aufgabenBogenHash)
                .aufgabenBogenId(aufgabenBogenId)
                .aufgabenListe(aufgabeToDtoConverter.convert(aufgaben,aufgabenBogenHash,aufgabenBogenId))
                .build();
    }

}
