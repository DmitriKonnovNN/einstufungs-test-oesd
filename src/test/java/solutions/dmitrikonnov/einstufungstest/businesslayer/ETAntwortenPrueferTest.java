package solutions.dmitrikonnov.einstufungstest.businesslayer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import solutions.dmitrikonnov.einstufungstest.domainlayer.*;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.MindestSchwelleRepo;
import solutions.dmitrikonnov.einstufungstest.utils.AntwortBogenCheckedEvent;
import solutions.dmitrikonnov.einstufungstest.utils.ETAufgabenToDTOConverter;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETAntwortenPrueferTest {

    @Mock
    private MindestSchwelleRepo mindSchwRepoMock;
    @Mock
    private ApplicationEventPublisher publisherMock;
    private AntwortBogenCheckedEvent expectedEvent;
    private final Faker faker = new Faker();
    private ETAntwortenPruefer underTest;
    private ETAntwortBogenDto givenAntwortBogen;
    private ETAufgabenBogen givenCachedAufgabenBogen;
    private Map<Integer, ArrayList<String>> givenItemHashZuAMap;
    private List<ETAufgabe> givenAufgabenListe = new ArrayList<>();
    private List<ETMindestschwelle> mindestschwellen;
    private ETErgebnisseDto expectedDto;
    private ETAufgabenToDTOConverter converter = new ETAufgabenToDTOConverter();
    private List<ETAufgabeDto> givenAufgabenDTOListe = new ArrayList<>();


    @BeforeEach
    void setUp() {
        Random r = new Random();
        long range = 1234567L;
        final Long ID = (long)(r.nextDouble()*range);

        int ABH = faker.number().numberBetween(1,10000);
        underTest = new ETAntwortenPruefer(publisherMock,mindSchwRepoMock);


        ETMindestschwelle schwelleA1 = ETMindestschwelle.builder().id((short)1).niveau(A1).mindestSchwelle(1).build();
        ETMindestschwelle schwelleA2 = ETMindestschwelle.builder().id((short)2).niveau(A2).mindestSchwelle(1).build();
        ETMindestschwelle schwelleB1 = ETMindestschwelle.builder().id((short)3).niveau(B1).mindestSchwelle(2).build();
        ETMindestschwelle schwelleB2 = ETMindestschwelle.builder().id((short)4).niveau(B2).mindestSchwelle(2).build();
        ETMindestschwelle schwelleC1 = ETMindestschwelle.builder().id((short)5).niveau(C1).mindestSchwelle(2).build();
        ETMindestschwelle schwelleC2 = ETMindestschwelle.builder().id((short)6).niveau(C2).mindestSchwelle(1).build();
        mindestschwellen = new ArrayList<>();
        mindestschwellen.add(schwelleA1);
        mindestschwellen.add(schwelleA2);
        mindestschwellen.add(schwelleB1);
        mindestschwellen.add(schwelleB2);
        mindestschwellen.add(schwelleC1);
        mindestschwellen.add(schwelleC2);

        ETItem item1 = ETItem.builder()
                .itemId(1)
                .moeglicheAntworten(new HashSet<>(Arrays.asList("dich", "euch", "du", "Sie")))
                .loesungen(Collections.singletonList("dich"))
                .build();

        ETItem item2 = ETItem.builder()
                .itemId(2)
                .moeglicheAntworten(new HashSet<>(Arrays.asList("dich selber", "drauf", "und pisse", "dran")))
                .loesungen(Collections.singletonList("drauf"))
                .build();
        ETItem item3 = ETItem.builder()
                .itemId(3)
                .moeglicheAntworten(new HashSet<>(Arrays.asList("einen Schwanz", "den Arsch", "die Eier", "einen")))
                .loesungen(Collections.singletonList("einen"))
                .build();
        ETItem item4 = ETItem.builder()
                .itemId(4)
                .moeglicheAntworten(new HashSet<>(Arrays.asList("kreuz und quer", "am Arsch lecken", "in den Arsch ficken", "kreuzweis")))
                .loesungen(Arrays.asList("am Arsch lecken", "kreuzweis"))
                .build();


        ETItem item5 = ETItem.builder()
                .itemId(5)
                .itemAufgabe("...wohnt in Salzburg")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Anna"))
                .build();
        ETItem item6 = ETItem.builder()
                .itemId(6)
                .itemAufgabe("...kommt aus Graz")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Katharina"))
                .build();
        ETItem item7 = ETItem.builder()
                .itemId(7)
                .itemAufgabe("...mag Musik")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Maximilian"))
                .build();
        ETItem item8 = ETItem.builder()
                .itemAufgabe("...arbeitet als Lehrerin")
                .itemId(8)
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Anna"))
                .build();
        ETItem item9 = ETItem.builder()
                .itemId(8)
                .itemAufgabe("...kocht und schwimmt gern")
                .moeglicheAntworten(new HashSet<>(Arrays.asList("Maximilian","Anna", "Katharina")))
                .loesungen(Collections.singletonList("Katharina"))
                .build();


        ETAufgabe aufgabe1 = ETAufgabe.builder()
                .aufgabeId(4)
                .aufgabenInhalt("Verpiss ...!")
                .aufgabenTyp(ETAufgabenTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A1)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item1)))
                .build();
        ETAufgabe aufgabe2 = ETAufgabe.builder()
                .aufgabeId(8)
                .aufgabenInhalt("Scheiß ..., Alta!")
                .aufgabenTyp(ETAufgabenTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A1)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item2)))
                .build();
        ETAufgabe aufgabe3 = ETAufgabe.builder()
                .aufgabeId(9)
                .aufgabenInhalt("Willst du mir .. blasen, Schatz?")
                .aufgabenTyp(ETAufgabenTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A2)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item3)))
                .build();
        ETAufgabe aufgabe4 = ETAufgabe.builder()
                .aufgabeId(2)
                .aufgabenInhalt("Du kannst du mich mal ...?")
                .aufgabenTyp(ETAufgabenTyp.SPRACHBAUSTEINE)
                .aufgabenNiveau(A2)
                .aufgabenStellung("Ergänzen Sie den Satz!")
                .items(new HashSet<>(Collections.singleton(item4)))
                .build();

        ETAufgabe aufgabe5 = ETAufgabe.builder()
                .aufgabeId(1)
                .aufgabenInhalt("Maximilian\n" +
                        "Servus! Mein Name ist Maximilian Gruber. Ich komme aus Österreich und wohne in Wien. Ich habe eine Ausbildung zum Mechatroniker gemacht und arbeite jetzt in einer Autowerkstatt. Ich mag Autos! In der Freizeit spiele ich Fußball, fahre Rad und höre Musik. Ein Tag ohne Musik ist kein guter Tag!\n" +
                        "\n" +
                        "Anna\n" +
                        "Grüß Gott! Ich heiße Anna Moser und komme aus Österreich, aus Klagenfurt. Jetzt wohne ich in Salzburg. Ich bin Musikerin und Lehrerin. Ich unterrichte am Mozarteum. Ich mag meine Studenten, die Arbeit macht mir Spaß. Meine Hobbys sind Reisen und Lesen. Ich lese gern Romane.\n" +
                        "\n" +
                        "Katharina\n" +
                        "Guten Tag! Ich heiße Katharina Berger. Ich wohne in Linz, aber ich komme aus Graz. Ich bin Krankenschwester von Beruf und arbeite im Spital. Meine Hobbys sind kochen und Filme sehen. Ich mag auch Sport: Schwimmen tut gut! Ich schwimme zweimal pro Woche.\n")
                .aufgabenTyp(ETAufgabenTyp.LESEN)
                .aufgabenNiveau(A1)
                .aufgabenStellung("Aufgaben 1-5\n" +
                        "Kreuzen Sie an. Was passt zu Maximilian, Anna, Katharina?\n")
                .items(new HashSet<>(Arrays.asList(item5,item6,item7,item8,item9)))
                .build();

        givenAufgabenListe.addAll(Arrays.asList(aufgabe1, aufgabe2, aufgabe3, aufgabe4, aufgabe5));
        givenAufgabenDTOListe = converter.convert(givenAufgabenListe,ABH,ID);

        Map<Integer, List<String>> itemZuLoesungen = new HashMap<>(){{
            put(item1.getItemId(),item1.getLoesungen());
            put(item2.getItemId(),item2.getLoesungen());
            put(item3.getItemId(),item3.getLoesungen());
            put(item4.getItemId(),item4.getLoesungen());
            put(item5.getItemId(), item5.getLoesungen());
            put(item6.getItemId(), item6.getLoesungen());
            put(item7.getItemId(), item7.getLoesungen());
            put(item8.getItemId(), item8.getLoesungen());
            put(item9.getItemId(), item9.getLoesungen());
        }};

        Map<Integer, ETAufgabenNiveau> itemZuNiveau = new HashMap<>(){{
            put(item1.getItemId(),aufgabe1.getAufgabenNiveau());
            put(item2.getItemId(),aufgabe2.getAufgabenNiveau());
            put(item3.getItemId(),aufgabe3.getAufgabenNiveau());
            put(item4.getItemId(),aufgabe4.getAufgabenNiveau());
            put(item5.getItemId(),aufgabe5.getAufgabenNiveau());
            put(item6.getItemId(),aufgabe5.getAufgabenNiveau());
            put(item7.getItemId(),aufgabe5.getAufgabenNiveau());
            put(item8.getItemId(),aufgabe5.getAufgabenNiveau());
            put(item9.getItemId(),aufgabe5.getAufgabenNiveau());

        }};


        /*List<ETAufgabenNiveau> moeglicheNiveaus = Arrays.asList(A1,A2,B1,B2,C1,C2);
        List<String> antwortenZumRandomisieren = Arrays.asList("A", "B", "C", "D");*/




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
        givenCachedAufgabenBogen = new ETAufgabenBogen(
                ID,
                ABH,
                givenAufgabenDTOListe,
                System.currentTimeMillis(),
                itemZuLoesungen,
                itemZuNiveau );


        givenItemHashZuAMap = new HashMap<>(){{
            put(sumHash(item4.getItemId(),ABH), new ArrayList<>(Collections.singleton("in den Arsch ficken")));
            put(sumHash(item1.getItemId(),ABH),new ArrayList<>(Collections.singleton("dich")));
            put(sumHash(item2.getItemId(), ABH),new ArrayList<>(Collections.singleton("drauf")));
            put(sumHash(item3.getItemId(),ABH),new ArrayList<>(Arrays.asList("die Eier", "einen Schwanz")));
            put(sumHash(item5.getItemId(),ABH),new ArrayList<>(Collections.singletonList("Anna"))); // correct
            put(sumHash(item6.getItemId(),ABH),new ArrayList<>(Collections.singletonList(""))); // false
            put(sumHash(item7.getItemId(),ABH),new ArrayList<>(Arrays.asList("Anna", "Katharina"))); // two false
            put(sumHash(item8.getItemId(),ABH),new ArrayList<>(Arrays.asList("Maximilian", "Anna"))); // false: one false one right
            put(sumHash(item9.getItemId(),ABH),new ArrayList<>(Collections.singletonList("Katharina"))); // correct

        }};


        givenAntwortBogen = new ETAntwortBogenDto(ID,givenItemHashZuAMap);
        expectedDto = ETErgebnisseDto.builder()
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(4)
                .idZuRichtigkeitMap(new HashMap<>(){{
                   put(item1.getItemId(),true);
                   put(item2.getItemId(),true);
                   put(item3.getItemId(), false);
                   put(item4.getItemId(),false);
                   put(item5.getItemId(),true);
                   put(item6.getItemId(),false);
                   put(item7.getItemId(),false);
                   put(item8.getItemId(),false);
                   put(item9.getItemId(),true);
                }})
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,0);
                    put(A2,0);
                    put(B1,0);
                    put(B2,0);
                    put(C1,0);
                    put(C2,0);
                }})
                .build();

        //TODO: check that the correct event has been thrown
        expectedEvent = new AntwortBogenCheckedEvent(underTest,ID,expectedDto.toString());
    }

    @AfterEach
    void tearDown() {
    }

    //@RepeatedTest(3)
    @Test
    void shouldcheckBogen() {
        //given

        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
        //when
        var actualResult = underTest.checkBogen(givenAntwortBogen, givenCachedAufgabenBogen);
        //then
        then(publisherMock)
                .should()
                .publishEvent(any(AntwortBogenCheckedEvent.class));
        assertThat(actualResult).isEqualTo(expectedDto);



    }

    private Integer sumHash (Integer a, Integer b) {
        return a+b;
    }
}