package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETErgebnisse;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.ETErgebnisseRepo;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.Pingable;

@AllArgsConstructor
@Service
@Slf4j
public class ETControllerSwitcher {
    Pingable ergebnisseRepo;
    SwitchableController controller;

    protected void switchOffController (){
        {
            log.error("EtAufgabenController has been switched OFF because unavailable repo!");
            controller.setEnable(false);
        }
    }

    protected void switchOnController(){
        log.warn("EtAufgabenController has been switched ON because the repo is available again!");
    }

    protected Boolean isRepoOn(){
        return ergebnisseRepo.ping(ETErgebnisse.class)==1;
    }


}
