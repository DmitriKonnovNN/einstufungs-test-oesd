package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisse;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisseDto;

@Service
@AllArgsConstructor
@Slf4j
public class ETErgebnisseConverterAndPersister {

    private final ETErgebnisseRepo repo;

    @Async
    public void convertAndPersist(ETErgebnisseDto ergebnisseDto) {
        //TODO: Write converter DTO to Entity
        ETErgebnisse ergebnisse = ETErgebnisse.builder()
                .A1richtig(ergebnisseDto.getA1richtig())
                .A1erreicht(ergebnisseDto.getA1erreicht())
                .A2richtig(ergebnisseDto.getA2richtig())
                .A2erreicht(ergebnisseDto.getA2erreicht())
                .B1richtig(ergebnisseDto.getB1richtig())
                .B1erreicht(ergebnisseDto.getA1erreicht())
                .B2richtig(ergebnisseDto.getB2richtig())
                .B2erreicht(ergebnisseDto.getB2erreicht())
                .C1richtig(ergebnisseDto.getC1richtig())
                .C1erreicht(ergebnisseDto.getC1erreicht())
                .C2richtig(ergebnisseDto.getC2richtig())
                .C2erreicht(ergebnisseDto.getC2erreicht())
                .zahlRichtigerAntworten(ergebnisseDto.getZahlRichtigerAntworten())
                .maxErreichtesNiveau(ergebnisseDto.getMaxErreichtesNiveau())
                .aufgabenBogenHash(ergebnisseDto.getAufgabenBogenHash())
                .idZuRichtigkeitMap(ergebnisseDto.getIdZuRichtigkeitMap())
                .build();
        log.info("ErgebnisseDto {} converted to Entity {}", ergebnisseDto,ergebnisse);
        repo.save(ergebnisse);
        log.info("ErgebnisseEntity {} persisted", ergebnisse);
    }
}
