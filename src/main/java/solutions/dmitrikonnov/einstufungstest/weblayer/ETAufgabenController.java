package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAntwortBogenDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogenDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETEndResultForFE;
import solutions.dmitrikonnov.einstufungstest.utils.AufgabenBogenFetchedFromCache;

@RestController
@RequestMapping("api/v2.0.0/et_ufzgi")
@AllArgsConstructor
public class ETAufgabenController {
    private final ETAufgabenService aufgabenService;
    private final InRamSimpleCache cache;
    private final ApplicationEventPublisher publisher;


    @GetMapping
    public ResponseEntity<ETAufgabenBogenDto> getAufgaben (){


        var bogen = cache.getPreparedAufgabenbogen();
        publisher.publishEvent(new AufgabenBogenFetchedFromCache(this));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ETAufgabenBogenDto(bogen.getAufgabenBogenId(),
                        bogen.getAufgabenListe() ));
    }

    @PostMapping()
    public ResponseEntity<ETEndResultForFE> checkAndGetResults(@RequestBody ETAntwortBogenDto antwortBogen){

        var cachedBogen = cache.fetch(antwortBogen.getAntwortBogenId());
        var result = aufgabenService.checkAntwortBogenAndGetTestErgebnisse(antwortBogen,cachedBogen);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);

    }


}