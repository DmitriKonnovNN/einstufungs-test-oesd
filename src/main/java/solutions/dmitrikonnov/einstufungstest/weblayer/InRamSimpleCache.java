package solutions.dmitrikonnov.einstufungstest.weblayer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.domainlayer.buffer.ETBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service ("inRamSimpleCache")
@Slf4j
@AllArgsConstructor
public class InRamSimpleCache implements AufgabenBogenCache {

    private final Map<Integer, ETAufgabenBogen> toCheckCache = new ConcurrentHashMap<>();
    private final ETBuffer buffer;

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
