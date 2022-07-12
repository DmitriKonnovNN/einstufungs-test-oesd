package solutions.dmitrikonnov.einstufungstest.security.securityConfig.userDetailsService;


import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@AllArgsConstructor
public class UDSQualifierResolver {

    ApplicationContext context;


    public UserDetailsService resolve(String qualifier) {
        return context.
                getBean(qualifier, UDSResolverInterface.class)
                .getUserDetailsService();
    }


}
