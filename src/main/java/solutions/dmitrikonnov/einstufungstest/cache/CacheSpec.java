package solutions.dmitrikonnov.einstufungstest.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Objects;
@AllArgsConstructor
@Getter
@Setter
public class CacheSpec {

    private String name;
    private Integer timeout;
    private Integer max = 200;


}
