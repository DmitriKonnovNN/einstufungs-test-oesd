package solutions.dmitrikonnov.einstufungstest.security.securityConfig.userDetailsService;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import solutions.dmitrikonnov.einstufungstest.verwaltung.user.UserServiceImpl;

@Component("inDataBase")
@AllArgsConstructor (onConstructor_ = {@Autowired} )
public class UserDetailsServiceImplementationWithDataBase implements UserDetailsServiceResolverInterface {

    @Qualifier ("userServiceImpl")
    private final UserServiceImpl userServiceImpl;


    @Override
    public UserDetailsService getUserDetailsService() {
        return userServiceImpl;
    }
}
