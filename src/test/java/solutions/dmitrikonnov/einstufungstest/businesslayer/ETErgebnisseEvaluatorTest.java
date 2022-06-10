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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import static solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau.A1;
import static solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau.A2;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETErgebnisseEvaluatorTest {

    @Mock
    private MindestSchwelleRepo mindSchwRepoMock;
    private List<ETMindestschwelle> mindestschwellen;
    private ETErgebnisseEvaluator underTest;
    private ETErgebnisseDto passedDto;
    private ETErgebnisseDto expectedDto;
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new ETErgebnisseEvaluator(mindSchwRepoMock);
        ETMindestschwelle schwelleA1 = ETMindestschwelle.builder().id(1).niveau(A1).mindestSchwelle(1).build();
        ETMindestschwelle schwelleA2 = ETMindestschwelle.builder().id(2).niveau(A2).mindestSchwelle(1).build();
        mindestschwellen = new ArrayList<>();
        mindestschwellen.add(schwelleA1);
        mindestschwellen.add(schwelleA2);
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
                }})
                .build();

        expectedDto = new ETErgebnisseDto(passedDto);
        expectedDto.setMaxErreichtesNiveau(A1);
        expectedDto.setNiveauZurZahlRichtiger(new HashMap<>(){{
            put(A1,2);
            put(A2,0);
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
}