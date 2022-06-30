package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETConstructorService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.construct.ETAufgabeConstructDTO;

@RestController
@RequestMapping("api/v2.0.0/constructor")
@AllArgsConstructor
public class ETConstructorController {
    private final ETConstructorService service;
    @PostMapping()
    public ResponseEntity<ETAufgabe> addAufgabe(@RequestBody ETAufgabeConstructDTO dto){
        var result = service.addAufgabe(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);

    }
}
