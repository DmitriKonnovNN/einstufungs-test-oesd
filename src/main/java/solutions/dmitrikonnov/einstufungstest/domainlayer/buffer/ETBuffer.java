package solutions.dmitrikonnov.einstufungstest.domainlayer.buffer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabeDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.exceptions.NoTaskSetToServeException;
import solutions.dmitrikonnov.einstufungstest.exceptions.TaskNotFoundException;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor (onConstructor_ = {@Autowired})
public class ETBuffer {
    private final int bufferAllocSize = 12;
    private final Queue<Optional<ETAufgabenBogen>> toServeBuffer = new LinkedBlockingQueue<>(bufferAllocSize);
    private final ETAufgabenService aufgabenService;

    public ETAufgabenBogen getPreparedAufgabenbogen(){

        return Objects.requireNonNull(
                toServeBuffer.poll())
                .orElseGet(this::getBogenForced);
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
        log.warn("ServeBuffer (allocated to {} ) has been filled up with {} elements", bufferAllocSize, toServeBuffer.size());
        toServeBuffer.forEach(b-> {
            log.debug(b.orElseThrow(()->new NoTaskSetToServeException("No Bogen set up!")).getAufgabenBogenHash().toString());
            b.get().getAufgabenListe().stream()
                    .map(ETAufgabeDto::getAufgabenHash)
                    .map(Object::toString)
                    .collect(Collectors.toList())
                    .forEach(log::trace);
        });
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
