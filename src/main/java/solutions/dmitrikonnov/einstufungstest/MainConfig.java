package solutions.dmitrikonnov.einstufungstest;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@EnableScheduling
public class MainConfig {
}
