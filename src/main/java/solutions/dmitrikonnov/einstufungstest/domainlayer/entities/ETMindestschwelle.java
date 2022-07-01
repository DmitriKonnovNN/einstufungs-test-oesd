package solutions.dmitrikonnov.einstufungstest.domainlayer.entities;

import lombok.*;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETSchwelle;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

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
    @Column(nullable = false,unique = true)
    private ETAufgabenNiveau niveau;
    @Column (nullable = false)
    private Short mindestSchwelle;
    @Column (nullable = false)
    private Short maximumSchwelle;

}