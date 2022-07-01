package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@AllArgsConstructor
@Slf4j
public class InRamSimpleCache {
    private final int cacheAllocSize = 12;
    private final Map<Long, ETAufgabenBogen> toCheckCache = new ConcurrentHashMap<>();
    private final Queue<Optional<ETAufgabenBogen>> toServeCache = new LinkedBlockingQueue<>(cacheAllocSize);
    private final ETAufgabenService aufgabenService;


    private void saveToCheck(Long id, ETAufgabenBogen bogen){
        bogen.setCachedAt(System.currentTimeMillis());
        toCheckCache.put(id, bogen);
    }

    public ETAufgabenBogen fetch(Long id){
        return toCheckCache.get(id);
    }

    public void evict(Long id) {
        toCheckCache.remove(id);
    }

    public ETAufgabenBogen getPreparedAufgabenbogen(){

        var bogen = toServeCache.poll().orElse(getBogenForced());
        saveToCheck(bogen.getAufgabenBogenId(),bogen);
        return bogen;
    }
    public void checkIfAlmostEmptyAndPopulate (){
        if(isAlmostEmpty()) populateCache();
    }
    @Async
    protected void populateCache(){
        for (int i = cacheAllocSize; i > 0 ; i--) {
            var aufgabenBogen = aufgabenService.getAufgabenListe();
            if(aufgabenBogen==null) {
                log.warn("No Bogen in Cache!");
                break;
            }
            toServeCache.offer(Optional.of(aufgabenBogen));
        }
        log.info("Cache (allocated to {} ) has been populated with {} elements",cacheAllocSize ,toServeCache.size());

    }

    private boolean isAlmostEmpty (){
        int currentSize = toServeCache.size();
        log.debug("Size of Aufg-Cache : {}", currentSize);
        return currentSize < cacheAllocSize/3;
    }
    private ETAufgabenBogen getBogenForced(){
        var bogen = aufgabenService.getAufgabenListe();
        if(bogen == null) {
            log.error("No Bogen set up!");
        throw new RuntimeException("No Bogen set up!");
        }
       return bogen;

    }

    public void warmUpCache(){
        log.debug("Cache warm-up started");
        populateCache();
    }

}
