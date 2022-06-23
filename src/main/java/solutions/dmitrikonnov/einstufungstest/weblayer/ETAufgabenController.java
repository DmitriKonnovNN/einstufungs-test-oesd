package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.*;

@RestController
@RequestMapping("api/v2.0.0/et_ufzgi")
@AllArgsConstructor
public class ETAufgabenController {
    private final ETAufgabenService aufgabenService;
    // TODO: Implement a controller with cache that saves a preset list. Key - AufgabenBogenID or Hash? Evicting policy: LRU + after submitting AntwortBogenDto;

    @GetMapping
    public ResponseEntity<ETAufgabenBogen> getAufgaben (){

        //TODO: refactor so that we return list of AufgabeDTO
        return ResponseEntity.status(HttpStatus.OK).body(aufgabenService.getAufgabenListe());
    }
/*    @PostMapping
    public void addAufgabe (@RequestBody ETAufgabe aufgabe){


    }*/

    @PostMapping()
    public ResponseEntity<ETEndResultForFE> checkAndGetResults(@RequestBody ETAntwortBogenDto bogen){

        //return aufgabenService.checkAntwortBogenAndGetTestErgebnisse(bogen, cachedAufgabenBogen);
        return null;
    }


}