package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETSchwelle;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ETMindestschwelle implements ETSchwelle {

    @Id
    private Integer id;
    @Enumerated(EnumType.STRING)
    private ETAufgabenNiveau niveau;
    private Integer mindestSchwelle;


}
