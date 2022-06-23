package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AufgabenBogenPersistedIdSequence {
    @Id
    @SequenceGenerator(name = "et_aufgabenbogen_seq",
            sequenceName = "et_aufgabenbogen_seq",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "et_aufgabenbogen_seq")
    private Long aufgabenBogenId;
}
