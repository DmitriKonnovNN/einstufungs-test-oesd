package solutions.dmitrikonnov.einstufungstest.domainlayer.buffer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabeDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.exceptions.NoTaskSetToServeException;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ET_Buffer {
    private final int bufferAllocSize = 12;
    //TODO: this should be stored IN-MEMORY
    private final Queue<Optional<ETAufgabenBogen>> toServeBuffer = new LinkedBlockingQueue<>(bufferAllocSize);
    private final ETAufgabenService aufgabenService;



    public ETAufgabenBogen getPreparedAufgabenbogen(){


        var bogen = Objects.requireNonNull(
                toServeBuffer.poll())
                .orElseGet(this::getBogenForced);
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
        log.info("ServeBuffer (allocated to {} ) has been filled up with {} elements", bufferAllocSize, toServeBuffer.size());
        toServeBuffer.forEach(b-> {
            System.out.println(b.get().getAufgabenBogenHash());
            (b.get().getAufgabenListe().stream().map(ETAufgabeDto::getAufgabenHash).collect(Collectors.toList())).forEach(System.out::println);
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
