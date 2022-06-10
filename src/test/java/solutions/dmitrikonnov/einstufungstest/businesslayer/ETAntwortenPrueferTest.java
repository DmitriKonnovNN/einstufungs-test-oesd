package solutions.dmitrikonnov.einstufungstest.businesslayer;

import com.github.javafaker.Faker;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import solutions.dmitrikonnov.einstufungstest.domainlayer.*;
import static org.mockito.BDDMockito.*;
import static solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau.*;
import static org.assertj.core.api.Assertions.*;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.MindestSchwelleRepo;

import java.util.*;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETAntwortenPrueferTest {

    @Mock
    private MindestSchwelleRepo mindSchwRepoMock;
    private final Faker faker = new Faker();
    private ETAntwortenPruefer underTest;
    private ETAntwortBogenDto givenAntwortBogen;
    private ETAufgabenBogen givenCachedAB;
    private Map<Integer, ArrayList<String>> givenAufgabenHashZAMap;
    private List<ETAufgabe> givenAufgabenListe = new ArrayList<>();
    private List<ETMindestschwelle> mindestschwellen;
    private ETErgebnisseDto expectedDto;


    @BeforeEach
    void setUp() {
        underTest = new ETAntwortenPruefer(mindSchwRepoMock);

        ETMindestschwelle schwelleA1 = ETMindestschwelle.builder().id(1).niveau(A1).mindestSchwelle(1).build();
        ETMindestschwelle schwelleA2 = ETMindestschwelle.builder().id(2).niveau(A2).mindestSchwelle(1).build();
        ETMindestschwelle schwelleB1 = ETMindestschwelle.builder().id(2).niveau(A2).mindestSchwelle(2).build();
        ETMindestschwelle schwelleB2 = ETMindestschwelle.builder().id(2).niveau(A2).mindestSchwelle(2).build();
        ETMindestschwelle schwelleC1 = ETMindestschwelle.builder().id(2).niveau(A2).mindestSchwelle(2).build();
        ETMindestschwelle schwelleC2 = ETMindestschwelle.builder().id(2).niveau(A2).mindestSchwelle(1).build();
        mindestschwellen = new ArrayList<>();
        mindestschwellen.add(schwelleA1);
        mindestschwellen.add(schwelleA2);
        mindestschwellen.add(schwelleB1);
        mindestschwellen.add(schwelleB2);
        mindestschwellen.add(schwelleC1);
        mindestschwellen.add(schwelleC2);

        ETAufgabe aufgabe1 = ETAufgabe.builder()
                .aufgabeId(4)
                .aufgabenInhalt("Verpiss ...!")
                .aufgabenTyp(ETAufgabenTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A1)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("dich", "euch", "du", "Sie")))
                .loesungen(Collections.singletonList("dich"))
                .build();
        ETAufgabe aufgabe2 = ETAufgabe.builder()
                .aufgabeId(8)
                .aufgabenInhalt("Scheiß ..., Alta!")
                .aufgabenTyp(ETAufgabenTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A1)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("dich selber", "drauf", "und pisse", "dran")))
                .loesungen(Collections.singletonList("drauf"))
                .build();
        ETAufgabe aufgabe3 = ETAufgabe.builder()
                .aufgabeId(9)
                .aufgabenInhalt("Willst du mir .. blasen, Schatz?")
                .aufgabenTyp(ETAufgabenTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A2)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("einen Schwanz", "den Arsch", "die Eier", "einen")))
                .loesungen(Collections.singletonList("einen"))
                .build();
        ETAufgabe aufgabe4 = ETAufgabe.builder()
                .aufgabeId(2)
                .aufgabenInhalt("Du kannst du mich mal ...?")
                .aufgabenTyp(ETAufgabenTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A2)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("kreuz und quer", "am Arsch lecken", "in den Arsch ficken", "kreuzweis")))
                .loesungen(Arrays.asList("am Arsch lecken", "kreuzweis"))
                .build();
        givenAufgabenListe.addAll(Arrays.asList(aufgabe1, aufgabe2, aufgabe3, aufgabe4));

        /*List<ETAufgabenNiveau> moeglicheNiveaus = Arrays.asList(A1,A2,B1,B2,C1,C2);
        List<String> antwortenZumRandomisieren = Arrays.asList("A", "B", "C", "D");*/
        Random r = new Random();
        long range = 1234567L;
        final Long ID = (long)(r.nextDouble()*range);
        int ABH = faker.number().numberBetween(1,10000);



        //TODO: fill both lists!
       /* for (int i = 0; i < 10; i++ ){
            ETAufgabe aufgabe = ETAufgabe.builder()
                    .aufgabeId(faker.number().numberBetween(1,10000))
                    .aufgabenNiveau(moeglicheNiveaus.get(r.nextInt(moeglicheNiveaus.size())))
                    .moeglicheAntworten(new HashSet<>(Collections.singleton(
                            antwortenZumRandomisieren.get(r.nextInt(antwortenZumRandomisieren.size())))))
                    .build();
            //givenAufgabenListe.a
        }
*/


        givenAufgabenHashZAMap = new HashMap<>(){{
            put(sumHash(2,ABH), new ArrayList<>(Collections.singleton("in den Arsch ficken")));
            put(sumHash(4,ABH),new ArrayList<>(Collections.singleton("dich")));
            put(sumHash(8,ABH),new ArrayList<>(Collections.singleton("drauf")));
            put(sumHash(9,ABH),new ArrayList<>(Arrays.asList("die Eier", "einen Schwanz")));


        }};

        givenCachedAB = new ETAufgabenBogen(ID,ABH,givenAufgabenListe);
        givenAntwortBogen = new ETAntwortBogenDto(ID,givenAufgabenHashZAMap);
        expectedDto = ETErgebnisseDto.builder()
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(2)
                .idZuRichtigkeitMap(new HashMap<>(){{
                   put(4,true);
                   put(2,false);
                   put(9, false);
                   put(8,true);
                }})
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,0);
                    put(A2,0);
                }})
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @RepeatedTest(3)
    void shouldcheckBogen() {
        //given
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
        //when
        var actualResult = underTest.checkBogen(givenAntwortBogen,givenCachedAB);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);



    }

    private Integer sumHash (Integer a, Integer b) {
        return a+b;
    }
}