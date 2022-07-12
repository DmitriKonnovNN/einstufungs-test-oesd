package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.exceptions.TimeForTestExpiredException;

import java.util.Objects;

@Service ("bufferAndCaffeineCache")
@Slf4j
public class BufferAndCaffeineCache extends AufgabenBogenCache{

    public BufferAndCaffeineCache(@Autowired ETAufgabenService aufgabenService) {
        super(aufgabenService);
    }
    @Override
    @CachePut (cacheNames = "to-check-cache", key = "#bogen.aufgabenBogenHash")
    public void saveToCheck(Integer id, ETAufgabenBogen bogen) {
        log.debug("save to check-cache: id {}", id);
    }

    @Override
    @Cacheable (cacheNames = "to-check-cache", key = "#id" )
    public ETAufgabenBogen fetch(Integer id) {
        throw  new TimeForTestExpiredException("Zeit für den Test ist um.");
    }

    @Override
    public void evict(Integer id) {
    }

}
