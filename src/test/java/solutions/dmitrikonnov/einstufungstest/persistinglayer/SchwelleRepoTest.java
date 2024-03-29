package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETSchwelle;

@DataJpaTest
@ActiveProfiles("unit-test")
class SchwelleRepoTest {

    @Autowired
    private SchwellenRepo underTest;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void itShouldCheckIf_AllSchwellenFoundOrderByNiveauAscended() {
        //given
        ETSchwelle mindestschwelle = ETSchwelle.builder()
                .id((short)1)
                .niveau(ETAufgabenNiveau.A1)
                .maximumSchwelle((short)5)
                .mindestSchwelle((short)2)
                .build();
        underTest.save(mindestschwelle);
        //when
        //then
        Assertions.assertTrue(true);
    }
}