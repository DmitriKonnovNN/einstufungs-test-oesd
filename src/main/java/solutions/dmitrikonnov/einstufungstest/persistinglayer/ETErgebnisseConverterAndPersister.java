package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisse;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisseDto;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ETErgebnisseConverterAndPersister {

    private final ETErgebnisseRepo repo;

    @Async
    @Timed (value = "et.service.ergconverter-persister")
    public Future<String> convertAndPersist(ETErgebnisseDto ergebnisseDto) {

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
        String persistedUUID = repo.save(ergebnisse).getId();
        log.info("ErgebnisseEntity {} persisted", ergebnisse);
        return new AsyncResult<>(persistedUUID);
    }

}
