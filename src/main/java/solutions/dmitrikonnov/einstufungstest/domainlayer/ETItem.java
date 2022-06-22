package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.*;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table (name = "ET_ITEM", indexes = @Index(columnList = "ET_AUFGABE_ID", name = "ET_ITEM_AUFG_IDX"))
public class ETItem {

    @Id
    @org.hibernate.annotations.GenericGenerator(
            name = "et_item_id_seq_generator",
            strategy = "enhanced-sequence",
            parameters = {
            @org.hibernate.annotations.Parameter(
                    name = "sequence_name",
                    value = "et_item_id_seq"
            ),
            @org.hibernate.annotations.Parameter(
                    name = "initial_value",
                    value = "100"
            ), @org.hibernate.annotations.Parameter(
                    name = "allocationSize",
                    value = "1"
                    )

    })

    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "et_item_id_seq_generator")
    private Integer itemId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name ="ET_AUFGABE_ID")
    private ETAufgabe aufgabe;

    @ElementCollection
    @CollectionTable(name = "et_moegl_antw_set")
    private Set<String> moeglicheAntworten;


    @ElementCollection
    @CollectionTable (name = "et_loesungen_set")
    private List<String> loesungen;

    private Long counter;
    private Long counterCorrectAnswers;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column (insertable = false, updatable = false)
    @org.hibernate.annotations.Generated (GenerationTime.ALWAYS)
    private Date lastModified;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ETItem)) return false;
        return itemId!=null && itemId.equals(((ETItem) o).getItemId());
    }
/*
* https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
* */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
