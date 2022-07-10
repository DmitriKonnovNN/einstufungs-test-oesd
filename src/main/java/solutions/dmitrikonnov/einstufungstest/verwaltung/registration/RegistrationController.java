package solutions.dmitrikonnov.einstufungstest.verwaltung.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping (path = "api/v2.0.0/registration")
@AllArgsConstructor
public class RegistrationController {


    RegistrationService registrationService;

    @PostMapping
    public String register(@Valid @RequestBody RegistrationRequest registrationRequest){
        return registrationService.register(registrationRequest);
    }
    @GetMapping (path = "confirm")
    public String confirm (@RequestParam ("token") String token) {
        return registrationService.confirmToken(token);
    }
}
