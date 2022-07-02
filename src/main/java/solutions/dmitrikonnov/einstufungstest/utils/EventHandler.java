package solutions.dmitrikonnov.einstufungstest.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.weblayer.InRamSimpleCache;

@Service
@Slf4j
@AllArgsConstructor
public class EventHandler {

    private final InRamSimpleCache cache;

    @Async
    @EventListener (AntwortBogenCheckedEvent.class)
    public void onApplicationEvent(AntwortBogenCheckedEvent event){
        log.info("Event {} --- time {} .", event.getCHECKED_MSG(),event.getTimestamp());
        log.debug("Temporary result of check {}", event.getTempResult());
        cache.evict(event.getBogenId());

    }
    @Async
    @EventListener(AufgabenBogenFetchedFromCache.class)
    public void onApplicationEvent(AufgabenBogenFetchedFromCache event){
        cache.checkIfAlmostEmptyAndPopulate();
    }

    @EventListener (ApplicationReadyEvent.class)
    public void onApplicationEvent (ApplicationReadyEvent event) {
        cache.warmUp();
    }
    //TODO: As well, try out: ContextRefreshedEvent, ApplicationStartedEvent
}
