package solutions.dmitrikonnov.einstufungstest.businesslayer;

import com.github.javafaker.Faker;
import org.junit.Ignore;
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

    private int ABH;

    @BeforeEach
    void setUp() {
        underTest = new ETErgebnisseEvaluator(mindSchwRepoMock);
        ETMindestschwelle schwelleA1 = ETMindestschwelle.builder().id(1).niveau(A1).mindestSchwelle(2).build();
        ETMindestschwelle schwelleA2 = ETMindestschwelle.builder().id(2).niveau(A2).mindestSchwelle(2).build();
        ETMindestschwelle schwelleB1 = ETMindestschwelle.builder().id(3).niveau(B1).mindestSchwelle(2).build();
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
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
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
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
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
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
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
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
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
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
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
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
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
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
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
        given(mindSchwRepoMock.findAllByOrderByNiveau()).willReturn(mindestschwellen);
        //when
        var actualResult = underTest.evaluate(passedDto);
        //then
        assertThat(actualResult).isEqualTo(expectedDto);

    }
}