package solutions.dmitrikonnov.einstufungstest.businesslayer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisseDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETMindestschwelle;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.MindestSchwelleRepo;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETErgebnisseEvaluatorTest {

    @Mock
    private MindestSchwelleRepo mindSchwRepoMock;
    private List<ETMindestschwelle> mindestschwellen;
    private ETErgebnisseEvaluator underTest;
    private ETErgebnisseDto passedDto;
    private ETErgebnisseDto expectedDto;
    private ETErgebnisseDto passedDto2;
    private ETErgebnisseDto expectedDto2;
    private ETErgebnisseDto passedDto3;
    private ETErgebnisseDto expectedDto3;
    private ETErgebnisseDto passedDto4;
    private ETErgebnisseDto expectedDto4;
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new ETErgebnisseEvaluator(mindSchwRepoMock);
        ETMindestschwelle schwelleA1 = ETMindestschwelle.builder().id(1).niveau(A1).mindestSchwelle(1).build(); // 50 % von allen (von 2)
        ETMindestschwelle schwelleA2 = ETMindestschwelle.builder().id(2).niveau(A2).mindestSchwelle(1).build(); // 50 % von allen (von 2)
        ETMindestschwelle schwelleB1 = ETMindestschwelle.builder().id(3).niveau(B1).mindestSchwelle(2).build(); // 50 % von allen (von 4)
        ETMindestschwelle schwelleB2 = ETMindestschwelle.builder().id(4).niveau(B2).mindestSchwelle(2).build();
        ETMindestschwelle schwelleC1 = ETMindestschwelle.builder().id(5).niveau(C1).mindestSchwelle(2).build();
        ETMindestschwelle schwelleC2 = ETMindestschwelle.builder().id(6).niveau(C2).mindestSchwelle(2).build();


        mindestschwellen = new ArrayList<>();
        mindestschwellen.add(schwelleA1);
        mindestschwellen.add(schwelleA2);
        mindestschwellen.add(schwelleB1);
        mindestschwellen.add(schwelleB2);
        mindestschwellen.add(schwelleC1);
        mindestschwellen.add(schwelleC2);

        int ABH = faker.number().numberBetween(1,10000);


        passedDto = ETErgebnisseDto.builder()
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
                    put(B1,0);
                    put(B2,0);
                    put(C1,0);
                    put(C2,0);
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,2);
            put(A2,0);
            put(B1,0);
            put(B2,0);
            put(C1,0);
            put(C2,0);

        }});

        passedDto2 = ETErgebnisseDto.builder()
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(0)
                .RichtigeLoesungenNachNiveau(Collections.singletonList(null))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,0);
                    put(A2,0);
                    put(B1,0);
                    put(B2,0);
                    put(C1,0);
                    put(C2,0);
                }})
                .build();

        expectedDto2 = new ETErgebnisseDto(passedDto2);
        expectedDto2.setMaxErreichtesNiveau(A0);

        passedDto3 = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-3")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(7)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A2,B1,B2,C1,C2))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,0);
                    put(A2,0);
                    put(B1,0);
                    put(B2,0);
                    put(C1,0);
                    put(C2,0);
                }})
                .build();

        expectedDto3 = new ETErgebnisseDto(passedDto3);
        expectedDto3.setMaxErreichtesNiveau(A2);
        expectedDto3.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,2);
            put(A2,1);
            put(B1,1);
            put(B2,1);
            put(C1,1);
            put(C2,1);
        }});

        passedDto4 = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-4")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(7)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,B1,B1,B2,C1))
                .niveauZurZahlRichtiger(new HashMap<>(){{
                    put(A1,0);
                    put(A2,0);
                    put(B1,0);
                    put(B2,0);
                    put(C1,0);
                    put(C2,0);
                }})
                .build();

        expectedDto4 = new ETErgebnisseDto(passedDto4);
        expectedDto4.setMaxErreichtesNiveau(B1);
        expectedDto4.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,2); // Zahl richtiger Antworten je nach Niveau
            put(A2,0);
            put(B1,2);
            put(B2,1);
            put(C1,1);
            put(C2,0);
        }});
    }


    @Test
    void evaluate() {
        //given
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @Test
    void evaluate2() {
        //given
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
        //when
        var actualResult = underTest.evaluate(passedDto2);
        //then
        assertThat(actualResult).isEqualTo(expectedDto2);

    }

    @Test
    void evaluate3() {
        //given
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
        //when
        var actualResult = underTest.evaluate(passedDto3);
        //then
        assertThat(actualResult).isEqualTo(expectedDto3);

    }

    @Test
    void evaluate4() {
        //given
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
        //when
        var actualResult = underTest.evaluate(passedDto4);
        //then
        assertThat(actualResult).isEqualTo(expectedDto4);

    }
}