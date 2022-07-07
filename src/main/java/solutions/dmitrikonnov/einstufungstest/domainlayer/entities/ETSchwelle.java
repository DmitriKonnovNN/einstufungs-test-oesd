package solutions.dmitrikonnov.einstufungstest.domainlayer.entities;

import lombok.*;
import org.springframework.cache.Cache;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@Table(name = "ET_SCHWELLE")
public class ETSchwelle {

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
