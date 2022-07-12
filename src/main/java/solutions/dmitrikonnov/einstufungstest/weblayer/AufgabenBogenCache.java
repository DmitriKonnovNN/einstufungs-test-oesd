package solutions.dmitrikonnov.einstufungstest.weblayer;

import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;

interface AufgabenBogenCache {

    void saveToCheck(Integer id, ETAufgabenBogen bogen);

    ETAufgabenBogen fetch(Integer id);

    void evict(Integer id);

    ETAufgabenBogen getPreparedAufgabenbogen();

}
