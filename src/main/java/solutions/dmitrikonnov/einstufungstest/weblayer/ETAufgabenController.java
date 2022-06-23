package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAntwortBogenDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETEndResultForFE;
import solutions.dmitrikonnov.einstufungstest.utils.AufgabenBogenFetchedFromCache;

@RestController
@RequestMapping("api/v2.0.0/et_ufzgi")
@AllArgsConstructor
public class ETAufgabenController {
    private final ETAufgabenService aufgabenService;
    private final InRamSimpleCache simpleCache;
    private final ApplicationEventPublisher publisher;


    @GetMapping
    public ResponseEntity<ETAufgabenBogen> getAufgaben (){

        var bogen = simpleCache.getPreparetedAufgabeBogen();
        publisher.publishEvent(new AufgabenBogenFetchedFromCache(this));
        return ResponseEntity.status(HttpStatus.OK).body(bogen);
    }
/*    @PostMapping
    public void addAufgabe (@RequestBody ETAufgabe aufgabe){


    }*/

    @PostMapping()
    public ResponseEntity<ETEndResultForFE> checkAndGetResults(@RequestBody ETAntwortBogenDto antwortBogen){

        var cachedBogen = simpleCache.fetch(antwortBogen.getAntwortBogenId());
        var result = aufgabenService.checkAntwortBogenAndGetTestErgebnisse(antwortBogen,cachedBogen);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);

    }


}