package solutions.dmitrikonnov.einstufungstest.security.securityConfig;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import solutions.dmitrikonnov.einstufungstest.security.securityConfig.securityConfigUtils.SecurityConfigWebAdapterQualifierResolver;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity (prePostEnabled = true)
public class ApplicationSecurityMainConfig {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final String SSC_CONF_MSG = "Current Spring Security Configuration : %s";

    protected final SecurityConfigWebAdapterQualifierResolver resolver;
    protected final  WebSecurityConfigurerAdapter adapter;



    /**
     * creates an instance of {@link WebSecurityConfigurerAdapter} by fetching the concerned bean name of its implementations
     * with JWT-TOKEN {@link io.dmitrikonnov.DeanerySimpleSpringBootApp.configuration.SecurityConfig.securityConfigImplementation.WebSecurityWithJwtConfig}
     * or with LOGIN-FORM {@link io.dmitrikonnov.DeanerySimpleSpringBootApp.configuration.SecurityConfig.securityConfigImplementation.WebSecurityWithLoginConfig }
     * from the properties whose path is configured in {@link io.dmitrikonnov.DeanerySimpleSpringBootApp.configuration.ConfigurationProperties}.
     *
     * To start with a specific implementation interchange it in the properties with respect to bean's name to go for given as parameter in @Component.
     *
     * */

    public ApplicationSecurityMainConfig(@Autowired SecurityConfigWebAdapterQualifierResolver resolver,
                                         @Value("${app.security.webSecurityAdapterQualifier}") String qualifier) {
        this.resolver = resolver;
        this.adapter = resolver.getAdapterQualifier(qualifier);
        log.warn(String.format(SSC_CONF_MSG,qualifier));
    }


}
