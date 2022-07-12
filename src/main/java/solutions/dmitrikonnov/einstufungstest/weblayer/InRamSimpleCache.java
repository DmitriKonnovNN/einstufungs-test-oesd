package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.domainlayer.buffer.ET_Buffer;
import solutions.dmitrikonnov.einstufungstest.exceptions.NoTaskSetToServeException;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Service ("inRamSimpleCache")
@Slf4j
@AllArgsConstructor
public class InRamSimpleCache implements AufgabenBogenCache {

    private final Map<Integer, ETAufgabenBogen> toCheckCache = new ConcurrentHashMap<>();
    private final ET_Buffer buffer;

    public void saveToCheck(Integer id, ETAufgabenBogen bogen){
        bogen.setCachedAt(System.currentTimeMillis());
        toCheckCache.put(id, bogen);
    }
    public ETAufgabenBogen fetch(Integer id){
        return toCheckCache.get(id);
    }

    // TODO: no explicit eviction any longer needed: replace by redis with TTL!
    public void evict(Integer id) {
        toCheckCache.remove(id);
    }

    @Override
    public ETAufgabenBogen getPreparedAufgabenbogen() {
        var b = buffer.getPreparedAufgabenbogen();
        saveToCheck(b.getAufgabenBogenHash(),b);
        return b;
    }
}
