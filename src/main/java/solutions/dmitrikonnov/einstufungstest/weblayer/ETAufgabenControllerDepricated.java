package solutions.dmitrikonnov.einstufungstest.weblayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAntwortBogenDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogenDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETEndResultForFE;
import solutions.dmitrikonnov.einstufungstest.exceptions.TimeForTestExpiredException;
import solutions.dmitrikonnov.einstufungstest.utils.AufgabenBogenFetchedFromCache;

import java.util.concurrent.TimeUnit;

/**
 * Controller with SIMPLE CACHE*/

@RestController
@Deprecated
@RequestMapping("api/v1.9.9/et_ufzgi")
public class ETAufgabenControllerDepricated implements SwitchableController {
    private final long TIME_FOR_TEST_Millis;
    private final ETAufgabenService aufgabenService;
    private final AufgabenBogenCache cache;
    private final ApplicationEventPublisher publisher;
    private final ApplicationContext context;

    private boolean isEnable;

    public ETAufgabenControllerDepricated(@Autowired ETAufgabenService aufgabenService,
                                          @Value("${app.cache.toCheckCache.config.inRam}")  String qualifier,
                                          @Autowired ApplicationEventPublisher publisher,
                                          @Value("${app.controller.timeForTest}") String time,
                                          @Autowired ApplicationContext context) {
        this.context = context;
        this.TIME_FOR_TEST_Millis = TimeUnit.MINUTES.toMillis(Long.valueOf(time));
        this.aufgabenService = aufgabenService;
        this.cache = (AufgabenBogenCache)context.getBean(qualifier);
        this.publisher = publisher;
        this.isEnable = true;
    }

    @GetMapping
    public ResponseEntity<ETAufgabenBogenDto> getAufgaben (){
        if(isEnable){
            var bogen = cache.getPreparedAufgabenbogen();
            publisher.publishEvent(new AufgabenBogenFetchedFromCache(this));

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ETAufgabenBogenDto(
                            bogen.getAufgabenBogenHash(),
                            bogen.getAufgabenListe(),
                            System.currentTimeMillis()));}
        else return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

    }

    @PostMapping()
    public ResponseEntity<ETEndResultForFE> checkAndGetResults(@RequestBody ETAntwortBogenDto antwortBogen){
        if(System.currentTimeMillis()-antwortBogen.getCreatedAt()> TIME_FOR_TEST_Millis) {
            throw new TimeForTestExpiredException("Zeit für den Test ist um.");
        }
        var cachedBogen = cache.fetch(antwortBogen.getAntwortBogenId());
        var result = aufgabenService.checkAntwortBogenAndGetTestErgebnisse(antwortBogen,cachedBogen);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}