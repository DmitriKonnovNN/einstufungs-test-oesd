package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.exceptions.NoTaskSetToServeException;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


interface AufgabenBogenCache {

    void saveToCheck(Integer id, ETAufgabenBogen bogen);

    ETAufgabenBogen fetch(Integer id);

    void evict(Integer id);

    ETAufgabenBogen getPreparedAufgabenbogen();

}
