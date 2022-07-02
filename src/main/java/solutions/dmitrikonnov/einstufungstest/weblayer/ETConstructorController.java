package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETConstructorService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.construct.ETAufgabeConstructDTO;
import solutions.dmitrikonnov.einstufungstest.domainlayer.construct.ETSchwellenConstructDTO;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETSchwelle;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("api/v2.0.0/constructor")
@AllArgsConstructor
public class ETConstructorController {
    private final ETConstructorService service;
    @PostMapping()
    public ResponseEntity<ETAufgabe> addTask(@RequestBody ETAufgabeConstructDTO dto){
        var result = service.addAufgabe(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/limits")
    public ResponseEntity<ETSchwelle> addLimit(@Valid @RequestBody ETSchwellenConstructDTO schwelle){
       return ResponseEntity.status(HttpStatus.CREATED).body(service.addSchwelle(schwelle));
    }

    @PutMapping("/limits")
    public ResponseEntity<ETSchwelle> updateLimit (@Valid @RequestBody ETSchwellenConstructDTO schwelle){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateSchwelle(schwelle));
    }
    @PatchMapping("/limits")
    @ResponseStatus(HttpStatus.OK)
    public void patchLimit (@Valid @RequestBody ETSchwellenConstructDTO schwelle){
        service.patchSchwelle(schwelle);
    }

    @GetMapping("/limits")
    public Map<ETAufgabenNiveau,Short> getAllMaxLimits(){
        return service.getMaxSchwellenByNiveaus();
    }


}
