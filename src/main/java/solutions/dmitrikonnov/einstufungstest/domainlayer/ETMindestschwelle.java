package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.*;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETSchwelle;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@Table(name = "ET_MINDEST_SCHWELLE")
public class ETMindestschwelle implements ETSchwelle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;
    @Enumerated(EnumType.STRING)
    private ETAufgabenNiveau niveau;
    private Integer mindestSchwelle;
    private Integer maximumSchwelle;

}
