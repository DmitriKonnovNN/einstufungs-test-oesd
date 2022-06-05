package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisse;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisseDto;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ETErgebnisseConverterAndPersister {

    private final ETErgebnisseRepo repo;

    @Async
    public void convertAndPersist(ETErgebnisseDto ergebnisseDto) {
        //TODO: Write converter DTO to Entity
        ETErgebnisse ergebnisse = ETErgebnisse.builder()
                .aufgabenBogenHash(ergebnisseDto.getAufgabenBogenHash())
                .richtigeLoesungenNachNiveau(ergebnisseDto.getRichtigeLoesungenNachNiveau()
                        .stream().map(ETAufgabenNiveau::toString)
                        .collect(Collectors.toList()))
                .zahlRichtigerAntworten(ergebnisseDto.getZahlRichtigerAntworten())
                .maxErreichtesNiveau(ergebnisseDto.getMaxErreichtesNiveau())
                .niveauZurZahlRichtiger(ergebnisseDto.getNiveauZurZahlRichtiger().entrySet()
                        .stream().collect(Collectors.toMap(entry->entry.getKey().toString(), Map.Entry::getValue)))
                .idZuRichtigkeitMap(ergebnisseDto.getIdZuRichtigkeitMap())
                .build();
        log.info("ErgebnisseDto {} converted to Entity {}", ergebnisseDto,ergebnisse);
        repo.save(ergebnisse);
        log.info("ErgebnisseEntity {} persisted", ergebnisse);
    }

}
