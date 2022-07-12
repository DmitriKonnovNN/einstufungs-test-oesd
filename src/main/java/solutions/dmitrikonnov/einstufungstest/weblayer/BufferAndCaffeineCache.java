package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.domainlayer.buffer.ETBuffer;
import solutions.dmitrikonnov.einstufungstest.exceptions.TimeForTestExpiredException;

@Service ("bufferAndCaffeineCache")
@Slf4j
@AllArgsConstructor
public class BufferAndCaffeineCache implements AufgabenBogenCache{
    private final ETBuffer buffer;
    private final CacheManager cacheManager;

    @Override
    public void saveToCheck(Integer id, ETAufgabenBogen bogen) {
        var cache = cacheManager.getCache("to-check-cache");
        cache.put(id, bogen);
    }

    @Override
    @Cacheable (cacheNames = "to-check-cache", key = "#id")
    public ETAufgabenBogen fetch(Integer id) {
        return serviceMock(id);
    }

    @Override
    public void evict(Integer id) {
    }

    @Override
    public ETAufgabenBogen getPreparedAufgabenbogen() {
        var b = buffer.getPreparedAufgabenbogen();
        saveToCheck(b.getAufgabenBogenHash(),b);
        return b;
    }
    /**
     * mocks the actual AufgabenBogenService to make the cache either return the cached value or throw
     * a related exception.
     * */
    private ETAufgabenBogen serviceMock (Integer id) {
        throw new TimeForTestExpiredException("Zeit für den Test ist um.");
    }
}
