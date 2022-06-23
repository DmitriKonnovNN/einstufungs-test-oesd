package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAntwortBogenDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETEndResultForFE;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("api/v2.0.0/et_ufzgi")
@AllArgsConstructor
public class ETAufgabenController {
    private final ETAufgabenService aufgabenService;
    // TODO: Implement a controller with cache that saves a preset list. Key - AufgabenBogenID or Hash? Evicting policy: LRU + after submitting AntwortBogenDto;
    private final Map<Long, ETAufgabenBogen> simpleCache = new ConcurrentHashMap<>();
    @GetMapping
    public ResponseEntity<ETAufgabenBogen> getAufgaben (){
        var bogen = aufgabenService.getAufgabenListe();
        simpleCache.put(bogen.getAufgabenBogenId(), bogen);

        return ResponseEntity.status(HttpStatus.OK).body(bogen);
    }
/*    @PostMapping
    public void addAufgabe (@RequestBody ETAufgabe aufgabe){


    }*/

    @PostMapping()
    public ResponseEntity<ETEndResultForFE> checkAndGetResults(@RequestBody ETAntwortBogenDto antwortBogen){

        var cachedBogen = simpleCache.get(antwortBogen.getAntwortBogenId());
        //return aufgabenService.checkAntwortBogenAndGetTestErgebnisse(bogen, cachedAufgabenBogen);

        return ResponseEntity.status(HttpStatus.OK)
                .body(aufgabenService.checkAntwortBogenAndGetTestErgebnisse(antwortBogen,cachedBogen));

    }


}