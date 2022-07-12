package solutions.dmitrikonnov.einstufungstest;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@EnableScheduling
@OpenAPIDefinition
//@EnableJpaRepositories(repositoryBaseClass = PingableImpl.class)

@EnableTransactionManagement
@EnableConfigurationProperties
@PropertySource(value = "classpath:etapplication.properties")
public class MainConfig {

/*  @Bean
    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
        return new CacheManagerCustomizer<ConcurrentMapCacheManager>() {
            @Override
            public void customize(ConcurrentMapCacheManager cacheManager) {
                cacheManager.
            }
        };
    }*/







    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Einstungstest Application")
                        .description("Einstufungstest für ÖSD")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Einstufungstest für ÖSD")
                        .url("/"));
    }

    //http://localhost:8080/swagger-ui/index.html

}
