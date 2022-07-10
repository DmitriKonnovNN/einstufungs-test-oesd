package solutions.dmitrikonnov.einstufungstest.security.securityConfig.securityConfigUtils;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@AllArgsConstructor
public class SecurityConfigWebAdapterQualifierResolver {

    private final ApplicationContext context;


    public WebSecurityConfigurerAdapter getAdapterQualifier(String qualifier)
    {
        return (WebSecurityConfigurerAdapter) context.getBean(qualifier);
    }
}
