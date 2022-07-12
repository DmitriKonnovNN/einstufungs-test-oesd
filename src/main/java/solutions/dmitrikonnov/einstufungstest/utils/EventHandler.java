package solutions.dmitrikonnov.einstufungstest.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.buffer.ET_Buffer;
import solutions.dmitrikonnov.einstufungstest.weblayer.InRamSimpleCache;

@Service
@Slf4j
@AllArgsConstructor
public class EventHandler {

    private final ET_Buffer buffer;

    @Async
    @EventListener (AntwortBogenCheckedEvent.class)
    public void onApplicationEvent(AntwortBogenCheckedEvent event){
        log.info("Event {} --- time {} .", event.getCHECKED_MSG(),event.getTimestamp());
        log.debug("Temporary result of check {}", event.getTempResult());
       // cache.evict(event.getBogenId()); // TODO: no explicit eviction any longer needed: replace by redis with TTL!

    }
    @Async
    @EventListener(AufgabenBogenFetchedFromCache.class)
    public void onApplicationEvent(AufgabenBogenFetchedFromCache event){
        buffer.fillUpIfAlmostEmpty();
    }

    @EventListener (ApplicationReadyEvent.class)
    public void onApplicationEvent (ApplicationReadyEvent event) {
        buffer.warmUp();
    }
    //TODO: As well, try out: ContextRefreshedEvent, ApplicationStartedEvent
}
