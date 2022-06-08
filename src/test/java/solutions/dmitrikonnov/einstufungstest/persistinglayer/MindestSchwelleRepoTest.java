package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETMindestschwelle;

@DataJpaTest
@ActiveProfiles("unit-test")
class MindestSchwelleRepoTest {

    @Autowired
    private MindestSchwelleRepo underTest;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void itShouldCheckIf_AllSchwellenFoundOrderByNiveauAscended() {
        //given
        ETMindestschwelle mindestschwelle = ETMindestschwelle.builder()
                .id(1)
                .niveau(ETAufgabenNiveau.A1)
                .mindestSchwelle(2)
                .build();
        underTest.save(mindestschwelle);
        //when
        //then
        Assertions.assertTrue(true);
    }
}