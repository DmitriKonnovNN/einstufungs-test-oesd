package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller ("api/v2.0.0/verwaltung")
@AllArgsConstructor
public class ETVerwaltungMVC {

    @GetMapping("'/")
    public String greetUser(){
        return "verwaltung";
    }

    @GetMapping("/login")
    public String getLoginView (){
        return "login";
    }
}
