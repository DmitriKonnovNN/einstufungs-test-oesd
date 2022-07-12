package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETErgebnisse;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.ETErgebnisseRepo;

@AllArgsConstructor
@Service
@Slf4j
public class ETControllerSwitcher {
    ETErgebnisseRepo ergebnisseRepo;
    ETAufgabenControllerDepricated aufgabenControllerSC;
    ETAufgabenController aufgabenController;

    protected void switchOffController (){
        {
            log.error("EtAufgabenController has been switched OFF because unavailable repo!");
            aufgabenControllerSC.setEnable(false);
        }
    }

    protected void switchOnController(){
        log.warn("EtAufgabenController has been switched ON because the repo is available again!");
    }

    protected Boolean isRepoOn(){
        return ergebnisseRepo.ping(ETErgebnisse.class)==1;
    }


}
