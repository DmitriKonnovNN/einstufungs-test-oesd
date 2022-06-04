package solutions.dmitrikonnov.einstufungstest.domainlayer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Builder
public class ETErgebnisse {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator")
    private  String id;

    private Integer aufgabenBogenHash;

    @ElementCollection
    @CollectionTable (name = "et_r_loesungen_nach_niveau")
    private List<String> richtigeLoesungenNachNiveau;

    private int A1richtig;
    private int A2richtig;
    private int B1richtig;
    private int B2richtig;
    private int C1richtig;
    private int C2richtig;

    private Boolean A1erreicht;
    private Boolean A2erreicht;
    private Boolean B1erreicht;
    private Boolean B2erreicht;
    private Boolean C1erreicht;
    private Boolean C2erreicht;

    private ETAufgabenNiveau maxErreichtesNiveau;
    private Integer zahlRichtigerAntworten;

    @ElementCollection
    @CollectionTable (name = "et_ergebnisse_mapping")
    @MapKeyColumn (name = "et_aufg_id")
    @Column (name = "et_aufg_correctness")
    private Map<Integer, Boolean> idZuRichtigkeitMap;

    @Temporal(TemporalType.TIMESTAMP)
    @Column (updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column (insertable = false, updatable = false)
    @org.hibernate.annotations.Generated (GenerationTime.ALWAYS)
    private Date lastModified;



}
