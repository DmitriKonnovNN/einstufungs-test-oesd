package solutions.dmitrikonnov.einstufungstest.weblayer;

import org.springframework.beans.factory.annotation.Autowired;
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
public class ETAufgabenController {
    private final ETAufgabenService aufgabenService;
    private final InRamSimpleCache cache;
    private final ApplicationEventPublisher publisher;
    private boolean isEnable;

    public ETAufgabenController(@Autowired ETAufgabenService aufgabenService,
                                @Autowired InRamSimpleCache cache,
                                @Autowired ApplicationEventPublisher publisher) {
        this.aufgabenService = aufgabenService;
        this.cache = cache;
        this.publisher = publisher;
        this.isEnable = true;
    }

    @GetMapping
    public ResponseEntity<ETAufgabenBogenDto> getAufgaben (){
        if(isEnable){
            var bogen = cache.getPreparedAufgabenbogen();
            publisher.publishEvent(new AufgabenBogenFetchedFromCache(this));

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ETAufgabenBogenDto(bogen.getAufgabenBogenHash(),
                            bogen.getAufgabenListe() ));}
        else return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

    }

    @PostMapping()
    public ResponseEntity<ETEndResultForFE> checkAndGetResults(@RequestBody ETAntwortBogenDto antwortBogen){

        var cachedBogen = cache.fetch(antwortBogen.getAntwortBogenId());
        var result = aufgabenService.checkAntwortBogenAndGetTestErgebnisse(antwortBogen,cachedBogen);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);

    }

    protected void setEnable(boolean enable) {
        isEnable = enable;
    }
}