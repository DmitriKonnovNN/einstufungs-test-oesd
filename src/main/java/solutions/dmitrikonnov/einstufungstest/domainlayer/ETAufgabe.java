package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.*;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * this class should be persistent
 * */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "ET_AUFGABE", indexes = @Index(columnList = "AUFGABEN_NIVEAU", name = "ET_AUFGABE_NIVEAU_IDX"))
public class ETAufgabe {

    @Id
    @SequenceGenerator(name = "et_aufgabe_seq",
            sequenceName = "et_aufgabe_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "et_aufgabe_seq")
    private Integer aufgabeId;



    private String aufgabenStellung;

    /*
    * mappedBy attribute which indicates that the @ManyToOne side is responsible for handling this bidirectional association
    *
    *Vergiss nicht: mappedBy bekommt den Namen, wie das Element im Kind genannt wird, also ggf. im ETItem: "aufgabe"
    *
    * */
    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "aufgabe")
    private Set<ETItem> items; // Set or List?

    /*
    * https://vladmihalcea.com/jpa-hibernate-synchronize-bidirectional-entity-associations/
    */

    public void addItem(ETItem item) {
        items.add(item);
        item.setAufgabe(this);
    }
    public void removeItem(ETItem item) {
        items.remove(item);
        item.setAufgabe(null);
    }
    /**
     * Either a link to media content or simple text.
     * */
    private String aufgabenInhalt;

    @Enumerated(EnumType.STRING)
    private ETAufgabenTyp aufgabenTyp;

    @Column (name = "AUFGABEN_NIVEAU")
    @Enumerated(EnumType.STRING)
    private ETAufgabenNiveau aufgabenNiveau;

    private Integer gewichtung;

    private Long counter;

    @Temporal(TemporalType.TIMESTAMP)
    @Column (updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column (insertable = false, updatable = false)
    @org.hibernate.annotations.Generated (GenerationTime.ALWAYS)
    private Date lastModified;





}
