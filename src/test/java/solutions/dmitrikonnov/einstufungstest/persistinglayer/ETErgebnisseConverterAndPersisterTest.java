package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau.*;

import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisse;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisseDto;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class ETErgebnisseConverterAndPersisterTest {

    @Mock
    private ETErgebnisseRepo repoMock;
    private ETErgebnisseConverterAndPersister underTest;
    private final Faker faker = new Faker();
    private ETErgebnisseDto dtoGiven;
    private ETErgebnisse entityExpected;


    @BeforeEach
    void setUp() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        //String ID = UUID.randomUUID().toString();
        Integer ABH = faker.number().numberBetween(1,10000);
        ETAufgabenNiveau MEN = A2;
        Integer ZR = 5;
        List<ETAufgabenNiveau> RLNN = new ArrayList<>(List.of(A1, A1, A2, A2, B1));
        List<String> RNNNSTRING = new ArrayList<>(List.of("A1", "A1", "A2", "A2", "B1"));
        Map<Integer, Boolean> IDZUR = new HashMap<>(){{
            put(faker.random().nextInt(1,10000),true);
            put(faker.random().nextInt(1,10000),true);
            put(faker.random().nextInt(1,10000),false);
            put(faker.random().nextInt(1,10000),true);
            put(faker.random().nextInt(1,10000),false);
            put(faker.random().nextInt(1,10000),false);
            put(faker.random().nextInt(1,10000),true);
            put(faker.random().nextInt(1,10000),true);
            put(faker.random().nextInt(1,10000),false);

        }};
        Map<ETAufgabenNiveau, Integer> NZZR = new HashMap<>(){{
            put(A1, 2);
            put(A2, 2);
            put(B1,1);
            put(B2, 0);
            put(C1, 0);
            put(C2, 0);
        }};
        Map<String, Integer> NZZRSTRING = new HashMap<>(){{
            put("A1", 2);
            put("A2", 2);
            put("B1",1);
            put("B2", 0);
            put("C1", 0);
            put("C2", 0);
        }};

        dtoGiven = ETErgebnisseDto.builder()
               // .id(ID)
                .aufgabenBogenHash(ABH)
                .maxErreichtesNiveau(MEN)
                .zahlRichtigerAntworten(ZR)
                .RichtigeLoesungenNachNiveau(RLNN)
                .idZuRichtigkeitMap(IDZUR)
                .niveauZurZahlRichtiger(NZZR)
                .build();
        underTest = new ETErgebnisseConverterAndPersister(repoMock);

        entityExpected = ETErgebnisse.builder()
                .aufgabenBogenHash(ABH)
                .maxErreichtesNiveau(MEN)
                .zahlRichtigerAntworten(ZR)
                .richtigeLoesungenNachNiveau(RNNNSTRING)
                .idZuRichtigkeitMap(IDZUR)
                .niveauZurZahlRichtiger(NZZRSTRING)
                .build();


    }

    @AfterEach
    void tearDown() {

    }

    @RepeatedTest(2)
    void shouldConvertAndPersist() {
        //given see setUp();

        //when
        underTest.convertAndPersist(dtoGiven);
        //then
        ArgumentCaptor<ETErgebnisse> entityArgumentCaptor = ArgumentCaptor.forClass(ETErgebnisse.class);
        verify(repoMock).save(entityArgumentCaptor.capture());
        ETErgebnisse result = entityArgumentCaptor.getValue();
        assertThat(entityExpected).isEqualTo(result);
        

    }
}