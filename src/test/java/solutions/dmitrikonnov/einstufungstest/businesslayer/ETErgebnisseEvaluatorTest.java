package solutions.dmitrikonnov.einstufungstest.businesslayer;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
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

@Slf4j
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETErgebnisseEvaluatorTest {

    @Mock
    private MindestSchwelleRepo mindSchwRepoMock;
    private static List<ETMindestschwelle> mindestschwellen2;
    private static List<ETMindestschwelle> mindestschwellen3;
    private ETErgebnisseEvaluator underTest;
    private ETErgebnisseDto passedDto;
    private ETErgebnisseDto expectedDto;

    private int ABH;

    @BeforeAll
    static void setUpMindestSchwelle() {
        ETMindestschwelle schwelleA1 = ETMindestschwelle.builder().id(1).niveau(A1).mindestSchwelle(2).build();
        ETMindestschwelle schwelleA2 = ETMindestschwelle.builder().id(2).niveau(A2).mindestSchwelle(2).build();
        ETMindestschwelle schwelleB1 = ETMindestschwelle.builder().id(3).niveau(B1).mindestSchwelle(2).build();
        ETMindestschwelle schwelleB2 = ETMindestschwelle.builder().id(4).niveau(B2).mindestSchwelle(2).build();
        ETMindestschwelle schwelleC1 = ETMindestschwelle.builder().id(5).niveau(C1).mindestSchwelle(2).build();
        ETMindestschwelle schwelleC2 = ETMindestschwelle.builder().id(6).niveau(C2).mindestSchwelle(2).build();

        mindestschwellen2 = new ArrayList<>();
        mindestschwellen2.add(schwelleA1);
        mindestschwellen2.add(schwelleA2);
        mindestschwellen2.add(schwelleB1);
        mindestschwellen2.add(schwelleB2);
        mindestschwellen2.add(schwelleC1);
        mindestschwellen2.add(schwelleC2);

        ETMindestschwelle schwelle3A1 = ETMindestschwelle.builder().id(1).niveau(A1).mindestSchwelle(3).build();
        ETMindestschwelle schwelle3A2 = ETMindestschwelle.builder().id(2).niveau(A2).mindestSchwelle(3).build();
        ETMindestschwelle schwelle3B1 = ETMindestschwelle.builder().id(3).niveau(B1).mindestSchwelle(3).build();
        ETMindestschwelle schwelle3B2 = ETMindestschwelle.builder().id(4).niveau(B2).mindestSchwelle(3).build();
        ETMindestschwelle schwelle3C1 = ETMindestschwelle.builder().id(5).niveau(C1).mindestSchwelle(3).build();
        ETMindestschwelle schwelle3C2 = ETMindestschwelle.builder().id(6).niveau(C2).mindestSchwelle(3).build();

        mindestschwellen3 = new ArrayList<>();
        mindestschwellen3.add(schwelle3A1);
        mindestschwellen3.add(schwelle3A2);
        mindestschwellen3.add(schwelle3B1);
        mindestschwellen3.add(schwelle3B2);
        mindestschwellen3.add(schwelle3C1);
        mindestschwellen3.add(schwelle3C2);

    }

    @BeforeEach
    void setUp() {
        underTest = new ETErgebnisseEvaluator(mindSchwRepoMock);

        Faker faker = new Faker();
        ABH = faker.number().numberBetween(1,10000);

    }


    @Test
    void evaluate() {
        //given
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
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @Test
    void evaluate2_reachedLevel_shouldBe_A0_if_noneCorrect() {
        //given
        passedDto = ETErgebnisseDto.builder()
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

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A0);
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @Test
    void evaluate3_reachedLevel_shouldBe_A1_ifOnlyA1Erreicht (){
        //given
        passedDto = ETErgebnisseDto.builder()
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

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,2);
            put(A2,1);
            put(B1,1);
            put(B2,1);
            put(C1,1);
            put(C2,1);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @Test
    void evaluate4_reachedLevel_shouldBe_A2_1_ifCorrect_2_0_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-4")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(6)
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

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A2_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,2); // Zahl richtiger Antworten je nach Niveau
            put(A2,0);
            put(B1,2);
            put(B2,1);
            put(C1,1);
            put(C2,0);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @Test
    void evaluate5_reachedLevel_shouldBe_A2_2_ifCorrect_2_1_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-5")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(7)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A2,B1,B1,B2,C1))
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
        expectedDto.setMaxErreichtesNiveau(A2_2);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,2); // Zahl richtiger Antworten je nach Niveau
            put(A2,1);
            put(B1,2);
            put(B2,1);
            put(C1,1);
            put(C2,0);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @Test
    void evaluate6_reachedLevel_shouldBe_A0_ifOnlyOneCorrect() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-6")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(7)
                .RichtigeLoesungenNachNiveau(Collections.singletonList(A1))
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
        expectedDto.setMaxErreichtesNiveau(A0);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,1); // Zahl richtiger Antworten je nach Niveau
            put(A2,0);
            put(B1,0);
            put(B2,0);
            put(C1,0);
            put(C2,0);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @Test
    void evaluate6_reachedLevel_shouldBe_A2_2_ifCorrect_3_0_3() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-6")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(8)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,B1,B1,B1,B2,C1))
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
        expectedDto.setMaxErreichtesNiveau(A2_2);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,3); // Zahl richtiger Antworten je nach Niveau
            put(A2,0);
            put(B1,3);
            put(B2,1);
            put(C1,1);
            put(C2,0);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @Test
    void evaluate7_reachedLevel_shouldBe_B1_1_ifCorrect_3_1_3() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-7")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(8)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A2,B1,B1,B1,B2,C1))
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
        expectedDto.setMaxErreichtesNiveau(B1_1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,3); // Zahl richtiger Antworten je nach Niveau
            put(A2,1);
            put(B1,3);
            put(B2,1);
            put(C1,1);
            put(C2,0);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @Test
    void evaluate8_reachedLevel_shouldBe_A1_evenIfCorrect_2_1_1_0_2_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-8")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(8)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A2,B1,C1,C1,C2,C2))
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
            put(A1,2); // Knapp erreicht = 50 % (die Mindestschwelle ist 2 richtige Lösungen)
            put(A2,1); // Niveau nicht erreicht = unter 50 %
            put(B1,1); // Niveau nicht erreicht = unter 50 %
            put(B2,0); // Von hier an werden keine Antworten mehr berücksichtigt
            put(C1,2); // ENDERGEBNIS: A1
            put(C2,2);
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }

    @Test
    void evaluate9_reachedLevel_shouldBe_B2_2_if_3_2_2_1_2_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-9")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(12)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A2,A2,B1,B1,B2,C1,C1,C2,C2))
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
        expectedDto.setMaxErreichtesNiveau(B2_2);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,3); // erreicht = 75 % (die Mindestschwelle = 2 richtige Lösungen)
            put(A2,2); // knapp erreicht = genau 50 %
            put(B1,2); // knapp erreicht = genau 50 %
            put(B2,1); // Niveau nicht erreicht = unter 50 %
            put(C1,2); // knapp erreicht = genau 50 %
            put(C2,2); // // knapp erreicht = genau 50 %
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen2);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }

    @Test
    void evaluate10_given_minLevel_3_reachedLevel_shouldBe_B1_if_4_3_3_2_2_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-10")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(16)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1,A2,A2,A2,B1,B1,B1,B2,B2,C1,C1,C2,C2))
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
        expectedDto.setMaxErreichtesNiveau(B1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,4); // erreicht = 80 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,3); // knapp erreicht = genau 60 % (3/5)
            put(B1,3); // knapp erreicht = genau 60 % (3/5)
            put(B2,2); // nicht erreicht =       40 % (2/5)
            put(C1,2); // nicht erreicht =       40 % (2/5)
            put(C2,2); // nicht erreicht =       40 % (2/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }

    @Test
    void evaluate11_given_minLevel_3_reachedLevel_shouldBe_B1_if_4_3_3_2_2_2() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-11")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(16)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A1,A2,A2,A2,B1,B1,B1,B2,B2,C1,C1,C2,C2))
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
        expectedDto.setMaxErreichtesNiveau(B1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,4); // erreicht = 80 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,3); // knapp erreicht = genau 60 % (3/5)
            put(B1,3); // knapp erreicht = genau 60 % (3/5)
            put(B2,2); // nicht erreicht =       40 % (2/5)
            put(C1,2); // nicht erreicht =       40 % (2/5)
            put(C2,2); // nicht erreicht =       40 % (2/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }
    @Test
    void evaluate12_given_minLevel_3_reachedLevel_shouldBe_B1_2_if_3_3_1_3_3_3() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-12")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(16)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A2,A2,A2,B1,B2,B2,B2,C1,C1,C1,C2,C2,C2))
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
        expectedDto.setMaxErreichtesNiveau(B1_2);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,3); // knapp erreicht = genau 60 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,3); // knapp erreicht = genau 60 % (3/5)
            put(B1,1); // nicht erreicht =       20 % (1/5)
            put(B2,3); // knapp erreicht =       60 % (2/5)
            put(C1,3); // knapp erreicht =       60 % (3/5)
            put(C2,3); // knapp erreicht =       60 % (3/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }

    @Test
    void evaluate13_given_minLevel_3_reachedLevel_shouldBe_A2_if_3_3_1_2_3_3() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-13")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(15)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A2,A2,A2,B1,B2,B2,C1,C1,C1,C2,C2,C2))
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
        expectedDto.setMaxErreichtesNiveau(A2);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,3); // knapp erreicht = genau 60 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,3); // knapp erreicht = genau 60 % (3/5)
            put(B1,1); // nicht erreicht =       20 % (1/5)
            put(B2,2); // nicht erreicht =       40 % (2/5)
            put(C1,3); // knapp erreicht =       60 % (3/5)
            put(C2,3); // knapp erreicht =       60 % (3/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }

    @Test
    void evaluate13_given_minLevel_3_reachedLevel_shouldBe_A2_evenIf_3_3_1_2_3_5() {
        //given
        passedDto = ETErgebnisseDto.builder()
                .id("PASSED-DTO-ID-13")
                .aufgabenBogenHash(ABH)
                .zahlRichtigerAntworten(17)
                .RichtigeLoesungenNachNiveau(Arrays.asList(A1,A1,A1,A2,A2,A2,B1,B2,B2,C1,C1,C1,C2,C2,C2,C2,C2))
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
        expectedDto.setMaxErreichtesNiveau(A2);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,3); // knapp erreicht = genau 60 % (die Mindestschwelle = 3 richtige Lösungen von 5)
            put(A2,3); // knapp erreicht = genau 60 % (3/5)
            put(B1,1); // nicht erreicht =       20 % (1/5)
            put(B2,2); // nicht erreicht =       40 % (2/5)
            put(C1,3); // knapp erreicht =       60 % (3/5)
            put(C2,5); // max erreicht aber egal = 100 % (5/5)
        }});
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen3);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);
        log.info("Das erreichte Niveau: " + actualResult.getMaxErreichtesNiveau());
        log.debug(passedDto.toString());

    }
}