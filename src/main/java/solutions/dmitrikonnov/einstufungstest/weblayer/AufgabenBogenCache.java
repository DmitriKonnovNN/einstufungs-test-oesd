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

@Slf4j
@AllArgsConstructor
public abstract class AufgabenBogenCache {
    protected final int bufferAllocSize = 12;
    //TODO: this should be stored IN-MEMORY
    protected final Queue<Optional<ETAufgabenBogen>> toServeBuffer = new LinkedBlockingQueue<>(bufferAllocSize);
    protected final ETAufgabenService aufgabenService;


    protected abstract void saveToCheck(Integer id, ETAufgabenBogen bogen);

    protected abstract ETAufgabenBogen fetch(Integer id);

    // TODO: no explicit eviction any longer needed: replace by redis with TTL!
    protected abstract void evict(Integer id);

    public ETAufgabenBogen getPreparedAufgabenbogen(){

        var bogen = Objects.requireNonNull(
                toServeBuffer.poll())
                .orElseGet(this::getBogenForced);
        saveToCheck(bogen.getAufgabenBogenHash(),bogen);
        return bogen;
    }
    public void fillUpIfAlmostEmpty(){
        if(isAlmostEmpty()) fillUpBuffer();
    }
    @Async
    protected void fillUpBuffer(){

        for (int i = bufferAllocSize; i > 0 ; i--) {
            var aufgabenBogen = aufgabenService.getAufgabenListe();
            if(aufgabenBogen==null) {
                log.warn("No Bogen in ServeBuffer!");
                toServeBuffer.offer(Optional.empty());
                break;
            }

            toServeBuffer.offer(Optional.of(aufgabenBogen));
        }
        toServeBuffer.forEach(bogen-> System.out.println(bogen.get().getAufgabenBogenHash()));
        log.info("ServeBuffer (allocated to {} ) has been filled up with {} elements", bufferAllocSize, toServeBuffer.size());
    }

    private boolean isAlmostEmpty (){
        int currentSize = toServeBuffer.size();
        log.debug("Size of Aufg-Cache : {}", currentSize);
        return currentSize < bufferAllocSize /3;
    }
    private ETAufgabenBogen getBogenForced(){
        var bogen = aufgabenService.getAufgabenListe();
        if(bogen == null) {
            log.error("No Bogen set up!");
            toServeBuffer.offer(Optional.empty());
            throw new NoTaskSetToServeException("No Bogen set up!");
        }
        return bogen;

    }

    public void warmUp(){
        log.debug("Buffer warm-up started");
        fillUpBuffer();
    }
}
