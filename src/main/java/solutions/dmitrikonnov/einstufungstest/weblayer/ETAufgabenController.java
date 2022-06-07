package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAntwortBogenDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabeDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETEndResultForFE;

@RestController
@RequestMapping("api/v2.0.0/et_ufzgi")
@AllArgsConstructor
public class ETAufgabenController {
    private final ETAufgabenService aufgabenService;
    // TODO: Implement a controller with cache that saves a preset list. Key - AufgabenBogenID or Hash? Evicting policy: LRU + after submitting AntwortBogenDto;

    @GetMapping
    public ResponseEntity<ETAufgabeDto> getAufgaben (){

        //return ResponseEntity.ok().cacheControl().
        return null;
    }

    @PostMapping()
    public ResponseEntity<ETEndResultForFE> checkAndGetResults(@RequestBody ETAntwortBogenDto bogen){

        //return aufgabenService.checkAntwortBogenAndGetTestErgebnisse(bogen, cachedAufgabenBogen);
        return null;
    }
}