package solutions.dmitrikonnov.einstufungstest.security.securityConfig.userDetailsService;


import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@AllArgsConstructor
public class UserDetailsServiceQualifierResolver {

    ApplicationContext context;


    public UserDetailsService resolveQualifier (String qualifier) {
        return context.
                getBean(qualifier, UserDetailsServiceResolverInterface.class)
                .getUserDetailsService();
    }


}
