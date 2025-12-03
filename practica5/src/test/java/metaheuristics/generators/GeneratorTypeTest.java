package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class GeneratorTypeTest {

    @Test
    public void values_and_valueOf_shouldWork() {
        GeneratorType[] values = GeneratorType.values();
        assertNotNull(values);
        assertTrue(values.length > 0);
        GeneratorType g = GeneratorType.valueOf("RandomSearch");
        assertEquals(GeneratorType.RandomSearch, g);
    }
}
